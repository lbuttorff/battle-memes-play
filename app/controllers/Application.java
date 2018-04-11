package controllers;

import models.Post;
import models.User;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(main.render(null));
    }

    public Result clearSession(){
        Http.Context.current().session().clear();
        return ok(main.render(null));
    }

    public Result generateUsersAndPosts(){
        for(int i=0;i<5;i++){
            User u = new User("user"+i,"u@u."+i,"pass"+i);
            u.save();
            for(int j=0;j<3;j++){
                Post p = new Post("hi"+i+j,u);
                p.save();
            }
        }
        return redirect("/global");
    }
}
