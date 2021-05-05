package net.javaguides.springboot.springsecurity.repository;

import net.javaguides.springboot.springsecurity.model.Article;
import net.javaguides.springboot.springsecurity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.available = 1")
    public Page<Article> findAllAvailable(Pageable pageable);


    @Query("SELECT p FROM Article p WHERE p.name LIKE %?1%"
            + " AND p.available = 1 ")
    public Page<Article> searchName(String keyword, Pageable pageable);

    @Query("SELECT p FROM Article p WHERE p.placeOfTheObject LIKE %?1%"
            + " AND p.available = 1 ")
    public Page<Article> searchPlace(String keyword, Pageable pageable);

    @Query("SELECT p FROM Article p WHERE CONCAT(p.price, '') LIKE %?1%"
            + " AND p.available = 1 ")
    public Page<Article> searchPrice(String keyword, Pageable pageable);



    @Query("select a from Article a inner join User u On a.user_owner=u.userId Where u.userId=?1")
    public Page<Article> findAllAvailableMy(Long myUserId, Pageable pageable );


    @Query("select a from Article a inner join User u On a.user_owner=u.userId WHERE a.name LIKE %?1%"
            + " AND u.userId=?2 ")
    public Page<Article> searchNameMy(String keyword,Long myUserId, Pageable pageable);

    @Query("select a from Article a inner join User u On a.user_owner=u.userId WHERE a.placeOfTheObject LIKE %?1%"
            + " AND u.userId=?2 ")
    public Page<Article> searchPlaceMy(String keyword,Long myUserId, Pageable pageable);

    @Query("select a from Article a inner join User u On a.user_owner=u.userId WHERE CONCAT(a.price, '') LIKE %?1%"
            + " AND u.userId=?2 ")
    public Page<Article> searchPriceMy(String keyword,Long myUserId, Pageable pageable);


    @Modifying
    @Query("delete from Article  where articleId = ?1")
    void deleteUsersByFirstName(Long id);

    Long deleteArticleByArticleId(Long id);


}
