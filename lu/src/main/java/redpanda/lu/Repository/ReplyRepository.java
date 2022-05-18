package redpanda.lu.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    //게시글 삭제할때, 해당 게시글에 있는 댓글 먼저 삭제 후 게시글 삭제함.
    //JPQL 이용해 삭제,업데이트할때 @Modifying 어노테이션 꼭 달아줌.
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);


    // 게시글로 댓글 가져오기.
    List<Reply> getReplyByBoardOrderByRno(Board board);



}
