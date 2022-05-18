package redpanda.lu.Dto;


//JPA에서 페이지 처리결과를 Page<Entity> 타입으로 반환
//그럼 Page<Entity> 엔티티 객체를 DTO객체로 변환해 자료구조에 담아야함.

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data   //이 클래스를 공용으로 사용하게 제네릭 타입으로 선언
public class PageResultDTO<DTO, EN> {

    //DTO 리스트
    private List<DTO> dtoList;

    //총 페이지 번호 ex.33 Pages
    private int totalPage;

    //현재 페이지 번호 4page
    private int page;

    //목록 사이즈 , ex. 1페이지에 10개의 목록
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start;
    private int end;

    //이전, 다음
    private boolean prev;
    private boolean next;


    //페이지 번호 목록
    private List<Integer> pageList;


    //여기 Function은 Entity타입을 받아서, DTO객체로 리턴해주겠다는 의미.
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn) {

        //컬렉션 객체(list)인 result를 stream메소드로 돌기.
        //collect()로 원하는 자료형(list)로 바꿔주기.
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){

        // 0페이지 부터 시작하니까 1추가
        this.page = pageable.getPageNumber() +1;
        this.size = pageable.getPageSize();

        // 임시 끝번호. 다 10씩 더함. 11 페이지면, 20 페이지가 보이도록.
        // 15페이지 % 10 이면 1.5고 반올림 ceil이면 2가 되고, 10을 곱하면 20페이지.
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd -9;

        prev = start > 1;

        // 총 페이지가 33이고 ,현재 임시 끝 번호가 20이면, 끝 번호 20
        // 총 페이지가 33이고, 현재 임시 끝 번호가 40이라면, 끝 번호 33
        end = totalPage > tempEnd ? tempEnd : totalPage;


        next = totalPage > tempEnd;

        //boxed(primitive타입을 wrapper타입으로 박싱해 변환)
        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }






}
