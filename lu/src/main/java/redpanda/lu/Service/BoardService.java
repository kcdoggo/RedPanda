package redpanda.lu.Service;

import redpanda.lu.Dto.BoardDTO;
import redpanda.lu.Dto.PageRequestDTO;
import redpanda.lu.Dto.PageResultDTO;
import redpanda.lu.Entity.Board;
import redpanda.lu.Entity.Member;


public interface BoardService {

    Long register(BoardDTO dto);

    /**
    // 게시글을 저장할때. dto를 entity로 변환해 jpa이용해 저장.
    default Board dtoToEntity(BoardDTO dto){


         * 기존버전.
         * 멤버 객체를 여기서 생성을 하는데 이러면 회원가입 하는 격이라서 안됨.
        //멤버 객체를 불러와서 넣어야 하는 것 같음.
        Member member = Member.builder()
                .email(dto.getWriterEmail())
                .build();


    }
     **/

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);


    //PageResultDTO
    //JPQL 실행결과인 객체 어레이 Object[]를 BoardDTO로 바꿔줘야 함.
    default BoardDTO entityToDto(Board board, Member member, Long replyCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .imgUrl(board.getImgUrl())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .writerPic(member.getPicture())
                .modDate(board.getModDate())
                .regDate(board.getRegDate())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }


    BoardDTO getPost(Long bno);

    // 댓글 먼저 삭제하고, 게시글도 삭제, 하나의 트랜잭션으로 이뤄져야 함.
    void deleteBoardWithReplies(Long bno);

    void modifyingContent(BoardDTO boardDTO);
}
