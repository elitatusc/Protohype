package com.example;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostgresDatabaseImpl implements DatabaseImpl{
    private ResultSet outcome = null;

    //JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL = Credentials.URL;

    @Override
    public ResultSet attendRequest() throws Exception {

        this.outcome = null;

        String sql = "SELECT\n" +
                "  r.recipe_name,\n" +
                "  r.prep_time,\n" +
                "  r.cook_time,\n" +
                "  r.difficulty_level\n" +
                "FROM \"Recipes\" r\n" +
                "JOIN \"Recipe_Ingredients\" ri ON r.recipe_id = ri.recipe_id\n" +
                "LEFT JOIN \"Fridge_items\" fi ON ri.ingredient_id = fi.ingredient_id\n" +
                "LEFT JOIN \"Ingredients\" i ON ri.ingredient_id = i.ingredient_id\n" +
                "GROUP BY r.recipe_id, r.recipe_name, r.prep_time, r.cook_time, r.difficulty_level\n" +
                "ORDER BY (SUM(CASE\n" +
                "    WHEN fi.quantity_available >= ri.quantity_needed THEN 1\n" +
                "    WHEN ri.quantity_needed IS NULL THEN 0\n" +
                "    ELSE 0\n" +
                "  END) * 100.0 / COUNT(ri.ingredient_id))  DESC, r.recipe_name ASC\n" +
                "LIMIT 5;\n";


        //Connect to the database
        //TO BE COMPLETED

        //Class.forName("org.postgresql.Driver");
        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        //Make the query
        //TO BE COMPLETED
        PreparedStatement pstmt = con.prepareStatement(sql);

        //edit this
//            pstmt.setString(1, this.requestStr[0]); //surname
//            pstmt.setString(2, this.requestStr[1]); //city

        //this gets sent to the client to be printed on the UI

        //don't actually need this as the SQL statement never changes
//            pstmt.setString(1, this.requestStr[0]); //recipe name
//            pstmt.setString(2, this.requestStr[1]); //prep time
//            pstmt.setString(2, this.requestStr[2]); //cook time
//            pstmt.setString(2, this.requestStr[3]); //difficulty


        ResultSet rs = pstmt.executeQuery();

        //Process query
        //TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.


        RowSetFactory aFactory = RowSetProvider.newFactory();
        CachedRowSet crs = aFactory.createCachedRowSet();
        crs.populate(rs);  //need to reset the iterator of rs??
        this.outcome = crs; //now populated

        rs.close();
        pstmt.close();
        con.close();



        return this.outcome;
    }
}
