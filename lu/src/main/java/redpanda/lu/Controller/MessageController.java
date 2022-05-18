package redpanda.lu.Controller;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import redpanda.lu.Configuration.Oauth.SessionUser;
import redpanda.lu.Dto.MessageDTO;
import redpanda.lu.Entity.LanguagePartner;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Message;
import redpanda.lu.Repository.LanguagePartnerRepository;
import redpanda.lu.Repository.MemberRepository;
import redpanda.lu.Repository.MessageRepository;
import redpanda.lu.Service.MessageService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
@ToString
public class MessageController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final MemberRepository memberRepository;
    private final LanguagePartnerRepository languagePartnerRepository;
    private final HttpSession httpSession;






    @PostMapping(value = "/send")
    public List<Message> sendMessages(@RequestBody MessageDTO messageDTO){

        /**
         *  LOGIN
         *  CustomOAuth2UserService에서 세션을 member로 정해줬음.
         */
        SessionUser member = (SessionUser) httpSession.getAttribute("member");

        messageDTO.setSender(member.getEmail());
        log.info("message info****"+messageDTO);







        Optional<Member> senderEmail = memberRepository.findByEmail(member.getEmail());
        Member sender = senderEmail.get();

        Optional<Member> receiverEmail = memberRepository.findByEmail(messageDTO.getReceiver());
        Member receiver = receiverEmail.get();

        ///추가. 닉네임 set해주기.
        Optional<LanguagePartner> senderNickname = languagePartnerRepository.findByMemberEmail(sender);
        LanguagePartner languagePartner1 = senderNickname.get();
        String sNickname = languagePartner1.getNickname();
        log.info("******sender nickname "+sNickname);
        messageDTO.setSender_nickname(sNickname);


        Optional<LanguagePartner> receiverNickname = languagePartnerRepository.findByMemberEmail(receiver);
        LanguagePartner languagePartner2 = receiverNickname.get();
        String rNickname = languagePartner2.getNickname();
        messageDTO.setReceiver_nickname(rNickname);
        log.info("******receiver nickname "+rNickname);



       messageService.registerMessage(messageDTO);

        return messageRepository.findAllBySenderAndReceiver(sender,receiver);
    }


    @PostMapping("/lists")
    public List<Message> getMessageList(String sender, String receiver){

        Optional<Member> senderO = memberRepository.findByEmail(sender);
        Member senderVal = senderO.get();

        Optional<Member> receiverO = memberRepository.findByEmail(receiver);
        Member receiverVal = receiverO.get();

        List<Message> messageList = messageRepository.findAllBySenderAndReceiver(senderVal, receiverVal);

       //추가
        List<Message> messageListW = messageRepository.findAllByReceiverAndSender(senderVal, receiverVal);

       // List<Message> messageMerged = Stream.concat(messageList.stream(), messageListW.stream()).collect(Collectors.toList());

        messageList.addAll(messageListW);
       // return messageRepository.findAllBySenderAndReceiver(senderVal,receiverVal);

        return messageList;
    }






}
