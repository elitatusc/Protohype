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
    private ResultSet secondOutcome = null;


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
            }
            //getting the recipe name into requestStr
            else if (!("".equals(tmp))){
                this.requestStr[0] = tmp;
            } else{
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
            if (this.requestStr[0].equals("generate")){
                this.outcome = database.attendRequest();

            }
            else{
                //need to make this an array too
                this.outcome = attendInstructions(); //is null
                this.secondOutcome = attendIngredients();
            }

            //do i need to put this back in? or is it just so we can check the print statement
//            while(this.outcome.next()) {
//
//                String recipe_name = outcome.getString("recipe_name");
//                String prep_time = outcome.getString("prep_time");
//                String cook_time = outcome.getString("cook_time");
//                String difficulty_level = outcome.getString("difficulty_level");
//
//                System.out.println(recipe_name + " " + prep_time + " " + cook_time + " " +difficulty_level);
//            }
        }catch (Exception e){
            System.out.println(e);
            flagRequestAttended = false;
        }


        return  flagRequestAttended;
    }

    public ResultSet attendInstructions() throws Exception{ //attendInstructions isnt being called?
        this.outcome = null;
        String jdbcURL = "jdbc:h2:mem:receipe";

        String sql1 = "SELECT r.recipe_name,r.prep_time,r.cook_time,r.difficulty_level, r.instructions\n" +
                "FROM recipes r WHERE recipe_name = ?; \n";

//        String sql2 = "SELECT i.ingredient_name, ri.quantity_needed, ri.quantity_unit\n" +
//                "FROM recipe_ingredients ri \n" +
//                "LEFT JOIN recipes r ON ri.recipe_id = r.recipe_id\n" +
//                "LEFT JOIN ingredients i ON i.ingredient_id = ri.ingredient_id\n" +
//                "WHERE r.recipe_id = (\n" +
//                "    SELECT recipe_id \n" +
//                "    FROM recipes\n" +
//                "    WHERE recipe_name = ?\n" +
//                ");\n";

        Connection connection = DriverManager.getConnection(jdbcURL);
        PreparedStatement pstmt = connection.prepareStatement(sql1);
//        PreparedStatement prepSts = connection.prepareStatement(sql2);
        pstmt.setString(1, this.requestStr[0]);
//        prepSts.setString(1, this.requestStr[0]);
        ResultSet rs = pstmt.executeQuery();
//        while(rs.next()){
//            System.out.println(rs.getString(1));
//            System.out.println(rs.getString(2));
//            System.out.println(rs.getString(3));
//            System.out.println(rs.getString(4));
//            System.out.println(rs.getString(5));
//        }

//        ResultSet rs1 = prepSts.executeQuery();
//        System.out.println(" ");
//        while(rs1.next()){
//            System.out.println(rs1.getString(1));
//            System.out.println(rs1.getString(2));
//            System.out.println(rs1.getString(3));
//        }
        RowSetFactory aFactory = RowSetProvider.newFactory();
        CachedRowSet crs = aFactory.createCachedRowSet();
        crs.populate(rs);  //need to reset the iterator of rs??

//        RowSetFactory bFactory = RowSetProvider.newFactory();
//        CachedRowSet crs1 = bFactory.createCachedRowSet();
//        crs1.populate(rs1);

//        while (rs1.next()){
//            crs.moveToInsertRow(); // moves to the next row to insert in
//            ResultSetMetaData currentRsMetaData = rs1.getMetaData(); // gets data about the rs1 resultset, allowing us to get the column count
//            int numberOfColumns = currentRsMetaData.getColumnCount();// gets the column count
//            for (int i = 1; i <= numberOfColumns; i++){
//                crs.updateObject(i, rs1.getObject(i)); //adds it
//            }
//            crs.insertRow(); //inserts it
//            crs.moveToCurrentRow();
//        }

        crs.beforeFirst();
//        crs1.beforeFirst();
        this.outcome = crs; //now populated
//        this.secondOutcome = crs1;

        //System.out.println("my outcome: " + this.outcome);

//        while(rs.next()){
//            System.out.println(rs.getString(1));
//        }


//        while(rs1.next()){
//            System.out.println(rs1.getString(1));
//
//        }

        //int numcol = crs.getMetaData().getColumnCount();
        //while(crs.next()){
//        while(this.outcome.next()){
//            for(int i = 1; i <= numcol; i++){
//                System.out.println("outcome: " + crs.getString(i) + "\t");
//            }
//        }
       // crs.beforeFirst();
        rs.close();
        pstmt.close();
        //connection.close(); can't close the connection as we need to access the in-memory database, and this would destroy it

        return this.outcome;

    }

    public ResultSet attendIngredients() throws Exception { //doesnt print second outcome
        this.secondOutcome = null;
        String jdbcURL = "jdbc:h2:mem:receipe";


        String sql2 = "SELECT i.ingredient_name, ri.quantity_needed, ri.quantity_unit\n" +
                "FROM recipe_ingredients ri \n" +
                "LEFT JOIN recipes r ON ri.recipe_id = r.recipe_id\n" +
                "LEFT JOIN ingredients i ON i.ingredient_id = ri.ingredient_id\n" +
                "WHERE r.recipe_id = (\n" +
                "    SELECT recipe_id \n" +
                "    FROM recipes\n" +
                "    WHERE recipe_name = ?\n" +
                ");\n";

        Connection connection = DriverManager.getConnection(jdbcURL);
        PreparedStatement pstmt = connection.prepareStatement(sql2);
        pstmt.setString(1, this.requestStr[0]);
        ResultSet rs1 = pstmt.executeQuery();

        RowSetFactory aFactory = RowSetProvider.newFactory();
        CachedRowSet crs = aFactory.createCachedRowSet();
        crs.populate(rs1);
        while(rs1.next()){
            System.out.println("pritning second outcome");
            System.out.println(rs1.getString(1));
            System.out.println(rs1.getString(2));
            System.out.println(rs1.getString(3));
        }
        crs.beforeFirst();
        //rs1.beforeFirst();


        this.secondOutcome = crs;
        rs1.close();
        pstmt.close();

        return this.secondOutcome;
    }



    //Wrap and return service outcome
    public void returnServiceOutcome() { //MIGHT NEED TO CHANGED
        try {
            //Return outcome
            while(this.outcome.next()) {

                System.out.println("printing return service outcome: ");
                String recipe_name = outcome.getString("recipe_name");
                String prep_time = outcome.getString("prep_time");
                String cook_time = outcome.getString("cook_time");
                String difficulty_level = outcome.getString("difficulty_level");


                System.out.println(recipe_name + "+" + prep_time + "+" + cook_time + "+" +difficulty_level);
            }
            this.outcome.beforeFirst();
            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.outcome); //not sure if this.outcome is correct
            outcomeStreamWriter.flush();
            //adding in close
            //outcomeStreamWriter.close();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket

            //comment out closing socket - find me
            this.serviceSocket.close();

        } catch (IOException | SQLException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }

    public void returnInstructions(){
        try {
            //find me
            //this.outcome.beforeFirst();
            //Return outcome
            while(this.outcome.next()) {

                System.out.println("pritning return instructions");
                String recipe_name = outcome.getString("recipe_name");
                String prep_time = outcome.getString("prep_time");
                String cook_time = outcome.getString("cook_time");
                String difficulty_level = outcome.getString("difficulty_level");
                String instructions = outcome.getString("instructions");
//                String ingredient_name = outcome.getString("ingredient_name");
//                String quantity_needed = outcome.getString("quantity_needed");
//                String quantity_unit = outcome.getString("quantity_unit");

                System.out.println(recipe_name + " " + prep_time + " " + cook_time + " " +difficulty_level + " " + instructions);

            }
            this.outcome.beforeFirst();
            OutputStream outcomeStream1 = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream1);
            try {
                outcomeStreamWriter.writeObject(this.outcome); //not sure if this.outcome is correct
                outcomeStreamWriter.flush();
            } catch (IOException e){
                System.out.println("failed to write" + e.getMessage());
            }

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket

            //comment out closing socket - find me
            //closing the service socket here would mean that return`ingredients couldnt use the socket?
            //this.serviceSocket.close();

        } catch (IOException | SQLException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }

    public void returnIngredients(){ //isnt printing return ingredients
        try {
            //Return outcome
            while(this.secondOutcome.next()) {
                System.out.println("printing return ingredients");
                String ingredient_name = secondOutcome.getString("ingredients");
                String quantity_needed = secondOutcome.getString("quantity");
                String quantity_unit = secondOutcome.getString("quantityUnit");
                System.out.println(ingredient_name + " " + quantity_needed + " " + quantity_unit);


                //System.out.println(recipe_name + " " + prep_time + " " + cook_time + " " + difficulty_level + " " + instructions + " " + ingredient_name + " " + quantity_needed + " " + quantity_unit);
            }

            this.secondOutcome.beforeFirst();
            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.secondOutcome); //broken pipe here
            outcomeStreamWriter.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.secondOutcome);

            //Terminating connection of the service socket

            this.serviceSocket.close();

        } catch (IOException | SQLException e) {
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
            if (!tmp) {
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            }
            if(this.requestStr[0].equals("generate")){
                this.returnServiceOutcome();
            } else{
                this.returnInstructions();
                this.returnIngredients();
            }


        } catch (Exception e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
