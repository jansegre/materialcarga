package models;

import java.util.*;
import java.security.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Codec;

@Entity
public class Usuario extends Model {

	@Required
	public String nome;

	@Required
	public String senha;

	public void setSenha(String senha) {
		this.senha = Codec.hexSHA1(senha);
	}

	@ManyToOne
	public Secao secao;

	public String posto;

	public String nomeCompleto;

	public boolean isAdmin;

	public Usuario(String nome, String senha) {
		this.nome = nome;
		this.setSenha(senha);
	}

	public static Usuario connect(String nome, String senha) {
		return find("byNomeAndSenha", nome, Codec.hexSHA1(senha)).first();
	}

	public String toString() {
		return nome;
	}

}
