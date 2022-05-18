package redpanda.lu.Service;

import redpanda.lu.Dto.LanguagePartnerDTO;
import redpanda.lu.Entity.LanguagePartner;
import redpanda.lu.Entity.Member;

import java.util.List;

public interface LanguagePartnerService {

    Long registerPartner(LanguagePartnerDTO dto);

    List<LanguagePartnerDTO> getPartnerList();


    //Entity to DTO
    default LanguagePartnerDTO entityToDto(LanguagePartner languagePartner){

        Member memberEmail = languagePartner.getMemberEmail();

        LanguagePartnerDTO dto = LanguagePartnerDTO.builder()
                .lno(languagePartner.getLno())
                .email(memberEmail.getEmail())
                .nickname(languagePartner.getNickname())
                .age(languagePartner.getAge())
                .partnerPic(languagePartner.getPartnerPic())
                .description(languagePartner.getDescription())
                .nativeLanguage(languagePartner.getNativeLanguage())
                .practiceLanguage(languagePartner.getPracticeLanguage())
                .gender(languagePartner.getGender())
                .hobby(languagePartner.getHobby())
                .regDate(languagePartner.getRegDate())
                .modDate(languagePartner.getModDate())
                .build();


        return dto;
    }

}
