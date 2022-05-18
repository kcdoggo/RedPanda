package redpanda.lu.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redpanda.lu.Dto.ReplyDTO;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Reply;
import redpanda.lu.Repository.BoardRepository;
import redpanda.lu.Repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReplyServiceImple implements ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Override
    public Long replyRegister(ReplyDTO dto) {

        Reply reply = dtoToEntity(dto);
        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getReplyList(Long bno) {

        Optional<Board> byId = boardRepository.findById(bno);
        Board board = byId.get();

        List<Reply> replyList = replyRepository.getReplyByBoardOrderByRno(board);

        return replyList.stream().map(reply -> entityToDto(reply)).collect(Collectors.toList());

    }

    @Override
    public void updateReply(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);

    }

    @Override
    public void removeReply(Long rno) {

        log.info("delete this reply" + rno);
        replyRepository.deleteById(rno);

    }


    public Reply dtoToEntity(ReplyDTO dto){

        Board board = boardRepository.findById(dto.getBno()).get();

        Reply reply = Reply.builder()
                .rno(dto.getRno())
                .replyContent(dto.getReplyContent())
                .replyer(dto.getReplyer())
                .board(board)
                .replyPw(dto.getReplyPw())
                .build();

        return reply;

    }






}
