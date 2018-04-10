package controllers;

import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.global;

public class FeedController extends Controller {
    public Result getGlobalFeed(){
        User u = User.getCurrentUser();
        return ok(global.render(u, Post.find.all()));
    }
}
