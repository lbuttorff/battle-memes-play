package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Post extends Model {
    @Id
    private long id;
    private String post;
    @ManyToOne
    private User user;
}
