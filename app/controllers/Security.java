package controllers;

import models.*;

public class Security extends Secure.Security {

	static boolean authentify(String username, String password) {
		return User.connect(username, password);
	}

	static boolean check(String profile) {
		User user = connectedUser();
		return user != null && user.checkProfile(profile);
	}
	
	static User connectedUser() {
		return User.findByLogin(connected());
	}

	static void onDisconnected() {
		Application.index();
	}

	static void onAuthenticated() {
		switch (connectedUser().profile) {
		case ADMIN:
			CRUD.index();
			break;
		case USER:
		default:
			Application.index();
			break;
		}
	}

}
