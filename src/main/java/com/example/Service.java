package com.example;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.sql.*;
import java.util.Iterator;
import javax.sql.rowset.*;

public class Service extends Thread {

    private Socket serviceSocket = null;
    private String[] requestStr = new String[1];
    private ResultSet outcome = null;

    //JDBC connection
    //private String USERNAME = Credentials.USERNAME;
    //private String PASSWORD = Credentials.PASSWORD;
   // private String URL = Credentials.URL;

    private DatabaseImpl database = new H2DatabaseImpl();


    //Class constructor
    public Service(Socket aSocket) {
        //TO BE COMPLETED
        serviceSocket = aSocket;
        this.start();

    }


    //Retrieve the request from the socket
    public String[] retrieveRequest() {
        //NEED TO WRITE
        //when the button is pressed, we then attend to the request

        this.requestStr[0] = "";

        String tmp = "";
        try {
            //TO BE COMPLETED
            InputStream socketStream = this.serviceSocket.getInputStream();
            InputStreamReader socketReader = new InputStreamReader(socketStream);
            StringBuffer stringBuffer = new StringBuffer();

            //String receivedLine = socketReader.read();
            char y;
            while(true){
                y = (char) socketReader.read();
                if (y == '#'){
                    break;
                }
                stringBuffer.append(y);
            }
            tmp = stringBuffer.toString();

            if ("generate".equals(tmp)) {
                this.requestStr[0] = tmp;
            } else {
                throw new IOException("Socket did not connect properly to Server.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.requestStr;
    }

        //Parse the request command and execute the query
    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;
        try {
            this.outcome = database.attendRequest();
            while(this.outcome.next()) {

                String recipe_nme = outcome.getString("recipe_name");
                String prep_time = outcome.getString("prep_time");
                String cook_time = outcome.getString("cook_time");
                String difficulty_level = outcome.getString("difficulty_level");

                System.out.println(recipe_nme + " " + prep_time + " " + cook_time + " " +difficulty_level);
            }
        }catch (Exception e){
            System.out.println(e);
            flagRequestAttended = false;
        }


        return  flagRequestAttended;
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
//            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
//                    + "artist->" + this.requestStr[0] + "; recordshop->" + this.requestStr[1]);

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
