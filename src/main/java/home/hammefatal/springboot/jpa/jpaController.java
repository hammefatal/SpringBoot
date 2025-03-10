package home.hammefatal.springboot.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class jpaController {

    /*
    EntityManagerFactory : EntityManager 를 생성하는 팩토리. 애플리케이션에 하나만 존재한다.
    EntityManager : JPA 의 핵심. 데이터베이스를 조작하는 기능을 제공한다.
                    저장 - persist()
                    조회 - find()
                    수정 - setter(), merge()
                    삭제 - remove()
    Entity : 데이터베이스 테이블에 대응하는 자바 객체.
             DB 테이블에서 하나의 행을 나타낸다. (대소문자를 구분하지 않는다.)
    EntityTransaction : 트랜잭션을 제어하는 객체.
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction tx = entityManager.getTransaction();

            User user = new User();
            user.setId("abcd");
            user.setPw("1234");
            ....

            try {
                tx.begin();
                entityManager.persist(user);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
            } finally {
                entityManager.close();
            }

    Persistence : 데이터를 영구적으로 저장하는 것.
    PersistenceContext : 엔티티 매니저가 관리하는 영속성 컨텍스트. Entity 를 저장하는 공간이다. (1차 캐시)
                         persist() 메소드로 Entity 를 저장하면, Entity 는 영속성 컨텍스트에 저장된다.
                            em.persist(user); -> 영속성 컨텍스트에 저장
                            em.remove(user); -> 영속성 컨텍스트에서 삭제
                            em.flush(); -> 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
                            em.clear(); -> 영속성 컨텍스트 초기화
                         ( ApplicationContext -> Bean 을 저장하는 공간, PersistenceContext -> Entity 를 저장하는 공간 )

     */

    private final EntityManagerFactory emf;
    private final BoardRepository boardRepository;

    public void jpaTest() {
        EntityManager em = emf.createEntityManager();
        System.out.println("em = " + em);

        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setId(1L);
        user.setPassword("1234");
        user.setName("홍길동");
        user.setEmail("email@email.com");

        tx.begin(); // 트랜잭션 시작
        // 1. 저장
        em.persist(user);

        // 2. 변경
        user.setName("변경된 이름");
        tx.commit();    // 트랜잭션 커밋 (영속성 컨텍스트의 변경 내용을 데이터베이스에 반영)

        // 3. 조회
        User findUser = em.find(User.class, 1L);

        // 4. 삭제
        tx.begin();
        em.remove(user);
        tx.commit();
    }

    /*
    Spring Data - 저장소 종류가 달라도 일관된 데이터 처리 방법을 제공한다.
    Spring Data JPA - JPA 를 위한 저장소(JpaRepository) 와 관련된 기능을 제공한다.

    CrudRepository : CRUD 기능을 제공하는 인터페이스
        조회
            count() : 저장된 엔티티의 개수를 반환
            existsById(ID id) : 엔티티가 존재하는지 여부를 반환
            findById(ID id) : 엔티티를 조회
            findAll() : 모든 엔티티 조회
            findAllById(Iterable<ID> ids) : 여러 엔티티 조회
        변경/저장
            save(S entity) : 엔티티 저장
            saveAll(Iterable<S> entities) : 여러 엔티티 저장
        삭제
            deleteById(ID id) : ID 로 엔티티 삭제
            delete(T entity) : 엔티티 삭제
            deleteAll(Iterable<? extends T> entities) : 여러 엔티티 삭제
            deleteAll() : 모든 엔티티 삭제
            deleteAllById(Iterable<? extends ID> ids) : 여러 ID 로 엔티티 삭제

    PagingAndSortingRepository : 페이징과 정렬 기능을 제공하는 인터페이스 / CrudRepository 상속
        findAll(Pageable pageable) : 페이징 처리
        findAll(Sort sort) : 정렬 처리

     */

    public void insertTest() {
        Board board = new Board();
        board.setTitle("Test Title");
        board.setContent("Test Content");
        board.setWriter("Test Writer");
        board.setViewCount(0L);
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());

        boardRepository.save(board);
    }

    public void updateTest() {
        Board board = boardRepository.findById(1L).orElse(null);
        board.setTitle("Test Title2");
        boardRepository.save(board);

        Board board2 = boardRepository.findById(2L).orElse(new Board());
    }

    /*
    JPA 가 지원하는 쿼리

    1. JPQL : JPA Query Language (JPA + SQL)
              DB 테이블이 아닌 Entity 객체를 대상으로 쿼리를 작성한다. (SQL 과 유사하다. 대소문자 구별)
        @Query("SELECT b FROM Board b WHERE b.title = ?1")
        List<Board> findByTitle(String title);

    **2. 쿼리 메소드 (Query Method) : 메소드 이름을 분석하여 JPQL 을 자동 생성한다. (Spring Data 에서 제공하는 기능)
                                   e.g. findBy + 필드명 + 조건
                                   e.g. find + (entity명) + By + 필드명 + 조건
                                   https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
        List<Board> findByTitle(String title);
        List<Board> findByTitleAndContent(String title, String content);
        List<Board> findByTitleOrContent(String title, String content);
        List<Board> findByTitleLike(String title);
        List<Board> findByTitleContaining(String title);
        List<Board> findByTitleStartingWith(String title);
        List<Board> findByTitleEndingWith(String title);
        List<Board> findByTitleIgnoreCase(String title);
        List<Board> findByTitleOrderByCreatedAtDesc(String title);
        List<Board> findByTitleOrderByCreatedAtAsc(String title);
        List<Board> findByTitleOrderByCreatedAtDesc();
        List<Board> findByTitleOrderByCreatedAtAsc();

    3. JPA Criteria : JPQL 을 메소드의 조합으로 작성한다.
                      JPA 표준이지만 불편하다.
        cq.select(board).where(cb.equal(board.get("title"), title));

    **4. QueryDSL : JPQL 을 메소드의 조합으로 작성한다. (JPA Criteria 보다 편리하고, 간결하다. 오픈 소스)
                  QueryDSL 을 사용하기 위해서는 QueryDSL 의존성을 추가해야 한다.
        List<Board> list = queryFactory.selectFrom(board)
                .where(board.title.eq(title))
                .fetch();
    5. Native Query : SQL 을 직접 작성한다. (JPQL 과 다르게 대소문자 구별) (성능이 좋다. JPQL 보다 빠르다.) 복잡한 쿼리 작성이 가능하다.
        @Query(value = "SELECT * FROM board WHERE title = ?1", nativeQuery = true)
        List<Board> findByTitle(String title);
     */

    /*
    [ 직접 JPQL 을 작성하는 방법 ]

    1. EntityManager 의 createQuery() 메소드를 사용한다.
        String query = "SELECT b FROM Board b WHERE b.title = :title";
        TypedQuery<Board> typedQuery = em.createQuery(query, Board.class);
        List<Board> list = typedQuery.setParameter("title", title).getResultList();

    2. @Query 어노테이션을 사용한다. (메소드 이름은 쿼리와 상관 없음)
        @Query("SELECT b FROM Board b WHERE b.title = :title")
        List<Board> findByTitle(String title);


    [ JPQL 의 매개변수를 지정하는 방법 ]

    1. 매개변수 순서(default) - ?1 : 첫 번째 매개변수, ?2 : 두 번째 매개변수
        @Query("SELECT b FROM Board b WHERE b.title = ?1 AND b.writer = ?2")
        List<Board> findByTitleAndWriter(String title, String writer);

    2. 매개변수 이름 - :매개변수명. @Param("이름") 으로 매개변수명을 지정하여 바인딩한다.
        @Query("SELECT b FROM Board b WHERE b.title = :title AND b.writer = :writer")
        List<Board> findByTitleAndWriter(@Param("title") String title, @Param("writer") String writer);


    [ 네이티브 쿼리(Native Query) 사용 방법 ]

    @Query 로 네이티브 쿼리 (DB에 종속된 쿼리) 를 사용할 수 있다.
        @Query(value = "SELECT * FROM board WHERE title = ?1", nativeQuery = true)
        List<Board> findByTitle(String title);

    일부 컬럼만 조회할 때는 반환 타임을 List<Object[]> 로 지정한다.
        @Query(value = "SELECT title, content FROM board WHERE title = ?1", nativeQuery = true)
        List<Object[]> findByTitle(String title);


    [ 페이징과 정렬 ]

    페이징 - @Query 붙은 메소드에 Pageable 을 매개변수로 받아서 사용한다.
        @Query("SELECT b FROM Board b WHERE b.title = :title")
        List<Board> findByTitle(String title, Pageable pageable);

    정렬 - 오름차순(Sort.Direction.ASC), 내림차순(Sort.Direction.DESC) 으로 정렬한다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Board> list = boardRepository.findByTitle("title", pageable);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        sorts.add(new Sort.Order(Sort.Direction.ASC, "title"));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(sorts));

     */

    public void createQueryTest() {
        EntityManager em = emf.createEntityManager();

        String query = "SELECT b FROM Board b";
        TypedQuery<Board> typedQuery = em.createQuery(query, Board.class);
        List<Board> list = typedQuery.getResultList();
    }

    public void queryAnnotationTest() {
        List<Board> list = boardRepository.findAllBoard();
        List<Board> list2 = boardRepository.findByTitleAndWriter2("title", "writer");
        List<Board> list3 = boardRepository.findByTitleAndWriter3("title", "writer");
        List<Board> list4 = boardRepository.findAllBoardBySQL();
        List<Object[]> list5 = boardRepository.findAllBoardBySQL2();
    }

    /*
    [ QueryDSL 사용 방법 ]

    Q Type : QueryDSL 을 사용하기 위해 Q Type 을 생성해야 한다. (QueryDSL 의 Query Type)
             Entity 를 기반으로 자동 생성되는 클래스이다.
    JPQLQuery : JPQL 쿼리를 위한 인터페이스
    JPAQuery : JPQLQuery 를 상속받은 인터페이스 구현체. 직접 생성하거나 JPAQueryFactory 를 통해 생성한다.

        QBoard qBoard = QBoard.board;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);     // 1. EntityManager 를 사용하여 QueryFactory 생성

        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                                .where(board.title.eq("title1"));   // 2. QueryDSL 을 사용하여 쿼리 생성

        List<Board> list = query.fetch();                           // 3. JPAQuery 를 실행해서 조회 결과 얻기

        e.g.
        // Board 의 모든 필드를 가져올 때는 JPAQuery<Board>, 일부만 가져올 때는 JPAQuery<Tuple>, 하나는 JPAQuery<T> 에 타입을 지정하여 사용한다.
        JPAQuery<Tuple> query = queryFactory.select(qBoard.writer, qBoard.viewCnt.sum())
                                .from(qBoard)
                                .where(qBoard.writer.eq("writer1").or(qBoard.title.notLike("title1%")))
                                .where(qBoard.content.contains("content"));
                                .where(qBoard.content.isNotNull())
                                .groupBy(qBoard.writer)
                                .having(qBoard.viewCnt.sum().gt(1000))
                                .orderBy(qBoard.writer.asc())
                                .orderBy(qBoard.viewCnt.sum().desc());
        List<Tuple> list = query.fetch();

    BooleanBuilder : 동적 쿼리를 작성할 때 사용한다. (null 체크, 조건 추가)
        e.g.
        String searchBy = "TC"; // T: Titile, C: Content, TC: Title or Content
        String keyword ="aaa";
        keyword = "%" + keyword + "%";
        // ...
        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건에 따라 동적으로 쿼리가 달라지게 한다.
        if (searchBy.equalsIgnoreCase("T")) {
            builder.and(qBoard.title.like(keyword));
        } else if (searchBy.equalsIgnoreCase("C")) {
            builder.and(qBoard.content.like(keyword));
        } else if (searchBy.equalsIgnoreCase("TC")) {
            builder.and(qBoard.title.like(keyword).or(qBoard.content.like(keyword)));
        }

        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                                .where(builder)
                                .orderBy(qBoard.updatedAt.desc());
     */

    public void queryDSLTest() {
        EntityManager em = emf.createEntityManager();
        QBoard qBoard = QBoard.board;

        // 1. JPAQueryFactory 를 사용하여 QueryFactory 생성
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // 2. QueryDSL 을 사용하여 쿼리 생성
        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                .where(qBoard.title.eq("title1"));

        // 3. JPAQuery 를 실행해서 조회 결과 얻기
        List<Board> list = query.fetch();
        list.forEach(System.out::println);
    }

    public void queryDSLTest2() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(emf.createEntityManager());

        JPAQuery<Tuple> query = queryFactory.select(QBoard.board.writer, QBoard.board.viewCount.sum()).from(QBoard.board)
                .where(QBoard.board.title.notLike("title1%"))
                .where(QBoard.board.writer.eq("writer1"))
                .where(QBoard.board.content.contains("content"))
                .where(QBoard.board.content.isNotNull())
                .groupBy(QBoard.board.writer)
                .having(QBoard.board.viewCount.sum().gt(100))
                .orderBy(QBoard.board.writer.asc())
                .orderBy(QBoard.board.viewCount.sum().desc());

        List<Tuple> list = query.fetch();
        list.forEach(System.out::println);
    }

    public void queryDSLTest3_dynamicQuery() {
        QBoard qBoard = QBoard.board;
        JPAQueryFactory queryFactory = new JPAQueryFactory(emf.createEntityManager());

        String searchBy = "TC";    // 제목과 작성내용에서 검색
        String keyword = "aaa";
        keyword = "%" + keyword + "%";

        BooleanBuilder builder = new BooleanBuilder();

        if (searchBy.equalsIgnoreCase("T")) {
            builder.and(qBoard.title.like(keyword));
        } else if (searchBy.equalsIgnoreCase("C")) {
            builder.and(qBoard.content.like(keyword));
        } else if (searchBy.equalsIgnoreCase("TC")) {
            builder.and(qBoard.title.like(keyword).or(qBoard.content.like(keyword)));
        }

        JPAQuery<Board> query = queryFactory.selectFrom(qBoard)
                .where(builder)
                .orderBy(qBoard.updatedAt.desc());

        List<Board> list = query.fetch();
        list.forEach(System.out::println);
    }

    /*
    [ 연관관계 맵핑하기 ]

    1. 연관 관계의 종류
       - 관계수 (cardinality) : 두 엔티티 간의 대응되는 행의 개수
       - 관계수의 종류 : 1:1, 1:N, N:1, N:M
       - 관계의 방향 : 관계형 모델(양방향), 객체 모델(단방향)

    2. 일대일 관계
       - @OneToOne : 일대일 관계를 맵핑할 때 사용한다.
       - @JoinColumn : 외래 키를 매핑할 때 사용한다.
       - @OneToOne(mappedBy = "user") : 양방향 맵핑을 위해 사용한다. (mappedBy 속성은 연관관계의 주인이 아닌 쪽에서 사용한다.)
                                        상호 참조를 주의해야 한다. (무한 루프에 빠질 수 있다.)

    3. 다대일 관계
       - @ManyToOne : 다대일 관계를 맵핑할 때 사용한다.
     */

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public void OneToOneTest() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@test.com");
        user.setPassword("test1111");
        userRepository.save(user);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cartRepository.save(cart);

        cart = cartRepository.findById(cart.getId()).orElse(null);
        System.out.println("cart = " + cart);

        user = userRepository.findById(user.getId()).orElse(null);
        System.out.println("user = " + user);
    }

}
