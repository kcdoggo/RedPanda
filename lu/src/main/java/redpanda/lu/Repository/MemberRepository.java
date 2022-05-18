package redpanda.lu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redpanda.lu.Entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {


    Optional<Member> findByEmail(String email);

}
