package com.example.uniwallet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManager{
    ArrayList<Account> usernameList = new ArrayList<Account>();
    private static final String ACCOUNTS_DIRECTORY = "E:\\Application Programming Project\\DirectoryCreationTests\\Accounts";
    private static final String accountsCSV = "AllAccounts.csv";
    File allAccountsFile = new File(ACCOUNTS_DIRECTORY, "AllAccounts.csv");
    File directory;

    // Account account;

    private int userCount;
    private String username;
    private String password;

    public AccountManager(String username, String password) {
        this.username = username;
        this.password = password;
        this.userCount = 0;
    }


    public boolean checkSignUpParameters(String username, String password) {

        File file = new File(ACCOUNTS_DIRECTORY);

        file.equals(username);
        if (usernameExists(username)) {
            System.out.println("this username already exists");
            return false;
        } else if (!file.equals(username)){
            return true;
        }//read csv to see if duplicate username
        //if non existing pass else fail
        return false;

    }

    public boolean usernameExists(String username) {
        for (Account account : usernameList) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Account signUp(String username, String password) throws IOException {
        System.out.println(checkSignUpParameters(username, password));

        if (checkSignUpParameters(username, password) == false) {
            System.out.println("sign up parameters bad");
            return null;
        } else if (checkSignUpParameters(username, password)) {
            System.out.println("sign up parameters good");


            Account account = new Account(userCount,username, password);
            createDirectory(ACCOUNTS_DIRECTORY, username, account);
            account.setDefaultValues();
            createCSVFiles(account, directory);
            //writeToCSV(account, balanceFile);
            //account.createAccountCSVFiles();
            //account.setCSVDefaults();

            usernameList.add(account);
            System.out.println(account.getBudget());
            System.out.println(account + " the directory has been created");


            return account;
        }
        return null;
    }

    public Account login(String username, String password) {


        Path accountsFilePath = Paths.get(ACCOUNTS_DIRECTORY, accountsCSV);

        try {
            List<String> lines = Files.readAllLines(accountsFilePath);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    int userID = Integer.parseInt(parts[0]);
                    if (parts[1].equals(username) && parts[2].equals(password)) {
                        System.out.println("Login successful!");
                        Account account = new Account(userID, username, password);
                        return account;
                        //with this return you can go to the next activity
                        /*
                         * pass THIS ACCOUNT as intent for next activity
                         * these next methods will be invoked in the next activity with the intent
                         * Accountmanager am = new AccountManager(account) pass in the intent account
                         * am.expense
                         * am.quickAdd
                         * am.quickRemove
                         * am.Income
                         */
                        //is this where all login specfic stuff has to be achieved?


                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Login failed: Incorrect username or password.");
        return null;
    }


    public void createDirectory(String ACCOUNTS_DIRECTORY, String username, Account account) throws IOException{
        //String workspacePath = System.getProperty("D:\\Application Programming Project\\DirectoryCreationTests\\Accounts");
        File directory = new File(ACCOUNTS_DIRECTORY, username);


        try {
            allAccountsFile.createNewFile();

            int highestUserId = getHighestUserId(directory);
            userCount = highestUserId + 1; // Increment the highest user ID
            FileWriter fw = new FileWriter(allAccountsFile, true); // Append mode
            //PrintWriter pw = new PrintWriter(fw);

            fw.write(String.format("%d,%s,%s\n", userCount, username, password));
            fw.close();

            if (!directory.exists()) {

                directory.mkdirs();
                createCSVFiles(account, directory);

                System.out.println("Directory created for " + username);
            } else if (directory.exists()){
                System.err.println(username + " directory already exists.");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createCSVFiles(Account account, File directory) {

        try {
            //int highestUserId = getHighestUserId(directory);
            //userCount = highestUserId + 1;
            File infoFile = new File(directory, "accountInfo.csv");
            File balanceFile = new File(directory, "accountBalance.csv");
            File incomeFile = new File(directory, "accountIncome.csv");
            File expenseFile = new File(directory, "accountExpense.csv");
            File quickAddFile = new File(directory, "accountQuickAdd.csv");
            File quickRemoveFile = new File(directory, "accountQuickRemove.csv");

            infoFile.createNewFile();
            balanceFile.createNewFile();
            incomeFile.createNewFile();
            expenseFile.createNewFile();
            quickAddFile.createNewFile();
            quickRemoveFile.createNewFile();

            //ADD USERID, and categories 1st line for each csv
            FileWriter fwInfo = new FileWriter(infoFile, true); // Append mode
            fwInfo.write(String.format("%d,%s,%s\n", userCount, account.getUsername(), account.getPassword()));
            fwInfo.close();

            FileWriter fwBalance = new FileWriter(balanceFile, true);
            fwBalance.write(String.format("%d,%s,%s\n", userCount, account.getBudget(), account.getBalance()));
            fwBalance.close();

            FileWriter fwIncome = new FileWriter(incomeFile, true);
            fwIncome.write(String.format("%d,%s,%s\n", userCount, account.getPay(), account.getTimeRate()));
            fwIncome.close();

            FileWriter fwExpense = new FileWriter(expenseFile, true);
            fwExpense.write(String.format("%d,%s,%s\n", userCount, account.getCategory(), account.getRate(), account.getCost()));
            fwExpense.close();

            FileWriter fwQuickAdd = new FileWriter(quickAddFile, true);
            fwQuickAdd.write(String.format("%d,%s,%s\n", userCount, account.getTransactionNumber(), account.getAmount()));
            fwQuickAdd.close();

            FileWriter fwQuickRemove = new FileWriter(quickRemoveFile, true);
            fwQuickRemove.write(String.format("%d,%s,%s\n", userCount, account.getTransactionNumber2(), account.getCategory2(), account.getItem(), account.getAmount2()));
            fwQuickRemove.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public void writeToCSV(Account account, File balanceFile) {

        try {
            FileWriter fwBalance = new FileWriter(balanceFile);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getHighestUserId(File directory) throws NumberFormatException, IOException {
        int highestUserId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(allAccountsFile))) {
            String line;
            //boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                //if (!headerSkipped) {
                //	headerSkipped = true;
                //continue;
                //}
                String[] tokens = line.split(",");
                if (tokens.length > 0 && !tokens[0].isEmpty()) {
                    try {
                        int userId = Integer.parseInt(tokens[0]);
                        highestUserId = Math.max(highestUserId, userId);
                    } catch (NumberFormatException e) {
                        // Handle if the userID cannot be parsed as an integer
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return highestUserId;
    }

    public void deleteAccount(String username) {
        Path accountsFilePath = Paths.get(ACCOUNTS_DIRECTORY, accountsCSV);
        List<String> updatedLines = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(accountsFilePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(username)) { // Check if there are at least two parts
                    lines.remove(i); // Remove the line containing the username to delete
                    Files.write(accountsFilePath, lines); // Write the updated lines back to the file
                    System.out.println("Account deleted successfully!");

                    File accountDirectory = new File(ACCOUNTS_DIRECTORY, username);
                    if (accountDirectory.exists()) {
                        deleteDirectory(accountDirectory);
                        System.out.println("Account directory for " + username + " has been deleted.");
                    } else {
                        System.out.println("Account directory for " + username + " does not exist.");
                    }

                    return;
                }
            }

            // Write the updated lines back to the file, effectively removing the account
            Files.write(accountsFilePath, updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete the account from AllAccounts.csv");
            return;
        }


    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete(); // Delete the directory itself
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}

