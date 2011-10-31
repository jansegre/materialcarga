package controllers;

import models.*;

public class Security extends Secure.Security {

	static boolean authentify(String username, String password) {
		return User.connect(username, password);
	}

	static boolean check(String profile) {
		if(profile.equals("public")) return true;
		User user = getConnectedUser();
		return user != null && user.checkProfile(profile);
	}
	
	static User getConnectedUser() {
		return User.findByLogin(connected());
	}

	static void onDisconnected() {
		Application.index();
	}

	static void onAuthenticated() {
		switch (getConnectedUser().profile) {
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
