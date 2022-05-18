package redpanda.lu.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void findMessagList(){

        Optional<Member> byEmail = memberRepository.findByEmail("uzs887@gmail.com");
        Member sender = byEmail.get();

        Optional<Member> byEmaill = memberRepository.findByEmail("cdoggokk@gmail.com");
        Member receiver = byEmaill.get();

        List<Message> byRece = messageRepository.findAllByReceiverAndSender(sender,receiver);
        List<Message> bySec = messageRepository.findAllBySenderAndReceiver(sender,receiver);

        byRece.addAll(bySec);

        //System.out.println(bySenderAndReceiver.stream().map(i -> i.getContent()).collect(Collectors.toList()));
        for(Message arr : byRece){
            System.out.println(arr.getContent());
        }

    }


    @Test
    public void sendingMessage(){

        Member s = Member.builder()
                .email("cdoggokk@gmail.com")
                .build();

        Member r = Member.builder()
                .email("uzs887@gmail.com")
                .build();

        Message message = Message.builder()
                .mid(1L)
                .sender(s)
                .receiver(r)
                .content("heyhey")
                .build();

        messageRepository.save(message);

    }




}