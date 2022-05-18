package redpanda.lu.Controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redpanda.lu.Configuration.Oauth.SessionUser;
import redpanda.lu.Dto.BoardDTO;
import redpanda.lu.Dto.LanguagePartnerDTO;
import redpanda.lu.Dto.PageRequestDTO;
import redpanda.lu.Dto.ReplyDTO;
import redpanda.lu.Entity.*;
import redpanda.lu.Repository.*;
import redpanda.lu.Service.BoardService;
import redpanda.lu.Service.LanguagePartnerService;
import redpanda.lu.Service.ReplyService;
import redpanda.lu.Service.S3Uploader;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/thread")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final MemberRepository memberRepository;
    private final LanguagePartnerService languagePartnerService;
    private final ReplyRepository replyRepository;
    private final LanguagePartnerRepository languagePartnerRepository;
    private final MessageRepository messageRepository;
    private final BoardRepository boardRepository;
    private final HttpSession httpSession;
    private final S3Uploader s3Uploader;




    @GetMapping("/main")
    public String mainPage(){
        return "main";

    }


    @PostMapping("/search")
    public String doSearch(String content, Model model){
        List<Board> byContentContains = boardRepository.findByContentContains(content);
        log.info("===========search result============="+byContentContains);

        model.addAttribute("searchKeyword", content);
        model.addAttribute("result",byContentContains);
        return "search";
    }


    @GetMapping("/korean")
    public String koreanThreadList(PageRequestDTO pageRequestDTO, Model model){
        log.info("**********korean thread**********");
        model.addAttribute("list",boardService.getList(pageRequestDTO));


        /**
         *  LOGIN
         *  CustomOAuth2UserService에서 세션을 member로 정해줬음.
         */
        SessionUser member = (SessionUser) httpSession.getAttribute("member");
        if(member != null){
            model.addAttribute("userInfo",member.getName());
        }


        return "korean";
    }

    @GetMapping("/customLogin")
    public String customLogin(){

        return "customLoginPage";
    }

    @GetMapping("/post-k-questions")
    public String postingQuestions(){
        log.info("**********posting area**********");
        SessionUser member = (SessionUser) httpSession.getAttribute("member");

        return "posting-questions";
    }

    @GetMapping("/language-partner")
    public String languagePartner(PageRequestDTO pageRequestDTO, Model model){
        log.info("**********language partner**********");

        List<LanguagePartnerDTO> partnerList = languagePartnerService.getPartnerList();
        log.info("partner list*****"+partnerList);
        model.addAttribute("partnerResult", partnerList);
        return "language_partner";
    }

    @GetMapping("/posting-partner")
    public String postingPartner(){

        return "posting-partner";
    }

    @PostMapping("/post-partner")
    public String postPartnerProccessed(LanguagePartnerDTO dto, MultipartFile multipartFile) throws IOException {


        log.info("*********dto info"+dto);
        log.info("*********multipart"+multipartFile);

        SessionUser member = (SessionUser) httpSession.getAttribute("member");
        String email = member.getEmail();
        dto.setEmail(email);

        String url = s3Uploader.upload(multipartFile,"static");
        dto.setPartnerPic(url);
        languagePartnerService.registerPartner(dto);

        return "redirect:/thread/language-partner";
    }


    @PostMapping("/sendingMessages")
    public void sendMessagePage(String nickname, Model model){

        log.info("nickname********"+nickname);
        Optional<LanguagePartner> byNickname = languagePartnerRepository.findByNickname(nickname);

         Member receiver  = byNickname.get().getMemberEmail();
         log.info("receiver***");
         log.info("receiver***"+receiver);


         SessionUser sessionUser = (SessionUser) httpSession.getAttribute("member");
        String senderEmail = sessionUser.getEmail();

        Optional<Member> senderBy = memberRepository.findByEmail(senderEmail);
        Member sender = senderBy.get();

        List<Message> messages = messageRepository.findAllBySenderAndReceiver(sender, receiver);

        log.info("messages : "+messages.stream().map(i->i.getContent()).collect(Collectors.toList()));

        model.addAttribute("receiver", receiver.getEmail());
        model.addAttribute("messageList",messages);
    }


    @GetMapping("/mailbox")
    public String mailbox(Model model){

        SessionUser member = (SessionUser) httpSession.getAttribute("member");
        Optional<Member> rec = memberRepository.findByEmail(member.getEmail());
        Member receiver = rec.get();
        log.info("**receiver**"+receiver);
        

        //수정
        List<String> receiverList = messageRepository.findAllByReceiver(receiver.getEmail());
        log.info("receiver list*****V"+String.valueOf(receiverList));

        model.addAttribute("receiverInfo",receiverList);

        return "mailbox";
    }



    @PostMapping("post")
    public String posting(BoardDTO dto, RedirectAttributes redirectAttributes){

        SessionUser member = (SessionUser) httpSession.getAttribute("member");
        if(member != null){
           // model.addAttribute("userInfo",member.getName());
            dto.setWriterEmail(member.getEmail());
            dto.setWriterName(member.getName());
            dto.setWriterPic(member.getPicture());

        }

        log.info("***dto***"+dto);
        Long bno = boardService.register(dto);

        //addFlash로 전달한 값은 url에 안붙음. 리프레시하면 소멸.
        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/thread/korean";

    }



    // @ModelAttribute 객체(pageRequestDTO)에 HTTP값을 바인딩함.(그 객체에 setter정의되있어야함.)
    // 그리고 그 어노테이션 붙은 객체가 자동으로 Model객체에 추가되어 뷰단까지 전달.
    @GetMapping("/getpost")
    public void getPost(Long bno, Model model, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO){
        log.info("********bno***"+bno);
        SessionUser member = (SessionUser) httpSession.getAttribute("member");

        BoardDTO postInfo = boardService.getPost(bno);
        List<ReplyDTO> replyList = replyService.getReplyList(bno);

        log.info("****post info****"+postInfo);
        log.info("****reply list info****"+replyList);


        model.addAttribute("post",postInfo);
        model.addAttribute("reply",replyList);

    }



    // @ModelAttribute 객체(pageRequestDTO)에 HTTP값을 바인딩함.(그 객체에 setter정의되있어야함.)
    // 그리고 그 어노테이션 붙은 객체가 자동으로 Model객체에 추가되어 뷰단까지 전달.
    @GetMapping("/modify-content")
    public void modifyingContent(Long bno, Model model){
        log.info("****update this post **"+bno);

        BoardDTO postInfo = boardService.getPost(bno);
        log.info("****post info****"+postInfo);

        model.addAttribute("post",postInfo);


    }

    @PostMapping("/update-post")
    public String update(BoardDTO dto){

        boardService.modifyingContent(dto);

        return "redirect:/thread/korean";

    }



    @PostMapping("/delete-content")
    public String  delete(Long bno){


        boardService.deleteBoardWithReplies(bno);

        return "redirect:/thread/korean";

    }



    @PostMapping("/delete-comment")
    public String deleteComment(String replyPw, Long rno, Long bno, Model model){

        log.info("*****rno"+replyPw);
        Optional<Reply> byId = replyRepository.findById(rno);
        Reply reply = byId.get();
        String originalPw = reply.getReplyPw();
        log.info("original pw"+ originalPw);


        log.info("****** reply info"+byId);

        if(replyPw.equals(originalPw)){
            replyRepository.deleteById(rno);
        }

        return "redirect:/thread/getpost?bno="+bno+"&page=1&type=&keyword=";
    }
}
