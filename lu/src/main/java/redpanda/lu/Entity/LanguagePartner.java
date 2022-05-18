package redpanda.lu.Entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LanguagePartner extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;

    private String nickname;

    // Many는 LanguagePart의 입장이고one이 member.
    @ManyToOne
    private Member memberEmail;

    private String gender;

    private int age;

    private String nativeLanguage;

    private String practiceLanguage;

    private String description;

    private String partnerPic;

    private String hobby;



}
