package redpanda.lu.Dto;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LanguagePartnerDTO {


    private Long lno;

    private String nickname;

    private String email;

    private String gender;

    private int age;

    private String nativeLanguage;

    private String practiceLanguage;

    private String description;

    private String partnerPic;

    private String hobby;

    private LocalDateTime regDate;

    private LocalDateTime modDate;


}
