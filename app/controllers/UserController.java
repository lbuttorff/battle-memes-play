package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.profile;
import views.html.personalProfile;
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
        if(!json.containsKey("username") || !json.containsKey("email") || !json.containsKey("password") || !json.containsKey("confPassword")){
            return ok(signup.render("Please fill out all fields!"));
        }
        String username = json.get("username")[0];
        String email = json.get("email")[0];
        String pass = json.get("password")[0];
        String conf = json.get("confPassword")[0];
        if(!pass.equals(conf)){
            return ok(signup.render("Your passwords did not match!"));
        }
        if(User.findByEmail(email) != null){
            return ok(signup.render("A User with that email already exists."));
        }
        if(User.findByUsername(username) != null){
            return ok(signup.render("A User with that username already exists."));
        }
        User user = new User(username, email, pass);
        //response().setCookie(Http.Cookie.builder("token", user.createToken()).build());
        session().put("token", user.createToken());
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
        //response().setCookie(Http.Cookie.builder("token", user.createToken()).build());
        session().put("token", user.createToken());
        return redirect(controllers.routes.FeedController.getGlobalFeed());
    }

    public Result logout(){
        User user = User.getCurrentUser();
        if(user != null){
            user.setUuid("");
            user.save();
        }
        session().clear();
        return redirect(controllers.routes.Application.index());
    }

    public Result getProfile(){
        User user = User.getCurrentUser();
        if(user == null){
            return redirect(controllers.routes.UserController.getSignIn());
        }
        return ok(profile.render(user, user));
    }

    public Result getOtherProfile(long id){
        User user = User.find.byId(id);
        User user1 = User.getCurrentUser();
        if(user == null){
            return redirect(controllers.routes.FeedController.getGlobalFeed());
        }
        return ok(profile.render(user1, user));
    }
}
