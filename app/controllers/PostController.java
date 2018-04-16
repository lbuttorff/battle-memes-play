package controllers;

import controllers.routes;
import models.Challenge;
import models.Post;
import models.User;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.newChallenge;
import views.html.newPost;

import java.io.File;
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
        /*Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("file");
        if(picture != null) {
            String name = picture.getFilename();
            File file = picture.getFile();
        }*/
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

    @AddCSRFToken
    public Result getNewChallenge(long id){
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        Post p = Post.find.byId(id);
        return ok(newChallenge.render(u, p));
    }

    @RequireCSRFCheck
    public Result newChallenge(long id){
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        Map<String, String[]> input = request().body().asFormUrlEncoded();
        String text = input.get("text")[0];
        Post p = new Post(text, u);
        p.save();
        u.save();
        Post challengee = Post.find.byId(id);
        if(challengee == null){
            return badRequest("The Post you were looking for does not exist.");
        }
        Challenge c = new Challenge(p, challengee);
        c.save();
        return redirect(controllers.routes.FeedController.getGlobalFeed());
    }

    @RequireCSRFCheck
    public Result challengeVote(long challengeId, long postId){
        if(User.getCurrentUser() == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        Challenge c = Challenge.find.byId(challengeId);
        if(c == null){
            return badRequest("The challenge you are looking for does not exist.");
        }
        Post p = Post.find.byId(postId);
        if(p == null){
            return badRequest("The post you voted for is not available.");
        }
        if(c.getChallengee().getId() == p.getId()){
            c.addCountChallengee();
        }else if(c.getChallenger().getId() == p.getId()){
            c.addCountChallenger();
        }else{
            return badRequest("The post voted for did not match either post on the challenge.");
        }
        c.save();
        return redirect(controllers.routes.FeedController.getGlobalChallengeFeed());
    }
}
