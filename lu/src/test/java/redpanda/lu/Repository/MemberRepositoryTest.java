package redpanda.lu.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redpanda.lu.Entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findUser(){
        Optional<Member> byEmail = memberRepository.findByEmail("jaden@gmail.com");
        Member result = byEmail.get();
        System.out.println("find member*******"+result);
    }


    @Test
    public void insertMember(){

        IntStream.rangeClosed(0,20).forEach(i->{
            Member member = Member.builder()
                    .email("test"+i+"@gmail.com")
                    .name("test"+i)
                    .password("test"+i)
                    .build();

            memberRepository.save(member);
        });
    }

}