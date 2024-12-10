package com.example;

import java.lang.ClassNotFoundException;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.sql.*;
import java.util.Collections;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class Client extends Application{

    public static Client me; //Get the application instance in javafx
    public static Stage thePrimaryStage;  //Get the application primary scene in javafx

    //public static Stage theSecondaryStage;
    private Socket clientSocket = null;

    private CachedRowSet serviceOutcome = null; //The service outcome

    private CachedRowSet serviceOutcome1 = null;

    private CachedRowSet serviceOutcome2 = null;
    private Label recipeName;
    private TextArea timeInformation;

    private String requestedRecipe = null;



    //Convenient to populate the TableView
    public class RecipeTable {
        private StringProperty recipeName;
        private StringProperty prepTime;
        private StringProperty cookTime;
        private StringProperty totalTime;
        private StringProperty difficulty;
        private StringProperty ingredients;
        private StringProperty instructions;
        private StringProperty quantity;
        private StringProperty quantityUnit;


        public void setRecipeName(String value) { recipeNameProperty().set(value); }
        public void setPrepTime(String value) { prepTimeProperty().set(value); }
        public void setCookTime(String value) { cookTimeProperty().set(value); }
        public void setDifficulty(String value) { difficultyProperty().set(value); }
        public void setIngredients(String value) { ingredientsProperty().set(value); }
        public void setInstructions(String value) { instructionsProperty().set(value); }
        public void setQuantity(String value) { quantityProperty().set(value); }
        public void setQuantityUnit(String value) { quantityUnitProperty().set(value); }
        public void setTotalTime(String value) { totalTimeProperty().set(value); }

        public StringProperty getName() {
            return recipeName;
        }

        public StringProperty getTotalTime(){
            return totalTime;
        }

        public StringProperty getDifficulty(){
            return difficulty;
        }



        public StringProperty recipeNameProperty() {
            if (recipeName == null)
                recipeName = new SimpleStringProperty(this, "");
            return recipeName;
        }
        public StringProperty prepTimeProperty() {
            if (prepTime == null)
                prepTime = new SimpleStringProperty(this, "");
            return prepTime;
        }
        public StringProperty cookTimeProperty() {
            if (cookTime == null)
                cookTime = new SimpleStringProperty(this, "");
            return cookTime;
        }
        public StringProperty totalTimeProperty() {
            if (totalTime == null)
                totalTime = new SimpleStringProperty(this, "");
            return totalTime;
        }
        public StringProperty difficultyProperty() {
            if (difficulty == null)
                difficulty = new SimpleStringProperty(this, "");
            return difficulty;
        }
        public StringProperty ingredientsProperty() {
            if (ingredients == null)
                ingredients = new SimpleStringProperty(this, "");
            return ingredients;
        }
        public StringProperty instructionsProperty() {
            if (instructions == null)
                instructions = new SimpleStringProperty(this, "");
            return instructions;
        }
        public StringProperty quantityProperty() {
            if (quantity == null)
                quantity = new SimpleStringProperty(this, "");
            return quantity;
        }
        public StringProperty quantityUnitProperty() {
            if (quantityUnit == null)
                quantityUnit = new SimpleStringProperty(this, "");
            return quantityUnit;
        }

    }

    public Client(){

        me=this;
    }


    public void initializeSocket(){

        //TO BE COMPLETED
        try {
            clientSocket = new Socket(Credentials.HOST, Credentials.PORT);
            System.out.println("connected correctly");
        }catch(UnknownHostException e){
            System.out.println("Client: Unknown host. " + e);
        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }

    }

    public void requestService() { //when the button is pressed call requestService
        System.out.println("Client: Button pressed. Requesting recipes\n");

        //TO BE COMPLETED
        try {
            //TO BE COMPLETED
            OutputStream requestStream = this.clientSocket.getOutputStream();
            OutputStreamWriter requestStreamWriter = new OutputStreamWriter(requestStream);
            requestStreamWriter.write("generate#");
            requestStreamWriter.flush(); //Service request sent

        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
            System.exit(1);
        }

    }

    //this function sends the recipe name that was clicked on to Service so that it can be put into the SQL statement

    public void requestInstructions() {
        System.out.println("Client: Button pressed. Requesting recipes\n");

        try{
            OutputStream requestStream = this.clientSocket.getOutputStream();
            OutputStreamWriter requestStreamWriter = new OutputStreamWriter(requestStream);
            requestStreamWriter.write( requestedRecipe + "#");
            requestStreamWriter.flush();
        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
            System.exit(1);
        }
    }

    public void reportServiceOutcomeRecipes() {
        try {
            InputStream outcomeStream = this.clientSocket.getInputStream();
            System.out.println("outcomeeeee:" + outcomeStream);
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream);
            ResultSet temporary = (ResultSet) outcomeStreamReader.readObject();
            System.out.println("Debug 1");
            //TableView outputBox = (TableView) thePrimaryStage.getScene().getRoot(); //error is here
            //ObservableList<MyTableRecord> tmpRecords = outputBox.getItems();

            this.serviceOutcome = RowSetProvider.newFactory().createCachedRowSet();
            this.serviceOutcome.populate(temporary);


            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot(); // breaks here - Cannot invoke "javafx.stage.Stage.getScene()" because "com.example.Client.thePrimaryStage" is null
            System.out.println("Debug 2");
            //Getting the border pane from the stage
            //This will allow us to put the results in there
            TableView<RecipeTable> outputTable = (TableView<RecipeTable>) borderPane.getCenter();
            if (outputTable == null) {

                outputTable = new TableView<>();
                borderPane.setCenter(outputTable);
            }
            //Accessing the table that is in the centre of the borderPane
            System.out.println("Debug 3");
            ObservableList<RecipeTable> tmpRecipes = outputTable.getItems();
            tmpRecipes.clear();
            System.out.println("Debug 4");

            if (serviceOutcome == null) {
                System.out.println("No data available.");
            }

            while (this.serviceOutcome.next()) {
                System.out.println("Row Found");
                RecipeTable recipe = new RecipeTable();
                recipe.setRecipeName(serviceOutcome.getString("recipe_name"));
                recipe.setPrepTime(serviceOutcome.getString("prep_time"));
                recipe.setCookTime(serviceOutcome.getString("cook_time"));
                int total = Integer.parseInt(serviceOutcome.getString("prep_time")) + Integer.parseInt(serviceOutcome.getString("cook_time"));
                String total_time = String.valueOf(total);
                recipe.setTotalTime(total_time);
                recipe.setDifficulty(serviceOutcome.getString("difficulty_level"));
                //System.out.println(recipe.getRecipeName() + " | " + recipe.getLabel() + recipe.getGenre() + " | " + recipe.getRrp() + " | " + record.getCopyID());
                //Can do this later as need to do get methods

                tmpRecipes.add(recipe);
            }

            this.serviceOutcome.beforeFirst();
            System.out.println("Debug 5");
            outputTable.setItems(tmpRecipes);
            System.out.println("Debug 6");

            String tmp = " ";
            //System.out.println(tmp +"\n====================================\n");
        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }catch(ClassNotFoundException e){
            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
        }catch(SQLException e){
            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
        }
    }

