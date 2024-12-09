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

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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

public class Client extends Application{

    public static Client me; //Get the application instance in javafx
    public static Stage thePrimaryStage;  //Get the application primary scene in javafx

    //public static Stage theSecondaryStage;
    private Socket clientSocket = null;

    private CachedRowSet serviceOutcome = null; //The service outcome
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

    public void reportServiceOutcomeInstructions() {
        try {
            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
            InputStream outcomeStream = clientSocket.getInputStream();
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream);
            serviceOutcome = (CachedRowSet) outcomeStreamReader.readObject();
            serviceOutcome.next();

            //This is the recipe name, which we are setting as a label
            BorderPane topPane = (BorderPane) borderPane.getTop();
            recipeName = (Label) topPane.getCenter();
            recipeName.setText(serviceOutcome.getString("recipe_name"));
            serviceOutcome.next();
            //We have to set the service outcome at next after every time we read a line

            //Now we will put the information about the recipe times in the text area
            StringBuilder timesBuilder = new StringBuilder();
            //Iterating through to get the different lines from the input
            String prepTime = serviceOutcome.getString("prepTime");
            timesBuilder.append("Prep Time: ").append(prepTime).append(" minutes\n");
            serviceOutcome.next();
            String cookingTime = serviceOutcome.getString("cookingTime");
            timesBuilder.append("Cooking Time: ").append(cookingTime).append(" minutes\n");
            serviceOutcome.next();
            timesBuilder.append(serviceOutcome.getString("difficultyLevel")).append("\n");
            serviceOutcome.next();
            //Calculating the totalTime from the 2 times
            int cookingTemp = Integer.parseInt(cookingTime);
            int prepTemp = Integer.parseInt(prepTime);
            int totalTime = prepTemp + cookingTemp;
            timesBuilder.append("Total Time: ").append(totalTime).append(" minutes\n");
            timesBuilder.append("Difficulty Level: ").append(serviceOutcome.getString("difficultyLevel")).append("\n");
            //This is where we will put the information about times and difficulty
            String timeText = timesBuilder.toString();
            timeInformation = (TextArea) topPane.getRight();
            timeInformation.setText(timeText);

            //Now we will get the instructions and put them in another text field
            TextArea instructionsText = (TextArea) borderPane.getCenter();
            instructionsText.setText(serviceOutcome.getString("instructions"));

        } catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        } catch(ClassNotFoundException e){
            System.out.println("Client: Unable to cast read object to CachedRowSet. " + e);
        } catch(SQLException e){
            System.out.println("Client: Can't retrieve requested attribute from result set. " + e);
        }
    }

    public void reportServiceOutcomeIngredients() {
        try {
            //Here we get and show the ingredients information for each recipe selected
            BorderPane borderPane = (BorderPane) thePrimaryStage.getScene().getRoot();
            InputStream outcomeStream = clientSocket.getInputStream();
            ObjectInputStream outcomeStreamReader = new ObjectInputStream(outcomeStream);
            serviceOutcome = (CachedRowSet) outcomeStreamReader.readObject();
            serviceOutcome.next();

            TableView<RecipeTable> ingredientsTable = (TableView<RecipeTable>) borderPane.getRight();
            //Accessing the ingredient table that is on the right side of the borderPane
            ObservableList<RecipeTable> tmpInstructions = ingredientsTable.getItems();
            tmpInstructions.clear();
            RecipeTable recipe = new RecipeTable();
            recipe.setIngredients(serviceOutcome.getString("ingredient_name"));
            recipe.setQuantity(serviceOutcome.getString("quantity"));
            recipe.setQuantityUnit(serviceOutcome.getString("quantity_unit"));
            tmpInstructions.add(recipe);
            ingredientsTable.setItems(tmpInstructions);

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

            this.clientSocket.close();


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

            this.reportServiceOutcomeInstructions();

            this.clientSocket.close();


        }catch(Exception e)
        {// Raised if connection is refused or other technical issue
            System.out.println("Client: Exception " + e);
        }
    }
    private Scene scene1;
    private Scene scene2;

    private TableView<RecipeTable> recipeTable;
    private Stage thePrimaryStageprimaryStage;
    @Override
    public void start(Stage primaryStage) {
        // Create Scene 1 and Scene 2
        Scene scene1 = createScene1();

        // Set the initial scene
        primaryStage.setTitle("Recipe Suggestions");
        primaryStage.setScene(scene1);
        primaryStage.show();
        thePrimaryStage = primaryStage;

    }

    private Scene createScene1() {
        BorderPane borderPane = new BorderPane();
        HBox buttonsBox = new HBox();


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

        //This is the filter button in the top right of the grid pane
        Button filter = new Button();
        filter.setText("Filter Recipes");
        filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //filterRecipes();
                //Make the filter recipes function
            }
        });
        buttonsBox.getChildren().add(filter);
        filter.setStyle("-fx-font-size: 14px;");

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
                    Scene myScene = createScene1();
                    System.out.println("Selected Recipe: " + selectedRecipe.getName());
                    thePrimaryStage.setScene(createScene2());
                }
            }
        });
        //This binds the widths of the table to the border pane width and height meaning that the table will fill out the whole space
        //recipeTable.prefWidthProperty().bind(borderPane.widthProperty());
        //recipeTable.prefHeightProperty().bind(borderPane.heightProperty());


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
            }
        });
        back.setStyle("-fx-font-size: 14px;");
        topPane.setLeft(back);

        Label positioning = new Label("         ");
        topPane.setRight(positioning);
        //This label is only here to try and fix the positioning of the recipeName label

        //Adding the recipe name to the top of the scene
        Label recipeName = new Label();
        topPane.setCenter(recipeName);
        recipeName.setStyle("-fx-font-size: 18px;");

        Label ingredientsLabel = new Label("Ingredients");
        ingredientsLabel.setStyle("-fx-font-size: 16px;");

        //Now we will make the text area for the time and difficulty information
        TextArea timeInformation = new TextArea();
        timeInformation.setText("Testing Times");
        timeInformation.setWrapText(true); //Setting this to true allows the text to show if its too long
        topPane.setRight(timeInformation);

        //Now we will make the text area for the ingredients
        TextArea instructions = new TextArea();
        instructions.setText("Testing instructions");
        instructions.setWrapText(true);
        borderPane.setCenter(instructions);

        //Making the table where the ingredients and quantities will go
        TableView<RecipeTable> ingredientsTable = new TableView<RecipeTable>();
        TableColumn<RecipeTable,String> ingredient_name = new TableColumn<RecipeTable,String>("Ingredient");
        TableColumn<RecipeTable,String> quantity_needed = new TableColumn<RecipeTable,String>("Quantity");
        TableColumn<RecipeTable,String> quantity_unit = new TableColumn<RecipeTable,String>("Quantity Unit");
        //rightPane.getChildren().addAll(ingredientsLabel, ingredientsTable);

        ingredient_name.setCellValueFactory(new PropertyValueFactory("Ingredient"));
        quantity_needed.setCellValueFactory(new PropertyValueFactory("Quantity"));
        quantity_needed.setCellValueFactory(new PropertyValueFactory("QuantityUnit"));
        //borderPane.setRight(rightPane);
        //Will go on the right hand side of the screen

        borderPane.setTop(ingredientsTable);

        getInstructionsExecute();
        return new Scene(borderPane, 800, 600);
    }

    public static void main (String[] args) {
        launch(args);
        System.out.println("Client: Finished.");
        System.exit(0);
    }

}
