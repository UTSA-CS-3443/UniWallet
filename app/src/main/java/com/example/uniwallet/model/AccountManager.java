package com.example.uniwallet.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.uniwallet.SignUpActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AccountManager{
    ArrayList<Account> usernameList;
    private static final String ACCOUNTS_DIRECTORY = "Accounts";
    private static final String accountsCSV = "users.csv";
    private static final String TAG = "Accounts";

    private int userID;
    private String username;
    private String password;
    private final String filename;
    private Activity activity;

    public AccountManager(Activity activity){
        this.activity = activity;
        usernameList = new ArrayList<Account>();
        filename = "users.csv";
        this.username = username;
        this.password = password;
        this.userID = 0;
    }

    public boolean checkSignUpParameters(String username, String password) {

        File file = new File(ACCOUNTS_DIRECTORY);

        file.equals(username);
        if (usernameExists(username)) {
            System.out.println("this username already exists");
            return false;
        } else if (!file.equals(username)){
            return true;
        }
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

        if (!checkSignUpParameters(username, password)) {
            System.out.println("sign up parameters bad");
            return null;
        } else if (checkSignUpParameters(username, password)) {
            Log.i("Sign up paras good", username);


            Account account = new Account(userID,username, password);


            createDirectory(username);
            initializeFiles(username);
            addUser(account);

            saveFile();

           // createDirectory(username);
            //createDirectory(ACCOUNTS_DIRECTORY, username, account);

            account.setDefaultValues();
            //createCSVFiles(account, createDirectory(username));

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


    private void createDirectory(String username) {

        File directory = activity.getFileStreamPath(filename).getParentFile();

        File playerDirectory = new File(directory, username);

        if (!playerDirectory.exists()) {

            if (playerDirectory.mkdirs()) {
                Log.i(TAG, "Directory created successfully: " + playerDirectory.getAbsolutePath());
            } else {
                Log.e(TAG, "Failed to create directory: " + playerDirectory.getAbsolutePath());
            }
        } else {
            Log.i(TAG, "Directory already exists: " + playerDirectory.getAbsolutePath());
        }
    }
    private void initializeFiles(String username) {
        try {
            Log.i(TAG, "Attempting to read from file ...");
                InputStream in = activity.openFileInput(filename);
            Log.i(TAG, "SUCCESS");
        } catch (FileNotFoundException e1) {
            Log.e(TAG, "Unable to read from file. File does not exist. " + filename);
            try {
                Log.i(TAG, "Attempting to create a file ...");
                OutputStream out = activity.openFileOutput(filename, Context.MODE_PRIVATE);
                Log.i(TAG, "SUCCESS");
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "Unable to create file. " + filename);
            }
        }
        try {

            File directory = activity.getFileStreamPath(ACCOUNTS_DIRECTORY).getParentFile();
            File userDirectory = new File(directory, username);

            if (!userDirectory.exists()) {
                if (!userDirectory.mkdirs()) {
                    Log.e(TAG, "Failed to create user directory: " + userDirectory.getAbsolutePath());
                    return;
                }
            }

            String[] csvFiles = {"accountInfo.csv", "accountBalance.csv", "accountIncome.csv",
                    "accountExpense.csv", "accountQuickAdd.csv", "accountQuickRemove.csv"};


            for (String fileName : csvFiles) {
                File csvFile = new File(userDirectory, fileName);
                if (!csvFile.exists()) {
                    if (csvFile.createNewFile()) {
                        Log.i(TAG, "CSV file created successfully: " + csvFile.getAbsolutePath());
                    } else {
                        Log.e(TAG, "Failed to create CSV file: " + csvFile.getAbsolutePath());
                    }
                } else {
                    Log.i(TAG, "CSV file already exists: " + csvFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred: " + e.getMessage());
        }
    }
    public void addUser(Account account){
        if(usernameList != null){
            Log.i(TAG, "Adding " + account + " to the players");
            usernameList.add(account);
            createDirectory(account.getUsername());
            createCSVFiles(account, new File(activity.getFileStreamPath(filename).getParentFile(), account.getUsername()));
        }
    }

    public void saveFile() {
        try {
            Log.i(TAG, "Attempting to save to file ... ");

            OutputStream out = activity.openFileOutput(filename, Context.MODE_APPEND);
            Log.i(TAG, "SUCCESS");
            if (usernameList != null) {

                int lastUserCount = getUserCountFromCSV();
                for (Account account : usernameList) {

                    lastUserCount++;
                    account.setUserID(lastUserCount);

                    String userData = String.format("%d,%s,%s\n", account.getUserID(), account.getUsername(), account.getPassword());
                    out.write(userData.getBytes(StandardCharsets.UTF_8));
                }
            }
            out.close();
        } catch (IOException e) {
            Log.i(TAG, "Failed to write to file. " + filename);
        }
    }
    private int getUserCountFromCSV() {
        int lastUserCount = 0;
        try {

            InputStream in = activity.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    int userCount = Integer.parseInt(parts[0]);

                    if (userCount > lastUserCount) {
                        lastUserCount = userCount;
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading users.csv: " + e.getMessage());
        }
        return lastUserCount;
    }


    public void createCSVFiles(Account account, File directory) {

        directory = activity.getFileStreamPath(filename).getParentFile();

        File playerDirectory = new File(directory, account.getUsername());

               if (!playerDirectory.exists()) {

            if (playerDirectory.mkdirs()) {
                Log.i(TAG, "Directory created successfully: " + playerDirectory.getAbsolutePath());
                createCSVFiles(account, playerDirectory);
            } else {
                Log.e(TAG, "Failed to create directory: " + playerDirectory.getAbsolutePath());
                return;
            }
        } else {
            Log.i(TAG, "Directory already exists: " + playerDirectory.getAbsolutePath());
        }

        try {

            File infoFile = new File(playerDirectory, "accountInfo.csv");
            File balanceFile = new File(playerDirectory, "accountBalance.csv");
            File incomeFile = new File(playerDirectory, "accountIncome.csv");
            File expenseFile = new File(playerDirectory, "accountExpense.csv");
            File quickAddFile = new File(playerDirectory, "accountQuickAdd.csv");
            File quickRemoveFile = new File(playerDirectory, "accountQuickRemove.csv");

            ensureFileCreated(infoFile);
            ensureFileCreated(balanceFile);
            ensureFileCreated(incomeFile);
            ensureFileCreated(expenseFile);
            ensureFileCreated(quickAddFile);
            ensureFileCreated(quickRemoveFile);

            appendToFile(infoFile, String.format("%d,%s,%s\n", account.getUserID(), account.getUsername(), account.getPassword()));
            appendToFile(balanceFile, String.format("%d,%s,%s\n", account.getUserID(), account.getBudget(), account.getBalance()));
            appendToFile(incomeFile, String.format("%d,%s,%s\n", account.getUserID(), account.getPay(), account.getTimeRate()));
            appendToFile(expenseFile, String.format("%d,%s,%s,%s\n", account.getUserID(), account.getCategory(), account.getRate(), account.getCost()));
            appendToFile(quickAddFile, String.format("%d,%s,%s\n", account.getUserID(), account.getTransactionNumber(), account.getAmount()));
            appendToFile(quickRemoveFile, String.format("%d,%s,%s,%s,%s\n", account.getUserID(), account.getTransactionNumber2(), account.getCategory2(), account.getItem(), account.getAmount2()));

            Log.i(TAG, "CSV files created successfully in directory: " + playerDirectory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to create CSV files in directory: " + playerDirectory.getAbsolutePath());
        }
    }
    private void ensureFileCreated(File file) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file: " + file.getAbsolutePath());
            }
        }
    }
    private void appendToFile(File file, String data) throws IOException {
        FileWriter fw = new FileWriter(file, true); // Append mode
        fw.write(data);
        fw.close();
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


    public void writeToCSV(Account account, File balanceFile) {

        try {
            FileWriter fwBalance = new FileWriter(balanceFile);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    static public void deleteAccount(String username) {
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
                        //deleteDirectory(accountDirectory);
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

    @NonNull
    public String toString(){
        return getUsername();
    }

}

