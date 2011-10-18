package controllers;

import models.Secao;//Nescessário para poder usar a classe Secao
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
@CRUD.For(Secao.class)//Nescessário pois Secoes não é "plural" de Secao
public class Secoes extends CRUD {
}