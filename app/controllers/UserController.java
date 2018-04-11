package controllers;

import com.google.inject.Inject;
import controllers.routes;
import models.Post;
import models.User;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.global;
import views.html.signin;
import views.html.signup;

import java.util.Map;

public class UserController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public UserController(final FormFactory formFactory){
        this.formFactory = formFactory;
    }

    @AddCSRFToken
    public Result getSignUp(){
        return ok(signup.render(null));
    }

    @RequireCSRFCheck
    public Result signUp(){
        Map<String, String[]> json = request().body().asFormUrlEncoded();
        if(!json.containsKey("username") || !json.containsKey("email") || !json.containsKey("password") || !json.containsKey("confirmPassword")){
            return ok(signup.render("Please fill out all fields!"));
        }
        String username = json.get("username")[0];
        String email = json.get("email")[0];
        String pass = json.get("password")[0];
        String conf = json.get("confirmPassword")[0];
        if(!pass.equals(conf)){
            return ok(signup.render("Your passwords did not match!"));
        }
        User user = new User(username, email, pass);
        session("token",user.createToken());
        return redirect(controllers.routes.FeedController.getGlobalFeed());    }

    @AddCSRFToken
    public Result getSignIn(){
        return ok(signin.render(null));
    }

    @RequireCSRFCheck
    public Result signIn(){
        Map<String, String[]> json = request().body().asFormUrlEncoded();
        if(!json.containsKey("email") || !json.containsKey("password")){
            return ok(signin.render("Please fill out all fields!"));
        }
        String email = json.get("email")[0];
        String pass = json.get("password")[0];
        User user = User.authenticate(email, pass);
        if(user == null){
            return ok(signin.render("An invalid Email or Password was entered."));
        }
        session("token",user.createToken());
        return redirect(controllers.routes.FeedController.getGlobalFeed());
    }
}