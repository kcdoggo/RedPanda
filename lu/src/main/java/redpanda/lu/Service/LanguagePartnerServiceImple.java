package redpanda.lu.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redpanda.lu.Dto.LanguagePartnerDTO;
import redpanda.lu.Entity.LanguagePartner;
import redpanda.lu.Entity.Member;
import redpanda.lu.Repository.LanguagePartnerRepository;
import redpanda.lu.Repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class LanguagePartnerServiceImple implements LanguagePartnerService{

    private final LanguagePartnerRepository languagePartnerRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<LanguagePartnerDTO> getPartnerList() {

        List<LanguagePartner> partnerList = languagePartnerRepository.findAll();


        //stream으로 돌며,map에서 연산, collect에서 반환하고 싶은 타입 적음.
        List<LanguagePartnerDTO> partnerListResult = partnerList.stream().map(arr ->
                entityToDto(arr)
        ).collect(Collectors.toList());

        //Object[] 타입 인자(JPQL의 결과) 받고, LanguagePartnerDTO 객체를 리턴할거임.
        /**Function<Object[], LanguagePartnerDTO> fn = (entityType -> entityToDto(
                (LanguagePartner) entityType[0]
        ));**/

        return partnerListResult ;
    }


    @Override
    public Long registerPartner(LanguagePartnerDTO dto) {

        LanguagePartner result = dtoToEntity(dto);
        LanguagePartner result_jpa = languagePartnerRepository.save(result);
        return result_jpa.getLno();

    }


    //DTO to ENTITY
    public LanguagePartner dtoToEntity(LanguagePartnerDTO dto){

        String email = dto.getEmail();
        Optional<Member> byEmail = memberRepository.findByEmail(email);

        LanguagePartner languagePartner = LanguagePartner.builder()
                .lno(dto.getLno())
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .memberEmail(byEmail.get())
                .partnerPic(dto.getPartnerPic())
                .description(dto.getDescription())
                .nativeLanguage(dto.getNativeLanguage())
                .practiceLanguage(dto.getPracticeLanguage())
                .gender(dto.getGender())
                .hobby(dto.getHobby())

                .build();

        return languagePartner;
    }








}
