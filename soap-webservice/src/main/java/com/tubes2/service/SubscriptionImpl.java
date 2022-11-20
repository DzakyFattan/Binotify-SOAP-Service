package com.tubes2.service;

import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.annotation.Resource;
import javax.xml.ws.handler.MessageContext;
import com.sun.net.httpserver.HttpExchange;

@WebService(endpointInterface = "com.tubes2.service.Subscription")
public class SubscriptionImpl implements Subscription {
    @Resource
    WebServiceContext wsContext;
    public void logger(String description) {
        // GET CLIENT REQUEST IP
        try {
            Connection db = new DBConn().getConnection();
            MessageContext msgx = wsContext.getMessageContext();
            HttpExchange httpx = (HttpExchange) msgx.get("com.sun.xml.ws.http.exchange");
            String ip = httpx.getRemoteAddress().getAddress().getHostAddress();
            String endpoint = httpx.getRequestURI().toString();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Statement statement = db.createStatement();
            String stat = "INSERT INTO logging (ip, endpoint, description, requested_at) VALUES ('" + ip + "', '" + endpoint + "', '" + description + "', '" + timestamp + "')";
            statement.executeUpdate(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public MsgWrapper subscribe(String creator_id_str, String subscriber_id_str, String status) {
        try {
            Connection db = new DBConn().getConnection();
            logger("subscribe " + creator_id_str  + " " + subscriber_id_str + " " + status);
            int creator_id = Integer.parseInt(creator_id_str);
            int subscriber_id = Integer.parseInt(subscriber_id_str);
            Statement statement = db.createStatement();
            String stat = "INSERT INTO subscription (creator_id, subscriber_id, status) VALUES (" + creator_id + ", " + subscriber_id + ", '" + status + "')";
            return new MsgWrapper(200, "Operation Succeed, " + statement.executeUpdate(stat) + " row(s) affected");
        } catch (Exception e) {
            return new MsgWrapper(500, e.getMessage());
        }
    }
    public MsgWrapper updateSub(String creator_id_str, String subscriber_id_str, String status) {
        try {
            Connection db = new DBConn().getConnection();
            logger("updateSub " + creator_id_str  + " " + subscriber_id_str + " " + status);
            int creator_id = Integer.parseInt(creator_id_str);
            int subscriber_id = Integer.parseInt(subscriber_id_str);
            Statement statement = db.createStatement();
            String stat = "UPDATE subscription SET status = '" + status + "' WHERE creator_id = " + creator_id + " AND subscriber_id = " + subscriber_id;
            return new MsgWrapper(200, "Operation Succeed, " + statement.executeUpdate(stat) + " row(s) affected");
        } catch (Exception e) {
            return new MsgWrapper(500, e.getMessage());
        }
    }
    public MsgWrapper getSub(String creator_id_str, String subscriber_id_str, String status) {
        // ASUMSI, KALO EMPTY STRING REQUEST BERARTI ALL
        String stat;
        try {
            Connection db = new DBConn().getConnection();
            logger("getSub " + creator_id_str  + " " + subscriber_id_str + " " + status);
            if (creator_id_str.equals("") && subscriber_id_str.equals("") && status.equals("")) {
                stat = "SELECT * FROM subscription";
            } else {
                stat = "SELECT * FROM subscription WHERE ";
                if (!creator_id_str.equals("")) {
                    stat += "creator_id = " + creator_id_str;
                }
                if (!subscriber_id_str.equals("")) {
                    if (!creator_id_str.equals("")) {
                        stat += " AND ";
                    }
                    stat += "subscriber_id = " + subscriber_id_str;
                }
                if (!status.equals("")) {
                    if (!creator_id_str.equals("") || !subscriber_id_str.equals("")) {
                        stat += " AND ";
                    }
                    stat += "status = '" + status + "'";
                }
            }
            Statement statement = db.createStatement();
            ResultSet res = statement.executeQuery(stat);
            MsgWrapper ret = new MsgWrapper(200, "Operation Succeed");
            for (int i = 0; res.next(); i++) {
                String oneline = i + "," + res.getString("creator_id") + "," + res.getString("subscriber_id") + "," + res.getString("status");
                ret.addContent(oneline);
            }
            return ret;
        } catch (Exception e) {
            return new MsgWrapper(500, e.getMessage());
        }
    }
}
