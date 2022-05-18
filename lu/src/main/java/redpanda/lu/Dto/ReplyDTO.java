package redpanda.lu.Dto;


import lombok.*;
import redpanda.lu.Entity.Board;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReplyDTO {

    private Long rno;

    private String replyContent;

    private String replyer;

    private Long bno;

    //추가
    private String replyPw;

    private LocalDateTime regDate;
    private LocalDateTime modDate;


}
