package net.javaguides.springboot.springsecurity.model;


import javax.persistence.*;
import java.beans.Transient;
import java.util.List;

@Entity
@Table(name="Article")
public class Article {

    @OneToMany(mappedBy = "article_id", cascade = {
            CascadeType.ALL
    })
    private List<Photos> photos;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "articleId")
    private Long articleId;

    private String name;
    private String placeOfTheObject;
    private String description;
    private String price;
    private Boolean available;
    private String photo;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY )
    @JoinColumn(name = "user_owner")
    private User user_owner;

    /*@OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "article",
            optional = false)
    private Transaction transaction;*/

    public Article(){
    }

    public Article(Long articleId, String name, String placeOfTheObject, String description, String price, Boolean available, String photo) {
        this.articleId = articleId;
        this.name = name;
        this.placeOfTheObject = placeOfTheObject;
        this.description = description;
        this.price = price;
        this.available = available;
        this.photo = photo;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || articleId == null) return null;

        return "/article-photos/" + articleId + "/" + photo;
    }

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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /*public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }*/

    public User getUser_owner() {
        return user_owner;
    }

    public void setUser_owner(User user_owner) {
        this.user_owner = user_owner;
    }

    public long getUser_owner_id() {
        return user_owner.getUserId();
    }

    @Override
    public String toString(){
        return "Article [id= "+ articleId + " name= " + name + " placeOfTheObject= " + placeOfTheObject + " description= " + description + " price= " + price + " available= " + available + "photo= " + photo + " ]";
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }
}
