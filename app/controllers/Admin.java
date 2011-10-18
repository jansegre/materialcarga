package controllers;
 
import play.*;
import play.mvc.*;
import play.data.validation.*;
 
import java.util.*;
 
import models.*;
 
@With(Secure.class)
public class Admin extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            //Usuario user = Usuario.find("byName", Security.connected()).first();
            //renderArgs.put("user", user.name);
        }
    }
 
    public static void index() {
        //List<Post> posts = Post.find("author.email", Security.connected()).fetch();
        //render(posts);
    	render();
    }
    
}