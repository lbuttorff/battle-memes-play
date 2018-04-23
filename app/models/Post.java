package models;

import io.ebean.*;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post extends Model {
    @Id
    private long id;
    @Column(length = 500)
    private String text;
    @ManyToOne
    private User user;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date postDate;
    @OneToMany(mappedBy = "challengee")
    private List<Challenge> challengeList;
    private String image;

    public Post(String text, User user){
        this.text = text;
        this.user = user;
        this.postDate = new Date();
    }

    public static final Finder<Long, Post> find = new Finder<>(Post.class);

    public long getId() {
        return id;
    }

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

    public List<Challenge> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public void addChallenge(Challenge challenge){
        this.challengeList.add(challenge);
        this.save();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
