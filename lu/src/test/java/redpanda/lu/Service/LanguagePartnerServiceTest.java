package redpanda.lu.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redpanda.lu.Dto.LanguagePartnerDTO;
import redpanda.lu.Entity.LanguagePartner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LanguagePartnerServiceTest {

    @Autowired
    LanguagePartnerService service;

    @Test
    public void register(){

        LanguagePartner partner = LanguagePartner.builder()
                .lno(1L)
                .nickname("joke")
                .age(25)
                .build();

        LanguagePartnerDTO dto = service.entityToDto(partner);
        service.registerPartner(dto);

    }


    @Test
    public void getPartnerList(){

        List<LanguagePartnerDTO> partnerList = service.getPartnerList();
        System.out.println(partnerList.toString());
    }


}