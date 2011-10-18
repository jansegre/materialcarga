package controllers;

import models.Secao;
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
@CRUD.For(Secao.class)
public class Secoes extends CRUD {
}