package redpanda.lu.Dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageDTO {

    private Long mid;
    private String receiver;
    private String sender;
    private String content;

    private String sender_nickname;
    private String receiver_nickname;
}
