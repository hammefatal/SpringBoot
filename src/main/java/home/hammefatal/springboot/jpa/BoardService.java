package home.hammefatal.springboot.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private BoardRepository boardRepository;

    // 전체 게시물을 가져오는 메소드
    public List<Board> getList() {
        return (List<Board>) boardRepository.findAll();
    }

    // 게시물을 작성하는 메소드
    public Board write(Board board) {
        return boardRepository.save(board);
    }

    // 게시물을 읽어오는 메소드
    public Board read(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시물을 수정하는 메소드
    public Board modify(Board newBoard) {
        Board board = boardRepository.findById(newBoard.getId()).orElse(null);

        if (board == null) return null;
        board.setTitle(newBoard.getTitle());
        board.setContent(newBoard.getContent());

        return boardRepository.save(board);
    }

    // 게시물을 삭제하는 메소드
    public void remove(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null)
            boardRepository.deleteById(id);
    }

}
