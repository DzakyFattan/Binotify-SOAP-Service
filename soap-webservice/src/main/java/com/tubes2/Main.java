package com.tubes2;
import com.tubes2.service.*;
import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        try {
            Endpoint.publish("http://0.0.0.0:2434/subscription", new SubscriptionImpl());
            System.out.println("Server started at " + "http://0.0.0.0:2434/subscription");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}