//    public void reportInstructions(){
//        System.out.println("hello");
//        try {
//            InputStream outcomeStream = this.clientSocket.getInputStream();
//            System.out.println("outcome:" + outcomeStream);
//
//            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream); //this line breaks
//            ResultSet temporary = (ResultSet) outcomeStreamReader.readObject(); //handle this exception
//
////            try {
////                System.out.println("tried");
////                ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream); //this line breaks
////                ResultSet temporary = (ResultSet) outcomeStreamReader.readObject();
////                System.out.println("pass");
////            } catch (IOException e){
////                e.printStackTrace();
////            } catch (Exception e){
////                e.printStackTrace();
////            }
//
//
////            ResultSet temporary = (ResultSet) outcomeStreamReader.readObject();
//
//
//            System.out.println("Debug 1");
//            //TableView outputBox = (TableView) thePrimaryStage.getScene().getRoot(); //error is here
//            //ObservableList<MyTableRecord> tmpRecords = outputBox.getItems();
//
//            this.serviceOutcome = RowSetProvider.newFactory().createCachedRowSet();
//            this.serviceOutcome.populate(temporary);
//
//
//            //this.serviceOutcome.populate(temporary);
//
//
//            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot(); // breaks here - Cannot invoke "javafx.stage.Stage.getScene()" because "com.example.Client.thePrimaryStage" is null
//            System.out.println("Debug 2");
//            //Getting the border pane from the stage
//            //This will allow us to put the results in there
//            TableView<RecipeTable> outputTable = (TableView<RecipeTable>) borderPane.getCenter();
//            if (outputTable == null) {
//
//                outputTable = new TableView<>();
//                borderPane.setCenter(outputTable);
//            }
//            //Accessing the table that is in the centre of the borderPane
//            System.out.println("Debug 3");
//            ObservableList<RecipeTable> tmpRecipes = outputTable.getItems();
//            tmpRecipes.clear();
//            System.out.println("Debug 4");
//
//            if (this.serviceOutcome == null) {
//                System.out.println("No data available.");
//            }
//
//            while (this.serviceOutcome.next()) { //not going in here - find me
//                System.out.println("Row Found");
//                RecipeTable recipe = new RecipeTable();
//                recipe.setRecipeName(serviceOutcome.getString("recipe_name"));
//                System.out.println("re");
//                recipe.setPrepTime(serviceOutcome.getString("prep_time"));
//                System.out.println("pre");
//                recipe.setCookTime(serviceOutcome.getString("cook_time"));
//                System.out.println("co");
//                //int total = Integer.parseInt(serviceOutcome.getString("prep_time")) + Integer.parseInt(serviceOutcome.getString("cook_time"));
//                //String total_time = String.valueOf(total);
//                //recipe.setTotalTime(total_time);
//                recipe.setDifficulty(serviceOutcome.getString("difficulty_level"));
//                System.out.println("di");
//                recipe.setInstructions(serviceOutcome.getString("instructions"));
//                System.out.println("in");
//                //System.out.println(recipe.getRecipeName() + " | " + recipe.getLabel() + recipe.getGenre() + " | " + recipe.getRrp() + " | " + record.getCopyID());
//                //Can do this later as need to do get methods
//
//                tmpRecipes.add(recipe);
//            }
//
//            this.serviceOutcome.beforeFirst();
//            System.out.println("Debug 5");
//            outputTable.setItems(tmpRecipes);
//            System.out.println("Debug 6");
//
//            String tmp = " ";
//            //System.out.println(tmp +"\n====================================\n");
//        }catch(IOException e){
//            System.out.println("Client: I/O error. " + e);
//        }
////        catch(ClassNotFoundException e){
////            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
////        }
//        catch(SQLException e){ //goes into here
//            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
//        }
//        catch(ClassNotFoundException e){
//            System.out.println("Class not found. " + e);
//        }
//    }



    public void reportServiceOutcomeInstructions() {
        System.out.println("oo");
        try {
            System.out.println("h");
            //BorderPane borderPane = (BorderPane) primaryStageVariable.getScene().getRoot();
            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
            System.out.println("h2");
            InputStream outcomeStream = clientSocket.getInputStream();
            System.out.println("h3");
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream); //this line is the error
            System.out.println("h4");
            ResultSet temp = (ResultSet) outcomeStreamReader.readObject();
            this.serviceOutcome1= RowSetProvider.newFactory().createCachedRowSet();
            this.serviceOutcome1.populate(temp);
           // serviceOutcome = (CachedRowSet) outcomeStreamReader.readObject();
            System.out.println("h5");
            //serviceOutcome1.next();
            System.out.println("hey");


            //This is the recipe name, which we are setting as a label
            //HBox topPane = (HBox) borderPane.getTop();
            BorderPane topPane = (BorderPane) borderPane.getTop();
            System.out.println("h6");
            recipeName = (Label) topPane.getCenter();
            System.out.println("h7");
            while (this.serviceOutcome1.next()) {
                recipeName.setText(serviceOutcome1.getString("recipe_name"));
                System.out.println("h8");
                //serviceOutcome.next();
                //We have to set the service outcome at next after every time we read a line

                System.out.println("h9");

                //Now we will put the information about the recipe times in the text area
                StringBuilder timesBuilder = new StringBuilder();
                System.out.println("After string builder");
                //Iterating through to get the different lines from the input

                //invalid cursor position
                String prepTime = serviceOutcome1.getString("prep_time");

                System.out.println("h10");

                timesBuilder.append("Prep Time: ").append(prepTime).append(" minutes\n");

                System.out.println("h11");

                //serviceOutcome.next();

                System.out.println("h12");
                String cookingTime = serviceOutcome1.getString("cook_time");
                timesBuilder.append("Cooking Time: ").append(cookingTime).append(" minutes\n");
                System.out.println("h13");
                // serviceOutcome.next();
                timesBuilder.append(serviceOutcome1.getString("difficulty_level")).append("\n");
                // serviceOutcome.next();
                //Calculating the totalTime from the 2 times
                int cookingTemp = Integer.parseInt(cookingTime);
                int prepTemp = Integer.parseInt(prepTime);
                int totalTime = prepTemp + cookingTemp;
                timesBuilder.append("Total Time: ").append(totalTime).append(" minutes\n");
                timesBuilder.append("Difficulty Level: ").append(serviceOutcome1.getString("difficulty_level")).append("\n");

                //This is where we will put the information about times and difficulty
                String timeText = timesBuilder.toString();
                timeInformation = (TextArea) topPane.getRight();
                timeInformation.setText(timeText);

                //Now we will get the instructions and put them in another text field
                System.out.println("1");
                TextArea instructionsText = (TextArea) borderPane.getCenter();
                System.out.println("2");
                instructionsText.setText(serviceOutcome1.getString("instructions"));
                System.out.println("3");
            }

        } catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        } catch(ClassNotFoundException e){
            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
        } catch(SQLException e){
            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
        }
    }

    //this needs to be called
    public void reportServiceOutcomeIngredients() {
        try {
            //Here we get and show the ingredients information for each recipe selected
//            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
//            InputStream outcomeStream = clientSocket.getInputStream();
//            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream);
//            serviceOutcome = (CachedRowSet) outcomeStreamReader.readObject();
//            serviceOutcome.next();
//
//            TableView<RecipeTable> ingredientsTable = (TableView<RecipeTable>) borderPane.getRight();
//            //Accessing the ingredient table that is on the right side of the borderPane
//            ObservableList<RecipeTable> tmpInstructions = ingredientsTable.getItems();
//            tmpInstructions.clear();
//            RecipeTable recipe = new RecipeTable();
//            recipe.setIngredients(serviceOutcome.getString("ingredient_name"));
//            recipe.setQuantity(serviceOutcome.getString("quantity"));
//            recipe.setQuantityUnit(serviceOutcome.getString("quantity_unit"));
//            tmpInstructions.add(recipe);
//            ingredientsTable.setItems(tmpInstructions);

            //Here we get and show the ingredients information for each recipe selected
            InputStream outcomeStream = clientSocket.getInputStream();
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream);
            //this.serviceOutcome2 = (CachedRowSet) outcomeStreamReader.readObject();
            //serviceOutcome.next();
            ResultSet temp = (ResultSet) outcomeStreamReader.readObject();
            this.serviceOutcome2= RowSetProvider.newFactory().createCachedRowSet();
            this.serviceOutcome2.populate(temp);
            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
            VBox ingredientsBox = (VBox) borderPane.getRight();
            ObservableList<Node> right_children = ingredientsBox.getChildrenUnmodifiable();
            //getting the table thats there already in the centre
            TableView<RecipeTable> ingredientsOutput = new TableView<>();
            for (int i = 0; i < right_children.size(); i++){
                if (right_children.get(i) instanceof TableView){
                    ingredientsOutput = (TableView) right_children.get(i);
                    System.out.println("found table");
                }
            }

            //Accessing the ingredient table that is on the right side of the borderPane
            ObservableList<RecipeTable> tmpInstructions = ingredientsOutput.getItems();
            tmpInstructions.clear();
            while(this.serviceOutcome2.next()) {
                RecipeTable recipe = new RecipeTable();
                System.out.println("hello");
                recipe.setIngredients(serviceOutcome2.getString("ingredient_name"));
                System.out.println("1");
                recipe.setQuantity(serviceOutcome2.getString("quantity_needed"));
                System.out.println("2");
                recipe.setQuantityUnit(serviceOutcome2.getString("quantity_unit"));
                System.out.println("3");
                tmpInstructions.add(recipe);
            }
            this.serviceOutcome2.beforeFirst();
            for (int i = 0; i < tmpInstructions.size(); i++){
                System.out.println(tmpInstructions.get(i));
            }
            ingredientsOutput.setItems(tmpInstructions);

        } catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        } catch(ClassNotFoundException e){
            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
        } catch(SQLException e){
            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
        }
    }




    public void execute(){
        //do we need the driver? 4/12/24
//        try {
//            DriverManager.registerDriver(new org.postgresql.Driver());
//        }catch(Exception e){
//            System.out.println(e);
//        }

        try{
            //Initializes the socket
            this.initializeSocket();

            //Request service
            this.requestService();

            this.reportServiceOutcomeRecipes();

            //comment out close socket
            //this.clientSocket.close();


        }catch(Exception e)
        {// Raised if connection is refused or other technical issue
            System.out.println("Client: Exception " + e);
        }
    }

    public void getInstructionsExecute(){
        try{
            //Initializes the socket
            this.initializeSocket();

            //Request service
            this.requestInstructions();

            //this.reportServiceOutcomeInstructions();
            //this.reportInstructions(); //does this just do instructions or ingredients too
            //change to calling report service outcome instructions
            this.reportServiceOutcomeInstructions();
            //need to call report Ingredients
            this.reportServiceOutcomeIngredients();
            //closing the socket
            this.clientSocket.close();



        }catch(Exception e)
        {// Raised if connection is refused or other technical issue
            System.out.println("Client: Exception " + e);
        }
    }
    private Scene scene1;
    private Scene scene2;

    private TableView<RecipeTable> recipeTable;

    @Override
    public void start(Stage primaryStage) {
        // Create Scene 1 and Scene 2
        Scene scene1 = createScene1();
        // Set the initial scene
        primaryStage.setTitle("Recipe Suggestions");
        primaryStage.setScene(scene1);
        primaryStage.setResizable(true);
        primaryStage.show();
        thePrimaryStage = primaryStage;

    }

    private void filter(int filter_type){
        BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
        TableView<RecipeTable> recipeTable = (TableView<RecipeTable>) borderPane.getCenter();
        if (recipeTable == null) {
            recipeTable = new TableView<>();
            borderPane.setCenter(recipeTable);
            return;
        }
        ObservableList<RecipeTable> listOfRecipes = recipeTable.getItems();
        int length = listOfRecipes.size();
        if (filter_type == 0 || filter_type == 2){
            //Filter by time taken (total)
            for (int i = 0; i < (length-1); i++) {
                for (int j = 0; j < (length - 1); j++) {
                    int totalTime1 = Integer.parseInt(listOfRecipes.get(j).getTotalTime().get());
                    int totalTime2 = Integer.parseInt(listOfRecipes.get(j + 1).getTotalTime().get());
                    if (totalTime1 > totalTime2) {
                        RecipeTable temp = listOfRecipes.get(j);
                        listOfRecipes.set(j, listOfRecipes.get(j + 1));
                        listOfRecipes.set(j + 1, temp);

                    }
                }
            }
            if (filter_type == 2){
                Collections.reverse(listOfRecipes);
            }
        }
        else if (filter_type == 1){
            //Filter alphabetically
            for (int i = 0; i < (length-1); i++) {
                for (int j = 0; j < (length - 1); j++) {
                    String name1 = listOfRecipes.get(j).getName().get();
                    String name2 = listOfRecipes.get(j+1).getName().get();
                    if (name1.compareTo(name2) > 0) {
                        RecipeTable temp = listOfRecipes.get(j);
                        listOfRecipes.set(j, listOfRecipes.get(j + 1));
                        listOfRecipes.set(j + 1, temp);

                    }
                }
            }
        }
        else if (filter_type == 3 || filter_type == 4){
            for (int i = 0; i < (length-1); i++) {
                for (int j = 0; j < (length - 1); j++) {
                    int difficulty1 = getDifficulty(listOfRecipes.get(j).getDifficulty().get());
                    int difficulty2 = getDifficulty(listOfRecipes.get(j+1).getDifficulty().get());
                    if (difficulty1 > difficulty2){
                        RecipeTable temp = listOfRecipes.get(j);
                        listOfRecipes.set(j, listOfRecipes.get(j + 1));
                        listOfRecipes.set(j + 1, temp);
                    }
                }
            }
            if (filter_type == 4){
                Collections.reverse(listOfRecipes);
            }
            for (RecipeTable recipe: listOfRecipes){
                System.out.println(recipe.getName().get());
            }
        }

        TableView<RecipeTable> outputFilteredTable = (TableView<RecipeTable>) borderPane.getCenter();
        if (outputFilteredTable == null) {
            outputFilteredTable = new TableView<>();
            borderPane.setCenter(outputFilteredTable);
        }
        outputFilteredTable.setItems(listOfRecipes);
    }

    private int getDifficulty(String difficulty){
        int difficultyNumber = 0;
        if (difficulty.equals("easy")){
            difficultyNumber = 1;
        }
        else if (difficulty.equals("medium")){
            difficultyNumber = 2;
        }
        else if (difficulty.equals("hard")){
            difficultyNumber = 3;
        }
        return difficultyNumber;
    }

    private Scene createScene1() {
        BorderPane borderPane = new BorderPane();
        HBox buttonsBox = new HBox();
        HBox tableHolder = new HBox();
        //This is the button you press to generate the recipes (fill out the table)
        Button generate = new Button();
        generate.setText("Generate Recipes");
        generate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //Scene scene2 = createScene2();
                //primaryStage.setScene(scene2);
                me.execute();
            }
        });
        generate.setStyle("-fx-font-size: 14px;");

        buttonsBox.getChildren().add(generate);

        //added these two lines
        //Label recipeName = new Label("Recipe name:");
        //borderPane.getChildren().add(recipeName);

        Pane expanding = new Pane();
        buttonsBox.getChildren().add(expanding);
        HBox.setHgrow(expanding, Priority.ALWAYS);
        expanding.setMaxWidth(Double.MAX_VALUE);

        //This is a filter button that, when pressed, will produce a number of options
        MenuButton filter = new MenuButton("Filter by");
        MenuItem byName = new MenuItem("Alphabetically");
        MenuItem byTotalTimeLow = new MenuItem("Time (low to high)");
        MenuItem byTotalTimeHigh = new MenuItem("Time (high to low)");
        MenuItem byDifficultyLow = new MenuItem("Difficulty (low to high)");
        MenuItem byDifficultyHigh = new MenuItem("Difficulty (high to low)");

        byTotalTimeLow.setOnAction(event -> filter(0));
        byTotalTimeHigh.setOnAction(event -> filter(2));
        byName.setOnAction(event -> filter(1));
        byDifficultyLow.setOnAction(event -> filter(3));
        byDifficultyHigh.setOnAction(event -> filter(4));
        filter.setStyle("-fx-font-size: 14px;");
        filter.getItems().addAll(byName, byTotalTimeLow, byTotalTimeHigh, byDifficultyLow, byDifficultyHigh);
        //This is adding the menu options to the actual menu button
        buttonsBox.getChildren().add(filter);




        //This is the filter button in the top right of the grid pane
