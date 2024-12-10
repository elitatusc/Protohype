package com.example;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.sql.*;
import javax.sql.rowset.*;

public class Service extends Thread {

    private Socket serviceSocket = null;
    private String[] requestStr = new String[1];
    private ResultSet outcome = null;
    private ResultSet secondOutcome = null;
    private DatabaseImpl database = new H2DatabaseImpl();


    //Class constructor
    public Service(Socket aSocket) {
        //TO BE COMPLETED
        serviceSocket = aSocket;
        this.start();
    }

    //Retrieve the request from the socket
    public String[] retrieveRequest() {
        this.requestStr[0] = "";

        String tmp = "";
        try {
            InputStream socketStream = this.serviceSocket.getInputStream();
            InputStreamReader socketReader = new InputStreamReader(socketStream);
            StringBuffer stringBuffer = new StringBuffer();

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

    public boolean attendRequest()
    {
        boolean flagRequestAttended = true;
        try {
            if (this.requestStr[0].equals("generate")){
                this.outcome = database.attendRequest();

            }
            else{
                this.outcome = attendInstructions(); //is null
                this.secondOutcome = attendIngredients();
            }
        }catch (Exception e){
            System.out.println(e);
            flagRequestAttended = false;
        }
        return  flagRequestAttended;
    }

    public ResultSet attendInstructions() throws Exception{
        this.outcome = null;
        String jdbcURL = "jdbc:h2:mem:receipe";

        String sql1 = "SELECT r.recipe_name,r.prep_time,r.cook_time,r.difficulty_level, r.instructions\n" +
                "FROM recipes r WHERE recipe_name = ?; \n";

        Connection connection = DriverManager.getConnection(jdbcURL);
        PreparedStatement pstmt = connection.prepareStatement(sql1);
        pstmt.setString(1, this.requestStr[0]);
        ResultSet rs = pstmt.executeQuery();
        RowSetFactory aFactory = RowSetProvider.newFactory();
        CachedRowSet crs = aFactory.createCachedRowSet();
        crs.populate(rs);
        crs.beforeFirst();
        this.outcome = crs;

        rs.close();
        pstmt.close();
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
        crs.beforeFirst();
        this.secondOutcome = crs;
        rs1.close();
        pstmt.close();

        return this.secondOutcome;
    }

    public void returnServiceOutcome() {
        try {
            //Return outcome
            while(this.outcome.next()) {

                String recipe_name = outcome.getString("recipe_name");
                String prep_time = outcome.getString("prep_time");
                String cook_time = outcome.getString("cook_time");
                String difficulty_level = outcome.getString("difficulty_level");

                System.out.println(recipe_name + " " + prep_time + " " + cook_time + " " +difficulty_level);
            }
            this.outcome.beforeFirst();
            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.outcome);
            outcomeStreamWriter.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            this.serviceSocket.close();

        } catch (IOException | SQLException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }

    public void returnInstructions(){
        try {
            while(this.outcome.next()) {

                System.out.println("pritning return instructions");
                String recipe_name = outcome.getString("recipe_name");
                String prep_time = outcome.getString("prep_time");
                String cook_time = outcome.getString("cook_time");
                String difficulty_level = outcome.getString("difficulty_level");
                String instructions = outcome.getString("instructions");

                System.out.println(recipe_name + " " + prep_time + " " + cook_time + " " +difficulty_level + " " + instructions);

            }
            this.outcome.beforeFirst();
            OutputStream outcomeStream1 = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream1);
            try {
                outcomeStreamWriter.writeObject(this.outcome);
                outcomeStreamWriter.flush();
            } catch (IOException e){
                System.out.println("failed to write" + e.getMessage());
            }

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);


        } catch (IOException | SQLException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }

    public void returnIngredients(){
        try {
            //Return outcome
            while(this.secondOutcome.next()) {
                System.out.println("printing return ingredients");
                String ingredient_name = secondOutcome.getString("ingredient_name");
                String quantity_needed = secondOutcome.getString("quantity_needed");
                String quantity_unit = secondOutcome.getString("quantity_unit");
                System.out.println(ingredient_name + " " + quantity_needed + " " + quantity_unit);
            }

            this.secondOutcome.beforeFirst();
            OutputStream outcomeStream = this.serviceSocket.getOutputStream();
            ObjectOutputStream outcomeStreamWriter = new ObjectOutputStream(outcomeStream);
            outcomeStreamWriter.writeObject(this.secondOutcome);
            outcomeStreamWriter.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.secondOutcome);

            this.serviceSocket.close();

        } catch (IOException | SQLException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }

    public void run() {
        try {
            System.out.println("\n============================================\n");

            this.retrieveRequest();

            //Attend the request
            boolean tmp = this.attendRequest();

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
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
