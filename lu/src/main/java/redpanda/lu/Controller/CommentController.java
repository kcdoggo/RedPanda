package redpanda.lu.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redpanda.lu.Dto.ReplyDTO;
import redpanda.lu.Entity.Reply;
import redpanda.lu.Service.ReplyService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Slf4j
@RequiredArgsConstructor
public class CommentController {


    private final ReplyService replyService;



    @GetMapping(value = "/board/{bno}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){
        log.info("bno"+bno);

        return new ResponseEntity<>(replyService.getReplyList(bno),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> addComment(@RequestBody ReplyDTO replyDTO){

        log.info("reply info ******"+replyDTO);
        Long rno = replyService.replyRegister(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);

    }




}
