package redpanda.lu.Service;

import redpanda.lu.Dto.MessageDTO;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Message;

public interface MessageService {

    Long registerMessage(MessageDTO dto);

    default MessageDTO entityToDto(Message message){
        Member receiver = message.getReceiver();
        String receiverEmail = receiver.getEmail();

        Member sender = message.getSender();
        String senderEmail = sender.getEmail();


        MessageDTO messageDTO = MessageDTO.builder()
                .mid(message.getMid())
                .content(message.getContent())
                .receiver(receiverEmail)
                .sender(senderEmail)
                .receiver_nickname(message.getReceiver_nickname())
                .sender_nickname(message.getSender_nickname())
                .build();

        return messageDTO;
    }

}
