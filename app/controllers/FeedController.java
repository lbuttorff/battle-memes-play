package controllers;

import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedController extends Controller {
    public Result getGlobalFeed(){
        User u = User.getCurrentUser();
        ArrayList<Post> posts = new ArrayList<>(Post.find.all());
        Collections.reverse(posts);
        return ok(global.render(u, posts));
    }
}
