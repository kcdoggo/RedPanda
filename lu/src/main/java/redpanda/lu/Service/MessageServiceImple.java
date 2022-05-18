package redpanda.lu.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redpanda.lu.Dto.MessageDTO;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Message;
import redpanda.lu.Repository.MemberRepository;
import redpanda.lu.Repository.MessageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImple implements MessageService{

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long registerMessage(MessageDTO dto) {

        Message message = dtoToEntity(dto);

        messageRepository.save(message);
        return message.getMid();
    }

    public Message dtoToEntity(MessageDTO dto){

        Optional<Member> sender = memberRepository.findByEmail(dto.getSender());
        Member senderVal = sender.get();

        Optional<Member> receiver = memberRepository.findByEmail(dto.getReceiver());
        Member receiverVal = receiver.get();



        Message message = Message.builder()
                .content(dto.getContent())
                .sender(senderVal)
                .receiver(receiverVal)
                .receiver_nickname(dto.getReceiver_nickname())
                .sender_nickname(dto.getSender_nickname())
                .build();

        return message;
    }


}
