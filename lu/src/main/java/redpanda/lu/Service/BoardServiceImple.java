package redpanda.lu.Service;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import redpanda.lu.Dto.BoardDTO;
import redpanda.lu.Dto.PageRequestDTO;
import redpanda.lu.Dto.PageResultDTO;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Member;
import redpanda.lu.Repository.BoardRepository;
import redpanda.lu.Repository.MemberRepository;
import redpanda.lu.Repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImple implements BoardService{

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;

    // 수정 버전. 글 등록시, 멤버 이메일 같이 넣어줌.
    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
    }

    /**
     * 기존 버전.
     * @Override
    public Long register(BoardDTO dto) {
        log.info(String.valueOf(dto));

        Board board = dtoToEntity(dto);
        boardRepository.save(board);

        return board.getBno();
    }**/


    public Board dtoToEntity(BoardDTO dto){

        /***멤버 찾아오기
        Member member = Member.builder()
                .email(dto.getWriterEmail())
                .build();
**/
        Member member = memberRepository.findByEmail(dto.getWriterEmail()).get();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .imgUrl(dto.getImgUrl())
                .writer(member)
                .build();

        return board;


    }



    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {


        log.info(String.valueOf(pageRequestDTO));

        //entityToDTO 메소드를 사용할거임. 패러미터 board,member,replyCount넣어주기.
        //Object[] 타입 인자(JPQL의 결과) 받고, BoardDTO 객체를 리턴할거임.
        Function<Object[], BoardDTO> fn = (entityType -> entityToDto(
                (Board) entityType[0], (Member) entityType[1],(Long) entityType[2]
        ) );




        //리포지토리에 만든 메소드 사용.
        /**
         *     @Query(value="select b, w, count(r) from Board b"+
         *             " left join b.writer w"+
         *             " left join Reply r on r.board = b group by b",
         *                  countQuery = "select count(b) from Board b" )
         */
        Page<Object[]> result = boardRepository.getBoardWithRepliesCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));



        return new PageResultDTO<>(result,fn);
    }

    @Override
    public BoardDTO getPost(Long bno) {

        Object result = boardRepository.getBoardWithBno(bno);

        //JPQL결과가  Object[] 타입으로 리턴해줌.
        Object[] arr = (Object[]) result;

        return entityToDto((Board) arr[0],(Member) arr[1],(Long) arr[2]);

    }

    @Override
    @Transactional  // 댓글 먼저 삭제하고, 게시글도 삭제, 하나의 트랜잭션으로 이뤄져야 함.
    public void deleteBoardWithReplies(Long bno) {

        //댓부터 삭제
        replyRepository.deleteByBno(bno);

        //후, 게시글 삭제
        boardRepository.deleteById(bno);
    }

    @Override
    @Transactional
    public void modifyingContent(BoardDTO boardDTO) {

        //Board 엔티티 가져온다음
        Board board = boardRepository.getById(boardDTO.getBno());
        System.out.println("***********board : "+board);

        //엔티티 변경
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        board.changeImgUrl(boardDTO.getImgUrl());

        //변경후 엔티티를 리포지토리 영속성 컨텍스트에 저장.
        boardRepository.save(board);

    }




}
