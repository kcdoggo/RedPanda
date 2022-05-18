package redpanda.lu.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseTimeEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyContent;

    //수정

    private String replyer;

    private String replyPw;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;



}
