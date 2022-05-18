package redpanda.lu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import redpanda.lu.Entity.LanguagePartner;
import redpanda.lu.Entity.Member;

import java.util.List;
import java.util.Optional;

public interface LanguagePartnerRepository extends JpaRepository<LanguagePartner, Long> {


    Optional<LanguagePartner> findByNickname(String nickname);

    Optional<LanguagePartner> findByMemberEmail(Member email);

}
