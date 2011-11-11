package models;

import javax.persistence.*;
import java.util.*;

import play.Play;
import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Codec;

@Entity
@Table(name="usuario")
public class User extends Model {

	@Required
	public String login;
	
	@Email
	public String email;

	@Required
	@Password
	public String passwordHash;
	
	@Required
	public Boolean changePassword;
	
	public void setPasswordHash(String password) {
		if(this.passwordHash == null || !this.passwordHash.equals(password)) {
			this.passwordHash = hash(password);
		}
	}

	@ManyToOne
	public Secao secao;

	public String posto;

	@Required
	public String name;

	public enum Profile {
		ADMIN, USER;
	}
	public Profile profile;

	public User(String login, String password, String name) {
		this.login = login;
		this.passwordHash = password;
		this.name = name;
		this.profile = Profile.USER;
		this.changePassword = false;
	}
	
	public boolean checkPassword(String password) {
        return passwordHash.equals(hash(password));
    }
	
	public boolean checkProfile(String profile) {
		if(profile.equals("admin")) {
			return isAdmin();
		} else if(profile.equals("user")) {
			return isUser();
		}
		return false;
	}
	
	public boolean isAdmin() {
		return this.profile.equals(Profile.ADMIN);
	}
	
	public boolean isUser() {
		return this.profile.equals(Profile.USER);
	}

	public static Boolean connect(String login, String password) {
		User user = findByLogin(login);
		return user != null && user.checkPassword(password);
	}
	
	public static User findByLogin(String login) {
		return find("login", login).first();
	}
	
	public static User findByName(String name) {
		return find("name", name).first();
	}

	// Função para nunca armazenar senhas em claro
	static String hash(String senha) {
		return Codec.hexMD5(senha + Play.secretKey);
	}

	public String toString() {
		return login;
	}

}
