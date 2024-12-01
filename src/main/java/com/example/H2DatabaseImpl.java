package com.example;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class H2DatabaseImpl implements DatabaseImpl{
    private ResultSet outcome = null;
    @Override
    public ResultSet attendRequest() throws Exception{
        this.outcome = null;
        String jdbcURL = "jdbc:h2:mem:receipe";

        String sql = "SELECT\n" +
                "  r.recipe_name,\n" +
                "  r.prep_time,\n" +
                "  r.cook_time,\n" +
                "  r.difficulty_level\n" +
                "FROM recipes r\n" +
                "JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id\n" +
                "LEFT JOIN fridge_items fi ON ri.ingredient_id = fi.ingredient_id\n" +
                "LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id\n" +
                "GROUP BY r.recipe_id, r.recipe_name, r.prep_time, r.cook_time, r.difficulty_level\n" +
                "ORDER BY (SUM(CASE\n" +
                "    WHEN fi.quantity_available >= ri.quantity_needed THEN 1\n" +
                "    WHEN ri.quantity_needed IS NULL THEN 0\n" +
                "    ELSE 0\n" +
                "  END) * 100.0 / COUNT(ri.ingredient_id))  DESC, r.recipe_name ASC\n" +
                "LIMIT 5;\n";

        Connection connection = DriverManager.getConnection(jdbcURL);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        RowSetFactory aFactory = RowSetProvider.newFactory();
        CachedRowSet crs = aFactory.createCachedRowSet();
        crs.populate(rs);  //need to reset the iterator of rs??
        this.outcome = crs; //now populated

        rs.close();
        pstmt.close();
        //connection.close(); can't close the connection as we need to access the in-memory database, and this would destroy it
        return this.outcome;
    }

}
