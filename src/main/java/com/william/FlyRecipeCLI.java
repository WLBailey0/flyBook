package com.william;

import com.william.dao.*;
import com.william.model.Flies;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

import java.util.List;
import java.util.Scanner;

public class FlyRecipeCLI {

    private final Scanner userInput = new Scanner(System.in);

    //declare instance variable of FliesDao
    private final FliesDao fliesDao;

    public FlyRecipeCLI(DataSource dataSource) {
        fliesDao = new JdbcFliesDao(dataSource);
    }

    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/FlyTying");
        dataSource.setUsername("postgres");
        dataSource.setPassword("katnis");
        FlyRecipeCLI application = new FlyRecipeCLI(dataSource);
        application.run();

    }
    private void run(){
        welcomeBanner();
        boolean isRunning = true;
        while(isRunning){
            displayMenu();
            String selection = userInput.nextLine();
            if(selection.equals("1")){
                //return list of all flies
                viewAllFlies();
            }
            else if (selection.equals("2")){
                //return a single fly
                promptForSingleFly();
            }

            else if (selection.equals("3")){
                //add a fly to the database
                modifyFly();

            }
            else if (selection.equals("0")){
                goodbyeMessage();
                isRunning = false;
            }
            else{
                System.out.println("Invalid Input\n");
            }
        }
    }
    private void modifyFly(){
        modifyFlyBanner();
        String choice = userInput.nextLine();
        if(choice.equals("1")){
            addFly();
        }
        else if (choice.equals("2")){
            updateFlyById();
        }
        else if (choice.equals("3")){
            removeFly();
        }
        else {
            System.out.println("Invalid Input\n");
        }
    }

    private void promptForSingleFly() {
        detailsBanner();
        String choice = userInput.nextLine();
        boolean valid = false;
        while (!valid) {
            if (choice.equals("1")) {
                selectSingleFly();
                valid = true;
            }
            else if (choice.equals("2")){
                viewRecipe();
                valid = true;
            }
        }
    }

    private void viewRecipe(){
        System.out.println("Select Fly By Id To View Recipe");
        String select = userInput.nextLine();
        boolean valid = false;
        while(!valid) {
            try {
                Flies fly = fliesDao.getRecipe(Integer.parseInt(select));
                System.out.printf("Recipe Location:\n%s\n", fly.getWebsite());
                valid = true;
            } catch (NumberFormatException | NullPointerException ex) {
                System.out.println("Enter the fly id");
                select = userInput.nextLine();
            }
        }
    }
    private void addFly(){
        Flies flies = promptForNewFly();
        flies = fliesDao.createFly(flies);
        if(flies != null){
            System.out.println("Adding Name: " +flies.getName());
        }
    }

    private void updateFlyById() {
        Flies flies = promptToUpdateFly();
        fliesDao.updateFly(flies);
    }

    private void viewAllFlies(){
        List<Flies> fliesList = fliesDao.getAllFlies();
        if (fliesList.size() == 0){
            System.out.println("No flies in the database\n");
        }
        else {
            for (Flies fly : fliesList) {
                System.out.printf("Fly Id %d, Name: %s, Creator: %s, Season %s\n",
                        fly.getFlyId(), fly.getName(), fly.getCreator(), fly.getSeason());
            }
        }
    }

    private void selectSingleFly() {

        String select = promptForId();
        boolean valid = true;
        while(valid) {
            try {
                if (fliesDao.getFly(Integer.parseInt(select)) != null) {
                    Flies fly = fliesDao.getFly(Integer.parseInt(select));
                    System.out.printf("name: %s, Creator: %s, Season %s, Target: %s" +
                            "\n", fly.getName(), fly.getCreator(), fly.getSeason(), fly.getTarget());
                    valid = false;
                } else if (fliesDao.getFly(Integer.parseInt(select)) == null || select.equals(null)) {
                    System.out.println("There is nothing at that location ");
                    select = userInput.nextLine();
                }
            }
            catch (NumberFormatException | NullPointerException ex){
                System.out.println("Enter the fly id");
                select = userInput.nextLine();
            }


        }
    }

    private void removeFly(){
        System.out.print("Enter the Id of the fly to delete >> ");
        String flyId = userInput.nextLine();
        Flies flies = fliesDao.getFly(Integer.parseInt(flyId));
        System.out.print("Confirm you want to delete (Y, N) >> " +
                flies.getName());
        String choice = userInput.nextLine().toLowerCase();
        boolean valid = false;
        while(!valid) {
            if (choice.equals("y")) {
                fliesDao.deleteFly(Integer.parseInt(flyId));
                valid = true;
            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println(flies.getName() + " will not be deleted");
                valid = true;
            } else {
                System.out.println("Invalid choice, enter (Y)es or (N)o");
            }
        }
    }

    private Flies promptForNewFly(){
        Flies flies = new Flies();


        String name = "";
        while(name.isBlank()){
            name = promptForName();
        }
        flies.setName(name);

        String creator = "";
        while(creator.isBlank()){
            creator = promptForCreator();
        }
        flies.setCreator(creator);

        String season = "";
        while(season.isBlank()){
            season = promptForSeason();
        }
        flies.setSeason(season);

        String target = "";
        while(target.isBlank()){
            target = promptForTarget();
        }
        flies.setTarget(target);

        String recipe = "";
        while(recipe.isBlank()){
            recipe = promptForRecipe();
        }
        flies.setWebsite(recipe);
        return flies;
    }
    private Flies promptToUpdateFly(){
        Flies flies = new Flies();

        int flyId = 0;
        while (flyId == 0){
            flyId =  Integer.parseInt(promptForId());
        }
        flies.setFlyId(flyId);

        String name = "";
        while(name.isBlank()){
            name = promptForName();
        }
        flies.setName(name);

        String creator = "";
        while(creator.isBlank()){
            creator = promptForCreator();
        }
        flies.setCreator(creator);

        String season = "";
        while(season.isBlank()){
            season = promptForSeason();
        }
        flies.setSeason(season);

        String target = "";
        while(target.isBlank()){
            target = promptForTarget();
        }
        flies.setTarget(target);

        String recipe = "";
        while(recipe.isBlank()){
            recipe = promptForRecipe();
        }
        flies.setWebsite(recipe);
        return flies;
    }

    private String promptForId() {
        System.out.print("Enter the Id of the fly >> ");
        return userInput.nextLine();
    }

    private String promptForName(){
        System.out.print("Enter the name of the Fly >> ");
        return userInput.nextLine();
    }
    private String promptForCreator(){
        System.out.print("Enter the name of the creator, N/A if unknown >> ");
        return userInput.nextLine();

    }
    private String promptForSeason(){
        System.out.print("Enter the best season for the fly >> ");
        return userInput.nextLine();
    }
    private String promptForTarget(){
        System.out.print("Enter the target species >> ");
        return userInput.nextLine();
    }
    private String promptForRecipe(){
        System.out.print("Enter the recipe location >> ");
        return userInput.nextLine();
    }
    private void goodbyeMessage(){
        System.out.println("|***********************|");
        System.out.println("|***** Happy Tying *****|");
        System.out.println("|***********************|");
    }
    private  void displayMenu(){
        System.out.println("1: View All Flies");
        System.out.println("2: View Fly Detail");
        System.out.println("3: Modify a Fly");
        System.out.println("0: Exit");
        System.out.print("Select an option >> ");
    }
    private void welcomeBanner(){
        System.out.println("|*****************************|");
        System.out.println("|*** Fly Tying Recipe Book ***|");
        System.out.println("|*****************************|");
    }
    private void modifyFlyBanner(){
        System.out.println("1: Add a Fly");
        System.out.println("2: Update a Fly");
        System.out.println("3: Remove a Fly");
        System.out.print("Select an option >> ");
    }
    private void detailsBanner(){
        System.out.println("1: View fly details");
        System.out.println("2: View fly recipe");
        System.out.print("Select an option >> ");
    }
}
