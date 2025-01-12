package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Secure extends Controller {
    @Before
    static void checkAuthentication() {
        // 認証チェックのロジックをここに実装
        // 認証されていない場合は、ログインページにリダイレクトするなど
    }
}
