package controllers;

import javax.inject.Inject;

import play.mvc.Controller;
import service.MerchantService;

public class MerchantController extends Controller {

    private MerchantService merchantService;

    @Inject
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

}

