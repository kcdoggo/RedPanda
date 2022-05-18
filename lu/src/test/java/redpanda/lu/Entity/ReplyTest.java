package redpanda.lu.Entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redpanda.lu.Dto.ReplyDTO;
import redpanda.lu.Repository.BoardRepository;
import redpanda.lu.Repository.ReplyRepository;
import redpanda.lu.Service.ReplyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyService replyService;

    @Test
    public void insertReply(){

        IntStream.rangeClosed(1,10).forEach(i->{


            Member member = Member.builder()
                    .email("replytest"+i+"@gmail.com")
                    .build();

            Board board = Board.builder()
                    .bno(2L)
                    .writer(member)
                    .build();

            Reply reply = Reply.builder()
                    .replyContent("replytest")
                    .replyer("replytest")
                    .board(board)
                    .build();

            replyRepository.save(reply);
        });
    }



    @Test
    public void getReply(){

        /**
        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.get();
        List<Reply> replyByBoardOrderByRno = replyRepository.getReplyByBoardOrderByRno(board);

        System.out.println(replyByBoardOrderByRno);**/

        List<ReplyDTO> replyList = replyService.getReplyList(1L);
        System.out.println(replyList);
    }

}