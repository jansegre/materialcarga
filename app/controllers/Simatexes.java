package controllers;

import models.Simatex;
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
@CRUD.For(Simatex.class)
public class Simatexes extends CRUD {
}