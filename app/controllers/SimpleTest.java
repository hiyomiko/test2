package controllers;

import models.FormData;
import play.Logger;
import play.data.binding.Binder;
import play.mvc.Controller;

public class SimpleTest extends Controller {

    public static void input() {
      render();
    }
     public static void submit() {
       Logger.info("Request method: " + request.method);
       Logger.info("Params: " + params.all());
        FormData formData = new FormData();
        Binder.bind(formData, null, params.all());

         System.out.println("Name: " + formData.name);
         System.out.println("Email: " + formData.email);
         System.out.println("Address: " + formData.address);
        renderText("OK");
     }
  }