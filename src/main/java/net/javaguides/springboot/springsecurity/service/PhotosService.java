package net.javaguides.springboot.springsecurity.service;

import net.javaguides.springboot.springsecurity.model.Article;
import net.javaguides.springboot.springsecurity.model.Photos;
import net.javaguides.springboot.springsecurity.repository.ArticleRepository;
import net.javaguides.springboot.springsecurity.repository.PhotosRepository;
import net.javaguides.springboot.springsecurity.web.dto.ArticleRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotosService {

    @Autowired
    private PhotosRepository photosRepository;

    public Photos save(Article article, String name) {

        Photos photo = new Photos();
        photo.setArticle_id(article);
        photo.setName(name);

        return photosRepository.save(photo);
    }

}
