package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Categoria extends Model {
	@Required
	public String nome;

	public String toString() {
		return nome;
	}
}