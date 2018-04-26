package models;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.DbJson;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Challenge extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Post challenger;
    @ManyToOne
    private Post challengee;
    @DbJson
    private List<Long> countChallenger;
    @DbJson
    private List<Long> countChallengee;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date date;

    public Challenge(Post challenger, Post challengee){
        this.challengee = challengee;
        this.challenger = challenger;
        this.countChallenger = new ArrayList<>();
        this.countChallengee = new ArrayList<>();
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
        return countChallenger.size();
    }

    public long getCountChallengee() {
        return countChallengee.size();
    }

    public void addCountChallengee(long id){
        if(!hasVoted(id)){
            this.countChallengee.add(id);
            this.save();
        };
    }

    public void addCountChallenger(long id){
        if(!hasVoted(id)){
            this.countChallenger.add(id);
            this.save();
        };
    }

    private boolean hasVoted(long id){
        if(this.countChallengee.contains(id) || this.countChallenger.contains(id)){
            return true;
        }else{
            return false;
        }
    }

    public Date getDate() {
        return date;
    }
}
