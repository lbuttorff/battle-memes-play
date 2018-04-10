package models;

import io.ebean.*;
import play.data.format.Formats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Post extends Model {
    @Id
    private long id;
    private String text;
    @ManyToOne
    private User user;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date postDate;

    public Post(String text, User user){
        this.text = text;
        this.user = user;
        this.postDate = new Date();
    }

    public static final Finder<Long, Post> find = new Finder<>(Post.class);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
