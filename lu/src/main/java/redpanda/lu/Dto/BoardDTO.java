package redpanda.lu.Dto;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Member;
import redpanda.lu.Repository.MemberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
//@Getter
//@Setter
public class BoardDTO {


    private Long bno;
    private String title;
    private String content;
    private String imgUrl;
    private String tag;

    private String writerEmail;
    private String writerName;

    //추가
    private String writerPic;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private int replyCount;







}
