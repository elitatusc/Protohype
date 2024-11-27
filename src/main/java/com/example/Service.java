package com.example;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.sql.*;
import javax.sql.rowset.*;

public class Service extends Thread{

    private Socket serviceSocket = null;
    private String[] requestStr = new String[2]; //One slot for artist's name and one for recordshop's name.
    private ResultSet outcome = null;

    //JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL = Credentials.URL;


    //Class constructor
    public Service(Socket aSocket) {

        //TO BE COMPLETED
        serviceSocket = aSocket;
        this.start();

    }


    //Retrieve the request from the socket
    public String[] retrieveRequest() {
        //NEED TO WRITE
        //
        return null;

    }


    //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;

        this.outcome = null;

        String sql = "SELECT r.recipe_name, COUNT (ri.ingredient_id) AS total_ingredients,SUM(CASE" +
                "WHEN fi.quantity_available >= ri.quantity_needed THEN 1" +
                "WHEN fi.quantity_available IS NULL THEN 0 ELSE 0" +
                "END) AS available_ingredients," +
                "(SUM(CASE" +
                "WHEN fi.quantity_available >= ri.quantity_needed THEN 1" +
                "WHEN ri.quantity_needed IS NULL THEN 0" +
                "ELSE 0" +
                "END) * 100.0 / COUNT(ri.ingredient_id)) AS match_score" +
                "FROM \"Recipes\" r" +
                "JOIN \"Recipe_Ingredients\" ri ON r.recipe_id = ri.recipe_id" +
                "LEFT JOIN \"Fridge_items\" fi ON ri.ingredient_id = fi.ingredient_id" +
                "LEFT JOIN \"Ingredients\" i ON ri.ingredient_id = i.ingredient_id" +
                "GROUP BY r.recipe_id, r.recipe_name" +
                "ORDER BY match_score DESC;";

        try {
            //Connet to the database
            //TO BE COMPLETED

            //Class.forName("org.postgresql.Driver");
            //DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //Make the query
            //TO BE COMPLETED
            PreparedStatement pstmt = con.prepareStatement(sql);

            //edit this
//            pstmt.setString(1, this.requestStr[0]); //surname
//            pstmt.setString(2, this.requestStr[1]); //city

            pstmt.setString(1, this.requestStr[0]); //recipe_name
            pstmt.setString(2, this.requestStr[1]); //match score

            ResultSet rs = pstmt.executeQuery();

            //Process query
            //TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.

            RowSetFactory aFactory = RowSetProvider.newFactory();
            CachedRowSet crs = aFactory.createCachedRowSet();
            crs.populate(rs);  //need to reset the iterator of rs??
            this.outcome = crs;

            //Clean up
            //TO BE COMPLETED

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return flagRequestAttended;
    }


    //Wrap and return service outcome
    public void returnServiceOutcome() { //MIGHT NEED TO CHANGED
        try {
            //Return outcome

            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.outcome); //not sure if this.outcome is correct
            outcomeStreamWriter.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket

            this.serviceSocket.close();

        } catch (IOException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run() {
        try {
            System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
                    + "artist->" + this.requestStr[0] + "; recordshop->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        } catch (Exception e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
