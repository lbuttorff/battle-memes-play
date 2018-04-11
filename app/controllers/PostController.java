package controllers;

import models.Post;
import models.User;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.newPost;

import java.util.Map;

public class PostController extends Controller {
    @AddCSRFToken
    public Result getNewPost(){
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        return ok(newPost.render(u));
    }

    @RequireCSRFCheck
    public Result newPost(){
        Map<String, String[]> input = request().body().asFormUrlEncoded();
        String text = input.get("text")[0];
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        Post p = new Post(text, u);
        p.save();
        u.save();
        return redirect(controllers.routes.FeedController.getGlobalFeed());
    }
}