//        Button filter = new Button();
//        filter.setText("Filter Recipes");
//        filter.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                //filterRecipes();
//                //Make the filter recipes function
//            }
//        });
//        buttonsBox.getChildren().add(filter);
//        filter.setStyle("-fx-font-size: 14px;");

        TableView<RecipeTable> recipeTable = new TableView<>();
        recipeTable.setEditable(true);
        //This is the output table where all the recipes will be listed
        TableColumn<RecipeTable,String> recipe_name = new TableColumn<RecipeTable,String>("Recipe Name");
        TableColumn<RecipeTable,String> prep_time = new TableColumn<RecipeTable,String>("Prep Time");
        TableColumn<RecipeTable,String> cooking_time = new TableColumn<RecipeTable,String>("Cook Time");
        TableColumn<RecipeTable,String> total_time = new TableColumn<RecipeTable,String>("Total Time");
        TableColumn<RecipeTable,String> difficulty = new TableColumn<RecipeTable,String>("Difficulty");

        recipe_name.setCellValueFactory(new PropertyValueFactory("recipeName"));
        prep_time.setCellValueFactory(new PropertyValueFactory("prepTime"));
        cooking_time.setCellValueFactory(new PropertyValueFactory("cookTime"));
        total_time.setCellValueFactory(new PropertyValueFactory("totalTime"));
        difficulty.setCellValueFactory(new PropertyValueFactory("difficulty"));


        recipeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {  //if user clicks then event will be triggered
                RecipeTable selectedRecipe = recipeTable.getSelectionModel().getSelectedItem();
                if (selectedRecipe != null) {
                    // Switch to Scene 2 when a row is clicked
                    Scene myScene = createScene2();
                    System.out.println("Selected Recipe: " + selectedRecipe.getName().getValue());
                    //need to send request recipe to service
                    this.requestedRecipe = selectedRecipe.getName().getValue();
                    //me.getInstructionsExecute();
                    thePrimaryStage.setScene(createScene2());
                    me.getInstructionsExecute();
                    //primaryStageVariable.setScene(createScene2());
                }
            }
        });
        //This binds the widths of the table to the border pane width and height meaning that the table will fill out the whole space
        recipeTable.setStyle("-fx-font-size: 14px;");
        recipeTable.prefWidthProperty().bind(borderPane.widthProperty());
        recipeTable.prefHeightProperty().bind(borderPane.heightProperty());

        recipeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        recipeTable.setMaxHeight(Double.MAX_VALUE);
        recipeTable.setMaxWidth(Double.MAX_VALUE);


        //need to edit these so they fit correctly and fill whole page
        ObservableList<TableColumn<RecipeTable,?>> tmp = recipeTable.getColumns();
        tmp.addAll(recipe_name, prep_time, cooking_time, total_time, difficulty);

        borderPane.setTop(buttonsBox);   // Top section for the button
        borderPane.setCenter(recipeTable);

        return new Scene(borderPane, 800, 600);

    }

    private Scene createScene2() {
        //The main grid pane of the second scene
        BorderPane borderPane = new BorderPane();

        VBox rightPane = new VBox();
        BorderPane topPane = new BorderPane();

        //This is the back button, when pressed, the first scene will be loaded
        Button back = new Button();
        back.setText("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                thePrimaryStage.setScene(createScene1());
                // call execute method here

            }
        });
        back.setStyle("-fx-font-size: 16px;");
        topPane.setLeft(back);

        Label positioning = new Label("         ");
        topPane.setRight(positioning);
        //This label is only here to try and fix the positioning of the recipeName label

        //Adding the recipe name to the top of the scene
        Label recipeName = new Label();
        topPane.setCenter(recipeName);
        recipeName.setUnderline(true);
        recipeName.setStyle("-fx-font-size: 26px;");

        Label ingredientsLabel = new Label("Ingredients");
        ingredientsLabel.setStyle("-fx-font-size: 20px; -fx-border-color: black; -fx-border-width: 0.5; -fx-padding: 3;");

        //Now we will make the text area for the time and difficulty information
        TextArea timeInformation = new TextArea();
        timeInformation.setText("Testing Times");
        timeInformation.setWrapText(true); //Setting this to true allows the text to show if its too long
        timeInformation.setPrefHeight(120);
        timeInformation.setPrefWidth(230);
        topPane.setRight(timeInformation);
        timeInformation.setStyle("-fx-font-size: 14px;");

        //Now we will make the text area for the ingredients
        TextArea instructions = new TextArea();
        instructions.setText("Testing instructions");
        instructions.setWrapText(true);
        instructions.setStyle("-fx-font-size: 16px;");
        borderPane.setCenter(instructions);

        //Making the table where the ingredients and quantities will go
        TableView<RecipeTable> ingredientsTable = new TableView<RecipeTable>();
        TableColumn<RecipeTable,String> ingredient_name = new TableColumn<RecipeTable,String>("Ingredient");
        TableColumn<RecipeTable,String> quantity_needed = new TableColumn<RecipeTable,String>("Quantity");
        TableColumn<RecipeTable,String> quantity_unit = new TableColumn<RecipeTable,String>("Quantity Unit");
        VBox.setVgrow(ingredientsTable, Priority.ALWAYS);
        rightPane.getChildren().addAll(ingredientsLabel, ingredientsTable);

        ingredient_name.setCellValueFactory(new PropertyValueFactory("ingredients"));
        quantity_needed.setCellValueFactory(new PropertyValueFactory("quantity"));
        quantity_unit.setCellValueFactory(new PropertyValueFactory("quantityUnit"));
        borderPane.setRight(rightPane);
        //Will go on the right hand side of the screen

        ObservableList<TableColumn<RecipeTable,?>> tmp = ingredientsTable.getColumns();
        tmp.addAll(ingredient_name,quantity_needed,quantity_unit);

        borderPane.setTop(topPane);


        return new Scene(borderPane, 800, 600);
    }

    public static void main (String[] args) {
        launch(args);
        System.out.println("Client: Finished.");
        System.exit(0);
    }

}
