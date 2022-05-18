package redpanda.lu.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;


    @ManyToOne
    private Member sender;


    @ManyToOne
    private Member receiver;


    private String sender_nickname;
    private String receiver_nickname;

    private String content;


    public Member getSender() {
        return sender;
    }

    public Member getReceiver() {
        return receiver;
    }

}
