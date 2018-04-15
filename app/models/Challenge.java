package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Challenge extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Post challenger;
    @ManyToOne
    private Post challengee;
    private long countChallenger;
    private long countChallengee;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date date;

    public Challenge(Post challenger, Post challengee){
        this.challengee = challengee;
        this.challenger = challenger;
        this.date = new Date();
    }

    public static final Finder<Long, Challenge> find = new Finder<>(Challenge.class);

    public long getId() {
        return id;
    }

    public Post getChallenger() {
        return challenger;
    }

    public void setChallenger(Post challenger) {
        this.challenger = challenger;
    }

    public Post getChallengee() {
        return challengee;
    }

    public void setChallengee(Post challengee) {
        this.challengee = challengee;
    }

    public long getCountChallenger() {
        return countChallenger;
    }

    public void setCountChallenger(long countChallenger) {
        this.countChallenger = countChallenger;
    }

    public long getCountChallengee() {
        return countChallengee;
    }

    public void setCountChallengee(long countChallengee) {
        this.countChallengee = countChallengee;
    }

    public void addCountChallengee(){
        this.countChallengee++;
        this.save();
    }

    public void addCountChallenger(){
        this.countChallenger++;
        this.save();
    }

    public Date getDate() {
        return date;
    }
}
