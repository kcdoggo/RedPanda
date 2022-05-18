package redpanda.lu.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


//목록처리 기능(페이지번호, 페이지 내 목록개수,검색조건을 dto로 선언)
//JPA쪽에서 사용하는 Pageable 타입 객체 생성
@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {



    private int page; //페이지번호,
    private int size; //페이지 내 목록개수

    //검색조건, 검색 키워드
    private String type;
    private String keyword;


    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    // Creates a new PageRequest with sort direction
    // of(int page, int size, Sort.Direction direction
    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1, size, sort);
    }

}
