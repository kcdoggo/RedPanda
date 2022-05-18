package redpanda.lu.Service;

import redpanda.lu.Dto.BoardDTO;
import redpanda.lu.Dto.ReplyDTO;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Reply;

import java.util.List;

public interface ReplyService {

    Long replyRegister(ReplyDTO dto);


    List<ReplyDTO> getReplyList(Long bno);

    void updateReply(ReplyDTO replyDTO);

    void removeReply(Long rno);


    //Board 객체 필요없음.
    default ReplyDTO entityToDto(Reply reply){

        Board board = reply.getBoard();
        ReplyDTO replyDTO = ReplyDTO.builder()
               .rno(reply.getRno())
               .replyContent(reply.getReplyContent())
               .replyer(reply.getReplyer())
               .bno(board.getBno())
                .replyPw(reply.getReplyPw())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
               .build();

       return replyDTO;
    }


}
