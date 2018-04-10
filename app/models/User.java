package models;

import org.mindrot.jbcrypt.BCrypt;
import play.data.format.Formats;
import play.data.validation.Constraints;
import io.ebean.*;
import play.mvc.Http;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static play.mvc.Controller.session;

@Entity
public class User extends Model {
    @Id
    private long id;
    @Constraints.Required
    private String username;
    @Constraints.Required
    private String email;
    @Constraints.Required
    private String passwordHash;
    @Formats.DateTime(pattern="dd/MM/yyyy")
    private Date joinDate;
    @OneToMany
    private List<Post> posts;
    @Constraints.Required
    private String uuid;

    public User(String u, String e, String p){
        this.username = u;
        this.email = e;
        this.passwordHash = BCrypt.hashpw(p, BCrypt.gensalt());
        this.joinDate = new Date();
    }

    public static User authenticate(String email, String password){
        User user = findByEmail(email);
        if(user != null && BCrypt.checkpw(password, user.getPasswordHash())){
            return user;
        }else{
            return null;
        }
    }

    public static User findByEmail(String email){
        return find.query().where().eq("email", email).findUnique();
    }

    //Static finder that will return a user based on the extend methods of Model
    public static final Finder<Long, User> find = new Finder<>(User.class);

    public static User getCurrentUser(){
        String temp = session().get("uuid");
        return find.query().where().eq("uuid", temp).findUnique();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String createToken() {
        this.uuid = UUID.randomUUID().toString();
        this.save();
        return this.uuid;
    }
}
