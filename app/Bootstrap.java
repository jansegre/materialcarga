import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Check if the database is empty
        if(User.count() == 0) {
            User user = new User("admin", "123456", "Administrador");
            user.profile = User.Profile.ADMIN;
            user.changePassword = true;
            user.save();
            user = new User("anderson", "123456", "Anderson");
            user.profile = User.Profile.ADMIN;
            user.posto = "Maj";
            user.changePassword = true;
            user.save();
			user = new User("persegani", "123456", "Persegani").save();
			user.posto = "Sgt";
			user.changePassword = true;
			user.save();
        }
    }
 
}