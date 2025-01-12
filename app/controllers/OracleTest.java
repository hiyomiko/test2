package controllers;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleTest {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//192.168.0.4:1523/XEPDB1";
        String user = "smartora";
        String password = "test";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("DBへ接続成功！");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}