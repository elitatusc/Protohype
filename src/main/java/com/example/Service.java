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
        return null;

    }


    //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;

        this.outcome = null;

        String sql = "SELECT record.title, record.label, record.genre, record.rrp, " +
                "COUNT (recordcopy.recordid) AS copyID FROM record INNER JOIN recordcopy ON " +
                "record.recordid = recordcopy.recordid INNER JOIN artist ON " +
                "record.artistid = artist.artistid INNER JOIN recordshop ON " +
                "recordshop.recordshopid = recordcopy.recordshopid " +
                "WHERE artist.lastname = ? AND recordshop.city = ? " + //do i need to make this so that onloan = f?
                "GROUP BY record.title, record.label, record.genre, record.rrp;"; //TO BE COMPLETED- Update this line as needed.



        try {
            //Connet to the database
            //TO BE COMPLETED

            //Class.forName("org.postgresql.Driver");
            //DriverManager.registerDriver(new org.postgresql.Driver());
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //Make the query
            //TO BE COMPLETED
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, this.requestStr[0]); //surname
            pstmt.setString(2, this.requestStr[1]); //city
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
    public void returnServiceOutcome() {
        try {
            //Return outcome
            //TO BE COMPLETED

            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.outcome); //not sure if this.outcome is correct
            outcomeStreamWriter.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket
            //TO BE COMPLETED

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
