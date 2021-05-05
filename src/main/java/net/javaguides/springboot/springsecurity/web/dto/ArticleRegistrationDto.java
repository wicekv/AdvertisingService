package net.javaguides.springboot.springsecurity.web.dto;

import javax.validation.constraints.NotEmpty;
import java.beans.Transient;

public class ArticleRegistrationDto {

    @NotEmpty
    private Long articleId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String placeOfTheObject;
    @NotEmpty
    private String description;
    @NotEmpty
    private String price;
    @NotEmpty
    private String photo;



    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long id) {
        this.articleId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceOfTheObject() {
        return placeOfTheObject;
    }

    public void setPlaceOfTheObject(String placeOfTheObject) {
        this.placeOfTheObject = placeOfTheObject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
