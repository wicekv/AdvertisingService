package net.javaguides.springboot.springsecurity.model;

import javax.persistence.*;

@Entity
@Table(name="Photos")
public class Photos{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "photoId")
    private Long photoId;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article_id;

    public Photos(Long photoId, String name){
        this.photoId = photoId;
        this.name = name;
    }

    public Photos() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Article getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Article article_id) {
        this.article_id = article_id;
    }

    @Override
    public String toString(){
        return "Article [id= "+ photoId + " name= " + name + " ]";
    }
}
