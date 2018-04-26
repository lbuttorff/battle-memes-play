package controllers;

import controllers.routes;
import models.Challenge;
import models.Post;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.newChallenge;
import views.html.newPost;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class PostController extends Controller {
    private final FormFactory formFactory;

    @Inject
    public PostController(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    @AddCSRFToken
    public Result getNewPost(){
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        return ok(newPost.render(u));
    }

    @RequireCSRFCheck
    public Result newPost() throws IOException {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        //System.out.println(requestData.toString());
        String text = requestData.get("text");
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        Post p = new Post(text, u);
        p.save();
        u.save();
        if (picture != null && picture.getContentType().contains("image/")) {
            String imageName = fileUpload(picture, p.getId());
            p.setImage(imageName);
            p.save();
        }
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
    public Result newChallenge(long id) throws IOException {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        User u = User.getCurrentUser();
        if(u == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        String text = requestData.get("text");
        Post p = new Post(text, u);
        p.save();
        u.save();
        if (picture != null && picture.getContentType().contains("image/")) {
            String imageName = fileUpload(picture, p.getId());
            p.setImage(imageName);
            p.save();
        }
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

    private String fileUpload(Http.MultipartFormData.FilePart<File> picture, long postId) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            File file = picture.getFile();
            String contentType = picture.getContentType();
            System.out.println("Attempting to move file to: "+System.getProperty("user.dir")+"/public/images/posts/"+
                    postId+"."+ contentType.substring(contentType.indexOf("/")+1)
            );
            File newFile = new File(System.getProperty("user.dir")+"/public/images/posts/"+postId+"."+
                    contentType.substring(contentType.indexOf("/")+1));
            if(!newFile.exists()){
                newFile.createNewFile();
            }
            //System.out.println("Content type = "+contentType.substring(contentType.indexOf("/")+1));
            in = new FileInputStream(file);
            //enter the file location in server
            out = new FileOutputStream(newFile);

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            return postId+"."+contentType.substring(contentType.indexOf("/")+1);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
