package redpanda.lu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import redpanda.lu.Entity.Member;
import redpanda.lu.Entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Long> {



    List<Message> findAllBySenderAndReceiver(Member sender, Member receiver);
    List<Message> findAllByReceiverAndSender(Member receiver, Member sender);

    @Query(nativeQuery = true,value = "select distinct(mes.sender_nickname) from message mes left join member m on mes.sender_email = m.email where mes.receiver_email = ?1")
    List<String> findAllByReceiver(String receiver);


   // List<Message> findAllByReceiver(String receiver);


}
