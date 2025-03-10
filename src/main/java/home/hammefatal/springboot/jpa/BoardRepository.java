package home.hammefatal.springboot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // SELECT COUNT(*) FROM board WHERE writer = :writer
    int countAllByWriter(String writer);

    List<Board> findByTitleAndWriter(String title, String writer);

    @Query("SELECT b FROM Board b")  // JPQL 은 명칭의 대소문자를 구별한다.
    List<Board> findAllBoard();

    @Query("SELECT b FROM Board b WHERE b.title = ?1 AND b.writer = ?2")
    List<Board> findByTitleAndWriter2(String title, String writer);

    @Query("SELECT b FROM Board b WHERE b.title = :title AND b.writer = :writer")
    List<Board> findByTitleAndWriter3(String title, String writer);

    @Query(value = "SELECT * FROM Board", nativeQuery = true)
    List<Board> findAllBoardBySQL();

    @Query(value = "SELECT TITLE, WRITER FROM Board", nativeQuery = true)
    List<Object[]> findAllBoardBySQL2();


}
