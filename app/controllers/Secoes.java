package controllers;

import models.Secao;//Nescessário para poder usar a classe Secao
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
@CRUD.For(Secao.class)//Nescessário pois Secoes != Secaos (Secao" + s)
public class Secoes extends CRUD {
}