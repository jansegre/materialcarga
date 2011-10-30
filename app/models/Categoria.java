package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Categoria extends Model {
	
	@Required
	public String name;
	
	@Required
	public String slug;
	
	public static Categoria findBySlug(String slug) {
		return find("slug", slug).first();
	}

	public String toString() {
		return name;
	}
}