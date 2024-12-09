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

       String sqlInsert2 = "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (0, 'Eggs On Toast', '1. Fry two eggs in a pan.\n2. Put bread in toaster.\n3. Butter toast.\n4. Put eggs on toast.', 3, 5, 'Easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (1, 'Cereal', '1. Pour cereal into bowl.\n2. Add milk\n', 1, 0, 'Easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (2, 'Guacamole', '1. Mash avocado in a bowl.\n 2. Add salt and pepper and stir in.\n3.Chop pepper and add in.\n4. Add chilli flakes', 10, 0, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (3, 'Tomato Pasta', '1. Boil pasta for 10 minutes.\n2. Drain pasta.\n3. Add tomato sauce and tomato puree', 2, 10, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (4, 'Mozerella Omlette', '1. Crack 2 eggs into a cup and whisk.\n2. Pour eggs into frying pan.\n3. Grate in 10g mozarella.\n4. Leave to cook.', 5, 5, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (5, 'Chicken Fajitas', '1. Chop chicken\n2. Chop onion, pepper and mushroom.\n3. Fry chicken, pepper and onion with seasonings until cooked.\n4. Add salsa, sour cream, chicken and vegatables to a tortilla wrap. ', 15, 15, 'Hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (6, 'Bacon Risotto', '1. Chop bacon and mushrooms.\n2. Fry bacon and onions until golden.\n3. Boil rice in stock.\n4. Once rice is cooked, stir in bacon and mushrooms. ', 5, 20, 'Hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (7, 'Chicken Udon Noodles', '1. Fry chicken.\n2. Chop and fry mushrooms, onion and pepper with the chicken.\n3. Add in udon noodles.\n4. Add in seasonings of choice. ', 10, 15, 'Hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (8, 'Steak and Chips', '1. Put chips in oven for 20 minutes to cook.\n2. Fry steak with salt and pepper. ', 5, 20, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (9, 'Sausage Pasta ', '1. Boil pasta for 10 minutes.\n2. Chop and fry sausage with fennel seeds and chilli flakes.\n3. Drain pasta and stir in with cooked sausage ', 5, 15, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (10, 'Chilli Con Carne', '1. Fry mince for 5 minutes.\n2. Boil rice.\n3. Fry onion and garlic.\n4. Pour in chopped tomatoes and leave to simmer.\n5. Add seasonings of choice. ', 10, 15, 'Hard');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (11, 'Tofu Fried Rice', '1. Boil rice.\n2. Fry tofu with seasonings of choice until golden.\n3. Combine cooked rice with tofu and fry for 2 minutes.', 5, 15, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (12, 'Stir Fry ', '1. Boil noodles for 3 minutes.\n2. Chop and fry onions, peppers and beansprouts.\n3. Fry noodles, vegetables and soy sauce for 2 minutes. ', 5, 10, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (13, 'Tuna Salad Sandwich ', '1. Combine mayonnaise and tuna and stir together.\n2. Wash and cut lettuce to size.\n3. Butter bread.\n4. Spread tuna mixture between bread and add salad.', 10, 0, 'Easy ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (14, 'Bacon Sandwich', '1. Fry bacon in a pan until cooked.\n2. Butter bread.\n3. Place cooked bacon on the bread and add desired sauces', 2, 5, 'Easy');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (15, 'Chicken Salad Wrap', '1. Fry chicken until well cooked.\n2. Wash and cut salad.\n3. Add mayonnaise, chicken and salad into your tortilla wrap ', 10, 10, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (16, 'Chicken Pesto Pasta', '1. Fry chicken.\n2. Boil pasta until cooked.\n3. Drain pasta and stir in pesto and chicken ', 2, 15, 'Medium');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (17, 'Salmon and Potatoes', '1. Cook salmon in oven for 20 minutes.\n2. Season potatoes with salt and herbs.\n3. Roast potatos in oven for 20 minutes. ', 5, 20, 'Medium ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (18, 'Cod and Chips', '1. Place cod and chips on baking tray.\n2. Place in oven for 25 minutes.\n3. Cook and drain peas and sweetcorn. ', 3, 25, 'Medium ');\n" +
               "INSERT INTO recipes (recipe_id, recipe_name, instructions, prep_time, cook_time, difficulty_level) VALUES (19, 'Pancakes', '1. Mix flour, egg and milk in a large bowl.\n2. Leave to sit for 20 minutes.\n3. Pour mixture into pan and let fry until golden on both sides. ', 25, 10, 'Medium ');";

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
