import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Conferir se existe algum usuário
        if(Usuario.count() == 0) {
        	// Se não existir nenhum usuário
        	// adicionamos o admin
        	try {
        		Usuario admin = new Usuario("admin", "admin1234");
        		admin.isAdmin = true;
        		admin.save();
        	} catch(Exception e) {
        	}
        }
    }
 
}