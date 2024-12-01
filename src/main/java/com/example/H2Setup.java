package com.example;

import java.sql.*;

public class H2Setup {
   public void initialise() throws SQLException {
       //creates the H2 database schema and creates all the tables
       String jdbcURL = "jdbc:h2:mem:receipe";
       System.out.println("**** Setting up H2 Database *****");
       Connection connection = DriverManager.getConnection(jdbcURL);

       String sql1 = "CREATE TABLE recipes (\n" +
               "    recipe_id INT NOT NULL PRIMARY KEY,\n" +
               "    recipe_name VARCHAR(250),\n" +
               "    instructions VARCHAR(4000),\n" +
               "    prep_time INT,\n" +
               "    cook_time INT,\n" +
               "    difficulty_level VARCHAR(250)\n" +
               "); ";
       String sql2 = "CREATE TABLE fridge_items (\n" +
               "    ingredient_id INT NOT NULL,\n" +
               "    quantity_available INT NOT NULL,\n" +
               "    PRIMARY KEY (ingredient_id),\n" +
               "    FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id)\n" +
               "        ON UPDATE NO ACTION\n" +
               "        ON DELETE NO ACTION\n" +
               ");";
       String sql3 = "CREATE TABLE ingredients (\n" +
               "    ingredient_id INT NOT NULL,\n" +
               "    ingredient_name VARCHAR(250),\n" +
               "    PRIMARY KEY (ingredient_id)\n" +
               ");";
       String sql4 = "CREATE TABLE recipe_ingredients (\n" +
               "    recipe_id INT NOT NULL,\n" +
               "    ingredient_id INT NOT NULL,\n" +
               "    quantity_needed INT NOT NULL,\n" +
               "    quantity_unit VARCHAR(250),\n" +
               "    PRIMARY KEY (recipe_id, ingredient_id),\n" +
               "    FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id) \n" +
               "        ON UPDATE NO ACTION \n" +
               "        ON DELETE NO ACTION,\n" +
               "    FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id) \n" +
               "        ON UPDATE NO ACTION \n" +
               "        ON DELETE NO ACTION\n" +
               ");";

       Statement statement = connection.createStatement();

       statement.execute(sql1);
       statement.execute(sql3);
       statement.execute(sql2);
       statement.execute(sql4);



       String sqlInsert1 = "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (0, 250);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (1, 2000);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (2, 3);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (3, 100);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (4, 50);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (5, 100);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (6, 200);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (7, 500);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (8, 300);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (9, 50);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (10, 89);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (11, 56);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (12, 300);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (13, 50);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (14, 100);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (15, 3);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (16, 1);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (17, 79);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (18, 1);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (19, 1);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (20, 500);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (21, 200);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (22, 100);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (23, 500);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (24, 150);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (25, 200);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (26, 50);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (28, 50);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (29, 500);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (30, 100);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (31, 200);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (48, 75);\n" +
               "INSERT INTO fridge_items (ingredient_id, quantity_available) VALUES (49, 100);";

       String sqlInsert2 = "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (0, 'eggs on toast', 'fry two eggs. Put bread in toaster. Butter toast. Put eggs on toast.', 3, 5, 'easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (1, 'cereal', 'pour cereal into bowl. Add milk', 1, 0, 'easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (2, 'quacamole', 'mash avocado. Add salt. Add pepper. Add chilli flakes', 10, 0, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (3, 'tomato pasta', 'boil pasta for 10 minutes. Drain pasta. Add tomato sauce. Add tomato puree', 2, 10, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (4, 'mozerella omlette', 'crack 2 eggs into a cup and whisk. Pour eggs into frying pan. Grate in 10g mozerlla. Leave to cook.', 5, 5, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (5, 'chicken fajita', 'place chicken in frying pan with seasonings and cook. Chop and fry onion, pepper and mushroom.  Add salsa, sour cream, chicken and vegatables to a tortilla wrap. ', 15, 15, 'hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (6, 'bacon risotto', 'chop and fry bacon and mushrooms. Boil rice in stock. Once rice is cooked, stir in bacon and mushrooms  ', 5, 20, 'hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (7, 'chicken udon ', 'fry chicken. Chop and fry mushrooms, onion and pepper with the chicken. Add in udon noodles. Add in seasonings of choice. ', 10, 15, 'hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (8, 'steak and chips', 'put chips in oven to cook. Fry steak with salt and pepper. ', 5, 20, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (9, 'sausage pasta ', 'boil pasta for 10 minutes. Chop and fry sausage with fennel seads and chilli flakes. Drain pasta and stir in with sausage ', 5, 15, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (10, 'chilli con carne', 'cook mince. Boil rice. Fry onion and garlic.  Pour in chopped tomatoes and leave to simmer. Add seasonings of choice. ', 10, 15, 'hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (11, 'tofu fried rice', 'boil rice. Cook tofu with seasonings of choice. Place boiled rice in frying pan along with tofu and fry for a few minutes.', 5, 15, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (12, 'stir fry ', 'boil noodles for 3 minutes. Chop and fry onions, peppers and beansprouts. Add soy sauce to vegeatbles. ', 5, 10, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (13, 'tuna salad sandwhich ', 'add mayonaise to tuna and stir. Wash and cut lettuce. Butter bread. Add tuna and salad in between slices of bread.', 10, 0, 'easy ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (14, 'bacon sandwhich', 'fry bacon in a pan. Butter bread. Place cooked bacon on the bread. Add desired sauces', 2, 5, 'easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (15, 'chicken salad wrap', 'fry chicken. Wash and cut salad. Add mayonaise, chicken and salad into a tortilla wrap ', 10, 10, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (16, 'chicken pesto pasta', 'fry chicken. Boil pasta. Drain pasta and stir in pesto and chicken ', 2, 15, 'medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (17, 'salmon and potatoes', 'cook salmon in oven. Roast potatoes, seasoning with salt and herbs. ', 5, 30, 'medium ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (18, 'cod and chips', 'cook cod and chips in the oven. Cook and drain peas and sweetcorn. ', 3, 20, 'medium ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (19, 'pancakes', 'mix flour, egg and milk in a large bowl. Leave to sit for 20 minutes. Pour mixture into pan and let fry. ', 20, 10, 'medium ');";

       String sqlInsert3 = "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (0, 'butter');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (1, 'milk');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (2, 'egg');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (3, 'tomato');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (4, 'pepper');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (5, 'mushroom');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (6, 'ketchup');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (7, 'pasta sauce');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (8, 'mayonaise');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (9, 'lemon juice');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (10, 'pesto');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (11, 'olives');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (12, 'yogurt');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (13, 'humous');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (14, 'carrot');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (15, 'onion');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (16, 'avocado');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (17, 'brocoli');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (18, 'cucumber');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (19, 'lettuce');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (20, 'mince');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (21, 'sausage');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (22, 'bacon');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (23, 'chicken');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (24, 'steak');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (25, 'cheddar');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (26, 'mozerella');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (27, 'garlic');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (28, 'parmesan');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (29, 'oat milk');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (30, 'tofu');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (31, 'prawns');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (32, 'bread');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (33, 'penne pasta');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (34, 'spagetti');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (35, 'granola');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (36, 'coco pops');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (37, 'honey');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (38, 'olive oil');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (39, 'sesame oil');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (40, 'vegetable oil');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (41, 'rice');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (42, 'noodle');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (43, 'flour');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (44, 'sugar');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (45, 'potato');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (46, 'salt');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (47, 'black pepper');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (48, 'salmon');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (49, 'cod');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (50, 'sweet potato');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (51, 'lasagne sheet');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (52, 'ginger');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (53, 'salt');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (54, 'cereal');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (55, 'chilli flakes');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (56, 'tomato sauce');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (57, 'tomato puree');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (58, 'salsa');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (59, 'sour cream');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (60, 'udon noodles');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (61, 'frozen chips');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (62, 'fennel seeds');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (63, 'chopped tomatoes');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (64, 'beansprouts');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (65, 'soy sauce');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (66, 'tuna');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (67, 'tortilla wrap');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (68, 'peas');\n" +
               "INSERT INTO ingredients (ingredient_id, ingredient_name) VALUES (69, 'sweetcorn');";

       String sqlInsert4 = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (0, 2, 2, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (0, 32, 1, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (0, 0, 5, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (1, 1, 100, 'millilitres');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (2, 53, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (2, 47, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (2, 55, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (2, 16, 1, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (3, 35, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (3, 56, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (3, 57, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (4, 2, 2, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (4, 26, 10, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (5, 23, 200, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (5, 15, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (5, 67, 1, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (5, 4, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (5, 5, 30, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (6, 22, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (6, 5, 30, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (6, 41, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (7, 23, 200, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (7, 5, 30, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (7, 15, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (7, 4, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (7, 60, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (8, 61, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (8, 24, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (8, 53, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (8, 47, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (9, 33, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (9, 21, 70, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (9, 62, 2, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (9, 55, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (10, 20, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (10, 41, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (10, 15, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (10, 27, 1, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (10, 63, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (11, 41, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (11, 30, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (11, 5, 30, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (11, 4, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (12, 42, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (12, 15, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (12, 4, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (12, 64, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (12, 65, 10, 'millilitres');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (13, 8, 5, 'millilitres');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (13, 66, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (13, 19, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (13, 32, 2, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (14, 22, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (14, 32, 2, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (15, 23, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (15, 19, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (15, 8, 5, 'millilitres');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (15, 67, 1, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (16, 23, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (16, 33, 75, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (16, 10, 10, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (17, 48, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (17, 45, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (17, 53, 1, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (18, 49, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (18, 61, 50, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (18, 68, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (18, 69, 20, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (19, 43, 100, 'grams');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (19, 2, 2, 'count');\n" +
               "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity_needed, quantity_unit) VALUES (19, 1, 100, 'millilitres');";

       statement.executeUpdate(sqlInsert2);
       statement.executeUpdate(sqlInsert3);
       statement.executeUpdate(sqlInsert1);
       statement.executeUpdate(sqlInsert4);
       System.out.println("****** H2 Database Setup Completed ******");
 //      connection.close(); // can't close the connection as we need to access the in-memory database, and this would destroy it
       // TODO: if we want to, changing database to embedded would allow us to close the connection, however would have to change this file so that it checks if table/data already exists.

   }



}
