package net.javaguides.springboot.springsecurity.service;

import net.javaguides.springboot.springsecurity.model.Article;
import net.javaguides.springboot.springsecurity.model.Photos;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.repository.ArticleRepository;
import net.javaguides.springboot.springsecurity.web.dto.ArticleRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserService userService;

    public Article save(ArticleRegistrationDto registration) {
        Article article = new Article();
        article.setName(registration.getName());
        article.setPlaceOfTheObject(registration.getPlaceOfTheObject());
        article.setDescription(registration.getDescription());
        article.setPrice(registration.getPrice());
        article.setAvailable(true);
        article.setUser_owner(userService.loadCurrentUser());
        article.setPhoto(registration.getPhoto());


        return articleRepository.save(article);
    }

    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }

    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public Page<Article> listAll(String keyword, String searchKeyword, int pageNum) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);


        if (keyword != null) {
            if (searchKeyword.equals("Nazwa")) {
                return articleRepository.searchName(keyword, pageable);
            } else if (searchKeyword.equals("Lokalizacja")) {
                return articleRepository.searchPlace(keyword, pageable);
            } else if (searchKeyword.equals("Cena")) {
                return articleRepository.searchPrice(keyword, pageable);
            } else {
                return articleRepository.findAllAvailable(pageable);
            }
        }

        return articleRepository.findAllAvailable(pageable);
    }

    public Page<Article> listAllMy(String keyword, String searchKeyword, int pageNum, Long myUserId) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        if (keyword != null) {
            if(searchKeyword.equals("Nazwa") )
            {
                return articleRepository.searchNameMy(keyword,myUserId, pageable);
            }
            else if (searchKeyword.equals("Lokalizacja"))
            {
                return articleRepository.searchPlaceMy(keyword,myUserId, pageable);
            }
            else if( searchKeyword.equals("Cena"))
            {
                return articleRepository.searchPriceMy(keyword,myUserId, pageable);
            }
            else
            {
                return articleRepository.findAllAvailableMy(myUserId,pageable);
            }
        }


        return articleRepository.findAllAvailableMy(myUserId, pageable );
    }

    @Transactional
    public void deleteArticle(Long id ) {

//        articleRepository.deleteUsersByFirstName(id);
        articleRepository.deleteArticleByArticleId(id);
    }


}
