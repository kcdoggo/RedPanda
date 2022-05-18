package redpanda.lu.Entity;

import lombok.*;
import redpanda.lu.Dto.BoardDTO;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer") //ToString은 모든 멤버변수 출력하는데, Member객체 역시 출력되서 지연로딩때 빼줌.
@Getter
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @Column(nullable = true)
    private String imgUrl;

    //Board를 영속성컨텍스트에 저장하면서, 여기 Member도 같이 저장하겠다.
    @ManyToOne( fetch = FetchType.LAZY)
    private Member writer;


    // title, content,imgUrl만 변경가능하게.
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }



}
