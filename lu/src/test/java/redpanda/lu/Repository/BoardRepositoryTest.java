package redpanda.lu.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import redpanda.lu.Dto.BoardDTO;
import redpanda.lu.Dto.PageRequestDTO;
import redpanda.lu.Dto.PageResultDTO;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Member;
import redpanda.lu.Service.BoardService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void search(){
        List<Board> result = boardRepository.findByContentContains("강추");
        System.out.println("result : "+result);
    }

    @Test
    public void doSearchUsingQuery(){
        List<Board> ressult= boardRepository.getContent("강추");
        System.out.println("=========test result===="+ressult);
    }


    @Test
    public void insertBoard(){

        IntStream.rangeClosed(0,10).forEach(i->{
            Member member = Member.builder()
                    .email("boardTest"+i+"@gmail.com")
                    .build();

            Board board = Board.builder()

                    .content("content"+i)
                    .title("content"+i)
                    .writer(member)
                    .imgUrl("content"+i)
                    .build();
            boardRepository.save(board);
        });


    }

    //해당 메소드를 하나의 트랜잭션으로 처리하라. 필요할때 디비 연결 다시 생성함.  BOARD에서 MEMBER 디비 연결해줌.
    @Transactional
    @Test
    public void readBoard(){

        Optional<Board> result = boardRepository.findById(5L);
        Board board = result.get();
        System.out.println(board);
        System.out.println(board.getWriter());
    }


    @Test
    public void getBoardWithWriter(){
        Object result = boardRepository.getBoardWithWriter(5L);
        Object[] resultarr = (Object[]) result;
        System.out.println(Arrays.toString(resultarr));
    }


    //게시글과 댓글들 가져옴
    @Test
    public void getBoardsAndReplies(){

        List<Object[]> result = boardRepository.getBoardWithReplies(2L);

        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }

    }

    //Creates a new PageRequest with sort parameters applied.
    @Test
    public void replyCount(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithRepliesCount(pageable);

        //Page타입인 result는 list임. 그리고 제네릭에 객체리스트가 정의되있음
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
      /**  result.get().forEach(row -> {
            //Object[] arr = row;
            System.out.println(Arrays.toString(row));

        });
*/
    }


    //Bno로 게시글 가져오기
    @Test
    public void getBoardWithBno(){
        Object result = boardRepository.getBoardWithBno(5L);

        //쿼리문 결과가 list라 객체 리스트로 변환
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));

    }


    @Test
    public void testRegister(){

        BoardDTO boardDTO = BoardDTO.builder()
                .title("board test1")
                .content("board test1")
                .writerEmail("jaden@gmail.com")
                .build();

        boardService.register(boardDTO);

    }

    @Test
    public void getList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        // PageResultDTO  를 돌기
        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }

    }

    /**
     *  @Query(value = "select b,w,count(r) from Board b" +
     *             " left join b.writer w "+
     *             " left join Reply r on r.board = b where b.bno = :bno")
     */
    @Test
    public void getPost(){
        BoardDTO post = boardService.getPost(5L);
        System.out.println(post);
    }

    @Test
    public void deletePostNReplies(){
        boardService.deleteBoardWithReplies(8L);
    }

    @Test
    public void modifyingBoard(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(5L)
                .content("huh ")
                .title("ch")
                .imgUrl("hu")
                .build();

        boardService.modifyingContent(boardDTO);
    }


}