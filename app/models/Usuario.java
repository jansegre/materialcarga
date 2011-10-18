package models;

import java.util.*;
import java.security.*;

import javax.persistence.*;

import play.Play;
import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Codec;

@Entity
public class Usuario extends Model {

	@Required
	public String nome;

	@Required
	public String senha;

	// Função para nunca armazenar senhas em claro
	static String safe(String senha) {
		return Codec.hexSHA1(Play.secretKey + senha);
	}

	public void setSenha(String senha) {
		this.senha = safe(senha);
	}

	@ManyToOne
	public Secao secao;

	public String posto;

	public String nomeCompleto;

	public boolean isAdmin;

	public Usuario(String nome, String senha) {
		this.nome = nome;
		this.senha = safe(senha);
	}

	public static Usuario connect(String nome, String senha) {
		return find("byNomeAndSenha", nome, safe(senha)).first();
	}

	public String toString() {
		return nome;
	}

}
