package redpanda.lu.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import redpanda.lu.Entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    //목록에서 게시글 정보와 댓수 가져옴.
    // Board에서 writer 연관관계정의해서 on없이도 b.writer로 조인해서 alias 로 w줌.

    @Query("select b,w from Board b left join b.writer w where b.bno = :bno ")
    Object getBoardWithWriter(@Param("bno") Long bno);


    //게시글과 댓글을 같이 가져옴. Board 에서 Reply객체 잠조 안하고 있기때문에
    // 직접조인 on을 이용함 left join Reply r on r.board = b
    @Query("select b,r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReplies(@Param("bno") Long bno);


    
    //목록 처리 with Pageable
    //하나의 게시글 당(group by)
    //게시글(게시번호,제목,작성시간),회원(이름,이메일),댓글 수 가져오기
    //띄어쓰기 조심.
    // Board + Member 조인,  Board + Reply 조인인
   @Query(value="select b, w, count(r) from Board b"+
            " left join b.writer w"+
            " left join Reply r on r.board = b group by b",
                 countQuery = "select count(b) from Board b" )
    Page<Object[]> getBoardWithRepliesCount(Pageable pageable);


    // bno로 보드 가져왹
    @Query(value = "select b,w,count(r) from Board b" +
            " left join b.writer w "+
            " left join Reply r on r.board = b where b.bno = :bno")
    Object getBoardWithBno(@Param("bno") Long bno);



    /**
    @Query(value = "select b from Board b where b.content = :content")
    List<Object[]> getContent(@Param("content") String content);
    **/


    List<Board> findByContentContains(String content);

    @Query("select b from Board b where b.content like '%content%'")
    List<Board> getContent(@Param("content") String content);


}
