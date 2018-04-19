package controllers;

import models.Challenge;
import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.challenge;
import views.html.global;

import java.util.ArrayList;
import java.util.Collections;

public class FeedController extends Controller {
    public Result getGlobalFeed(){
        User u = User.getCurrentUser();
        ArrayList<Post> posts = new ArrayList<>(Post.find.all());
        //Collections.reverse(posts);
        return ok(global.render(u, posts));
    }

    public Result getGlobalChallengeFeed(){
        User u = User.getCurrentUser();
        ArrayList<Challenge> challenges = new ArrayList<>(Challenge.find.all());
        //Collections.reverse(challenges);
        return ok(challenge.render(u, challenges));
    }
}
