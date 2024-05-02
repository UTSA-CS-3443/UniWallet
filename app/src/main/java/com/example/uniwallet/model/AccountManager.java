package com.example.uniwallet.model;

import android.app.Activity;
import android.content.Context;
import android.icu.util.Output;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.uniwallet.SignUpActivity;

import org.intellij.lang.annotations.JdkConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * Manages user accounts including sign-up, account initialization, and budget calculation.
 * Implements Serializable interface to support serialization.
 */
public class AccountManager implements Serializable {
    ArrayList<Account> usernameList;
    private static final String ACCOUNTS_DIRECTORY = "files"; // Directory to store user account files
    private static final String accountsCSV = "users.csv"; // File name for user account CSV
    private static final String TAG = "Accounts"; // Log tag for debugging purposes

    private int userID; // List of user accounts
    private final String filename; // File name for user account
    private Activity activity;
    /**
     * Constructs an AccountManager object associated with the provided activity.
     *
     * @param activity The activity associated with the account manager.
     */
    public AccountManager(Activity activity){
        this.activity = activity;
        usernameList = new ArrayList<Account>();
        filename = "users.csv";
        //this.username = username;
        //this.password = password;
        //this.userID = 0;
    }
    /**
     * Checks if the provided username and password meet the sign-up criteria.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the parameters meet the criteria, false otherwise.
     */
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
    /**
     * Checks if the provided username exists in the username list.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */

    public boolean usernameExists(String username) {
        for (Account account : usernameList) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Signs up a new user with the provided username and password.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return The newly created account if sign-up is successful, null otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public Account signUp(String username, String password) throws IOException {

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

            account.setDefaultValues();

            usernameList.add(account);
            System.out.println(account.getBudget());
            System.out.println(account + " the directory has been created");


            return account;
        }
        return null;
    }
    /**
     * Generates the budget for the provided account based on income and expenses.
     *
     * @param account The account for which to generate the budget.
     * @return The calculated budget.
     */

    public double generateBudget(Account account) {
       // double balance = account.getBalance();
        double amount = 0;
        double income = 0;

        try {
            File userDirectory = getUserDirectory(account.getUsername());

            // INCOME
            String line;
                     File incomeFile = new File(userDirectory, "accountIncome.csv");
            BufferedReader reader = new BufferedReader(new FileReader(incomeFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    amount = Double.parseDouble(parts[1]);
                    income += amount;
                }
            }
//SAVINGS
            reader.close();
            File balanceFile = new File(userDirectory, "accountBalance.csv");
            reader = new BufferedReader(new FileReader(balanceFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    amount = Double.parseDouble(parts[3]);
                    double savingsDecimal = amount / 100;
                    double newIncome = income * savingsDecimal;
                    income -= newIncome;
                }
            }
            reader.close();
            // EXPENSES
            File expenseFile = new File(userDirectory, "accountExpense.csv");
             reader = new BufferedReader(new FileReader(expenseFile));
             while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    amount = Double.parseDouble(parts[3]);
                    income -= amount;
                }
            }
            reader.close();

            // Process accountQuickAdd.csv
            /*
            File quickAddFile = new File(userDirectory, "accountQuickAdd.csv");
            reader = new BufferedReader(new FileReader(quickAddFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                     amount = Double.parseDouble(parts[2]);
                    income += amount;
                }
            }
            reader.close();
*/
            // Process accountQuickRemove.csv
            File quickRemoveFile = new File(userDirectory, "accountQuickRemove.csv");
            reader = new BufferedReader(new FileReader(quickRemoveFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                     amount = Double.parseDouble(parts[4]);
                    income -= amount;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: Balance file not found for user: " + account.getUsername(), e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading balance file for user: " + account.getUsername(), e);
        }
        // Apply budget constraint

        return income;
    }
    /**
     * Retrieves the savings amount for the provided account from the file.
     *
     * @param account The account for which to retrieve savings.
     * @return The savings amount.
     */
      public double getSavingsFromFile(Account account) {
        double savings = 0.0;
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File balanceFile = new File(userDirectory, "accountBalance.csv");

            BufferedReader reader = new BufferedReader(new FileReader(balanceFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // && parts[0].equals(String.valueOf(account.getUserID()))
                if (parts.length >= 4) {
                    savings = Double.parseDouble(parts[3]);
                    account.setSavings(savings);
                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: Balance file not found for user: " + account.getUsername());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error reading balance file for user: " + account.getUsername());
            e.printStackTrace();
        }
        return savings;
    }
    /**
     * Retrieves the balance amount for the provided account from the file.
     *
     * @param account The account for which to retrieve the balance.
     * @return The balance amount.
     */
    public double getBalanceFromFile(Account account) {
        double balance = 0.0;
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File balanceFile = new File(userDirectory, "accountBalance.csv");

            BufferedReader reader = new BufferedReader(new FileReader(balanceFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID())
                if (parts.length >= 3 ) {
                    balance = Double.parseDouble(parts[2]);
                    account.setBalance(balance);
                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: Balance file not found for user: " + account.getUsername());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error reading balance file for user: " + account.getUsername());
            e.printStackTrace();
        }
        return balance;
    }
    /**
     * Updates the savings amount in the balance file for the provided account.
     *
     * @param account The account for which to update savings.
     * @param savings The new savings amount.
     */
    public void updateSavingsInFile(Account account, double savings){
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File balanceFile = new File(userDirectory, "accountBalance.csv");

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(balanceFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID()))
                if (parts.length >= 4) {
                    parts[3] = String.valueOf(savings); // Update balance value
                    account.setSavings(savings);
                }
                updatedLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter writer = new FileWriter(balanceFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();

            Log.i(TAG, "Savings updated successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error updating savings in file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Updates the balance amount in the balance file for the provided account.
     *
     * @param account The account for which to update the balance.
     * @param balance The new balance amount.
     */
    public void updateBalanceInFile(Account account, double balance) {
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File balanceFile = new File(userDirectory, "accountBalance.csv");

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(balanceFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID()))
                if (parts.length >= 4) {
                    parts[2] = String.valueOf(balance); // Update balance value
                    account.setBalance(balance);
                }
                updatedLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter writer = new FileWriter(balanceFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();

            Log.i(TAG, "Balance updated successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error updating balance in file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Updates the budget amount in the balance file for the provided account.
     *
     * @param account The account for which to update the budget.
     * @param budget The new budget amount.
     */
    public void updateBudgetInFile(Account account, double budget) {
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File balanceFile = new File(userDirectory, "accountBalance.csv");

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(balanceFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID())
                if (parts.length >= 4 ) {
                    parts[1] = String.valueOf(budget); // Update budget value
                    account.setBudget(budget);
                }
                updatedLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter writer = new FileWriter(balanceFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();

            Log.i(TAG, "Budget updated successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error updating savings in file for user: " + account.getUsername(), e);
        }

    }
    /**
     * Retrieves the total amount removed from funds for the provided category.
     *
     * @param account The account for which to retrieve removed funds.
     * @param category The category for which to retrieve removed funds.
     * @return The total amount removed from funds.
     */
    public double getRemoveFundsCategory(Account account, String category) {
        double totalAmount = 0.0;
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountQuickRemoveFile = new File(userDirectory, "accountQuickRemove.csv");

            BufferedReader reader = new BufferedReader(new FileReader(accountQuickRemoveFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[2].equals(category)) {
                    totalAmount += Double.parseDouble(parts[4]);
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading quick remove file for user: " + account.getUsername(), e);
        }
        return totalAmount;
    }
    /**
     * Retrieves the adjusted utility rate for the provided category and rate.
     *
     * @param account The account for which to retrieve the adjusted utility rate.
     * @param category The category for which to retrieve the adjusted utility rate.
     * @param rate The rate for which to retrieve the adjusted utility rate.
     * @param isCustom Indicates if the category is custom.
     * @return The adjusted utility rate.
     */
    public double getUtilityRateAdjusted(Account account, String category, String rate, boolean isCustom) {
        double adjustedRate = 0.0;
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountExpenseFile = new File(userDirectory, "accountExpense.csv");

            BufferedReader reader = new BufferedReader(new FileReader(accountExpenseFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1] != null && parts[1].equals(category)) {
                    String utilityRate = parts[3];
                    if (isCustom && "Custom".equals(parts[1])) {
                        switch (rate) {
                            case "Weekly":
                                adjustedRate += Double.parseDouble(parts[3]);
                                break;
                            case "Monthly":
                                adjustedRate += Double.parseDouble(parts[3]);
                                break;
                            case "Yearly":
                                adjustedRate += Double.parseDouble(parts[3]);
                                break;
                        }
                    } else if (!isCustom && !"Custom".equals(parts[1])) {
                        adjustedRate += Double.parseDouble(parts[3]);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading expense file for user: " + account.getUsername(), e);
        }
        return adjustedRate;
    }
    /**
     * Retrieves the utility rate for the provided category.
     *
     * @param account The account for which to retrieve the utility rate.
     * @param category The category for which to retrieve the utility rate.
     * @return The utility rate.
     */
    public String getUtilityRate(Account account, String category) {
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountExpenseFile = new File(userDirectory, "accountExpense.csv");

            BufferedReader reader = new BufferedReader(new FileReader(accountExpenseFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equals(category)) {
                    return parts[2];
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading expense file for user: " + account.getUsername(), e);
        }
        return null;
    }
    /**
     * Adds a new utility to the expense file for the provided account.
     *
     * @param account The account for which to add the utility.
     * @param category The category of the utility.
     * @param rate The rate of the utility.
     * @param cost The cost of the utility.
     * @param isCustom Indicates if the category is custom.
     */
    public void addUtility(Account account, String category, String rate, double cost, boolean isCustom) {
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountExpenseFile = new File(userDirectory, "accountExpense.csv");

            // Calculate adjusted cost based on payment frequency
           // calculateUtilityAmount(category, rate, cost, getLastPaymentDate(account, category));

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(accountExpenseFile));
            String line;
            boolean utilityFound = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (!isCustom && parts[1].equals(category)) {
                        utilityFound = true; // Utility of the same type already exists
                        // Update existing line with new values
                        parts[2] = rate;
                        parts[3] = String.valueOf(cost);
                        account.setRate(rate);
                        account.setCost(cost);
                    }
                    updatedLines.add(String.join(",", parts)); // Preserve existing or other lines
                }
            }
            reader.close();

            if (!utilityFound) {
                // Add new utility with adjusted cost
                String transactionLine = account.getUserID()  + "," + category + "," + rate + "," + cost + "\n";
                updatedLines.add(transactionLine);
            }

            FileWriter writer = new FileWriter(accountExpenseFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();

            Log.i(TAG, "Expense added successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error adding expense in file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Removes funds from the account for the provided category and item.
     *
     * @param account The account from which to remove funds.
     * @param category The category for which to remove funds.
     * @param item The item for which to remove funds.
     * @param amount The amount to remove.
     */
    public void removeFunds(Account account, String category, String item, double amount){
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountRemoveFile = new File(userDirectory, "accountQuickRemove.csv");

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(accountRemoveFile));
            if (!accountRemoveFile.exists()) {

                FileWriter headerWriter = new FileWriter(accountRemoveFile);
                headerWriter.write("UserID,TransactionNumber,Category,Item,Amount\n");
                headerWriter.write(account.getUserID() + ",1," + "," + category + "," + item + "," + amount + "\n");
                headerWriter.close();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        parts[2] = category;
                        parts[3] = item;
                        parts[4] = String.valueOf(amount);
                        account.setCategory(category);
                        account.setItem(item);
                        account.setAmount(amount);
                    }
                    updatedLines.add(String.join(",", parts));
                }
                reader.close();
            }

            FileWriter writer = new FileWriter(accountRemoveFile, true);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }


            int transactionNumber = Files.readAllLines(accountRemoveFile.toPath()).size() - 1;
            String transactionLine = account.getUserID() + "," + (transactionNumber + 1) + "," + category + "," + item + "," + amount + "\n";
            writer.write(transactionLine);

            writer.close();

            Log.i(TAG, "Budget updated successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error updating budget in file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Retrieves the paycheck amount from the income file for the provided account.
     *
     * @param account The account for which to retrieve the paycheck.
     * @return The paycheck amount.
     */
    public double getPaycheckFromFile(Account account){
        double paycheck = 0.0;
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File incomeFile = new File(userDirectory, "accountIncome.csv");

            BufferedReader reader = new BufferedReader(new FileReader(incomeFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID())
                if (parts.length >= 3 ) {
                    paycheck = Double.parseDouble(parts[1]);
                    account.setPay(paycheck);
                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: income file not found for user: " + account.getUsername());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error reading income file for user: " + account.getUsername());
            e.printStackTrace();
        }
        return paycheck;
    }
    /**
     * Adds a new paycheck to the income file for the provided account.
     *
     * @param account The account for which to add the paycheck.
     * @param paycheck The paycheck amount to add.
     * @param timeRate The time rate of the paycheck.
     */
    public void addPaycheck(Account account, double paycheck, String timeRate){
        try{
            File userDirectory = getUserDirectory(account.getUsername());
            File accountPaycheckFile = new File(userDirectory, "accountIncome.csv");

            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(accountPaycheckFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //&& parts[0].equals(String.valueOf(account.getUserID())
                if (parts.length >= 3 ) {
                    parts[1] = String.valueOf(paycheck); // Update budget value
                    parts[2] = timeRate;
                    account.setPay(paycheck);
                    account.setTimeRate(timeRate);

                }
                updatedLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter writer = new FileWriter(accountPaycheckFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }

            writer.close();

            Log.i(TAG, "Budget updated successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error updating budget in file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Adds money to the account and updates the corresponding file.
     *
     * @param account The account to which to add money.
     * @param amount The amount to add.
     */
    public void addMoney(Account account, double amount) {
        try {
            File userDirectory = getUserDirectory(account.getUsername());
            File accountAddFile = new File(userDirectory, "accountQuickAdd.csv");

            if (!accountAddFile.exists()) {

                FileWriter headerWriter = new FileWriter(accountAddFile);
                headerWriter.write("UserID,TransactionNumber,Amount\n");
                headerWriter.write(account.getUserID() + ",1,"  + "," + amount + "\n");
                headerWriter.close();
            } else {

                FileWriter writer = new FileWriter(accountAddFile, true);

                int transactionNumber = Files.readAllLines(accountAddFile.toPath()).size() - 1;
                writer.write(account.getUserID() + "," + (transactionNumber + 1)  + "," + amount + "\n");
                writer.close();
            }
            account.setAmount2(amount);
            Log.i(TAG, "Amount added successfully for user: " + account.getUsername());
        } catch (IOException e) {
            Log.e(TAG, "Error adding amount to file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Updates the password in the account files for the provided account.
     *
     * @param account The account for which to update the password.
     * @param newPassword The new password.
     */
    public void updatePasswordInFiles(Account account, String newPassword) {
        try {

            File userDirectory = getUserDirectory(account.getUsername());
            File accountInfoFile = new File(userDirectory, "accountInfo.csv");

            List<String> accountInfoLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(accountInfoFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // && parts[0].equals(String.valueOf(account.getUserID()))
                if (parts.length >= 3) {
                    parts[2] = newPassword; // Update the password value
                }
                accountInfoLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter accountInfoWriter = new FileWriter(accountInfoFile);
            for (String updatedLine : accountInfoLines) {
                accountInfoWriter.write(updatedLine + "\n");
            }
            accountInfoWriter.close();

            Log.i(TAG, "Password updated successfully in account info file for user: " + account.getUsername());

            File usersFile = new File(activity.getFilesDir(), "users.csv");

            List<String> usersLines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(usersFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(account.getUsername())) {
                    parts[2] = newPassword; // Update the password value
                }
                usersLines.add(String.join(",", parts));
            }
            reader.close();

            FileWriter usersWriter = new FileWriter(usersFile);
            for (String updatedLine : usersLines) {
                usersWriter.write(updatedLine + "\n");
            }
            usersWriter.close();

            Log.i(TAG, "Password updated successfully in account info file for user: " + account.getUsername() +
                    ", New password: " + newPassword); // Log the new password

        } catch (IOException e) {
            Log.e(TAG, "Error updating password in account info file for user: " + account.getUsername(), e);
        }
    }
    /**
     * Retrieves the directory for the provided username.
     *
     * @param username The username for which to retrieve the directory.
     * @return The directory corresponding to the username.
     */
    private File getUserDirectory(String username) {
        return new File(activity.getFilesDir(), username);
    }
    /**
     * Deletes the account from files and directories.
     *
     * @param account The account to delete.
     */
    public void deleteAccount(Account account) {
        File userDirectory = getUserDirectory(account.getUsername());
        File usersFile = new File(userDirectory.getParent(), "users.csv");

        try {
            List<String> lines = Files.readAllLines(usersFile.toPath());
            boolean accountFound = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].equals(account.getUsername()) && parts[2].equals(account.getPassword())) {
                    lines.remove(i);
                    Files.write(usersFile.toPath(), lines);
                    accountFound = true;
                    System.out.println("Account deleted successfully!");

                    if (userDirectory.exists()) {
                        deleteDirectory(userDirectory);
                        System.out.println("Account directory for " + account.getUsername() + " has been deleted.");
                    } else {
                        System.out.println("Account directory for " + account.getUsername() + " does not exist.");
                    }
                    break;
                }
            }
            if (!accountFound) {
                System.out.println("Failed to delete the account from users.csv: Account not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete the account from users.csv");
        }
    }
    /**
     * Recursively deletes a directory and its contents.
     *
     * @param directory The directory to delete.
     */
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
    /**
     * Logs in the user with the provided username and password.
     *
     * @param username The username to log in.
     * @param password The password associated with the username.
     * @return The logged-in account if successful, otherwise null.
     */
    public Account login(String username, String password) {
        try {
            File accountsFile = new File(activity.getFilesDir(), accountsCSV);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(accountsFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
                    bufferedReader.close();
                    Log.i(TAG, "Login successful for user: " + username);
                    return new Account(Integer.parseInt(parts[0]), username, password);
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error: Accounts file not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error reading accounts file: " + e.getMessage());
            e.printStackTrace();
        }
        Log.i(TAG, "Login failed: Incorrect username or password.");
        return null;
    }
    /**
     * Creates a directory for the provided username.
     *
     * @param username The username for which to create the directory.
     */
     private void createDirectory(String username) {

        File directory = activity.getFileStreamPath(filename).getParentFile();

        File userDirectory = new File(directory, username);

        if (!userDirectory.exists()) {

            if (userDirectory.mkdirs()) {
                Log.i(TAG, "Directory created successfully: " + userDirectory.getAbsolutePath());
            } else {
                Log.e(TAG, "Failed to create directory: " + userDirectory.getAbsolutePath());
            }
        } else {
            Log.i(TAG, "Directory already exists: " + userDirectory.getAbsolutePath());
        }
    }
    /**
     * Initializes files required for the user's account, such as CSV files for account information, balance, income, expenses, and quick add/remove transactions.
     * Also, ensures the creation of the user's directory if it doesn't exist.
     *
     * @param username The username of the account for which files need to be initialized.
     */
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
    /**
     * Adds a user account to the username list and creates necessary directories and CSV files for the account.
     *
     * @param account The account to be added.
     */
    public void addUser(Account account){
        if(usernameList != null){
            Log.i(TAG, "Adding " + account + " to the players");
            usernameList.add(account);
            createDirectory(account.getUsername());
            createCSVFiles(account, new File(activity.getFileStreamPath(filename).getParentFile(), account.getUsername()));
        }
    }
    /**
     * Saves the user account information to a CSV file.
     */
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
    /**
     * Retrieves the user count from the CSV file.
     *
     * @return The user count retrieved from the CSV file.
     */
    private int getUserCountFromCSV() {
        int lastUserCount = 0;
        try {

            InputStream in = activity.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].isEmpty()) {
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

    /**
     * Creates CSV files for the provided account in the specified directory.
     *
     * @param account   The account for which CSV files are to be created.
     * @param directory The directory where CSV files are to be created.
     */
     public void createCSVFiles(Account account, File directory) {

        directory = activity.getFileStreamPath(filename).getParentFile();

        File userDirectory = new File(directory, account.getUsername());

               if (!userDirectory.exists()) {

            if (userDirectory.mkdirs()) {
                Log.i(TAG, "Directory created successfully: " + userDirectory.getAbsolutePath());
                createCSVFiles(account, userDirectory);
            } else {
                Log.e(TAG, "Failed to create directory: " + userDirectory.getAbsolutePath());
                return;
            }
        } else {
            Log.i(TAG, "Directory already exists: " + userDirectory.getAbsolutePath());
        }

        try {

            File infoFile = new File(userDirectory, "accountInfo.csv");
            File balanceFile = new File(userDirectory, "accountBalance.csv");
            File incomeFile = new File(userDirectory, "accountIncome.csv");
            File expenseFile = new File(userDirectory, "accountExpense.csv");
            File quickAddFile = new File(userDirectory, "accountQuickAdd.csv");
            File quickRemoveFile = new File(userDirectory, "accountQuickRemove.csv");

            ensureFileCreated(infoFile);
            ensureFileCreated(balanceFile);
            ensureFileCreated(incomeFile);
            ensureFileCreated(expenseFile);
            ensureFileCreated(quickAddFile);
            ensureFileCreated(quickRemoveFile);


/*
            appendToFile(infoFile, String.format("%s,%s,%s\n", "UserID", "Username", "Password"));
            appendToFile(balanceFile, String.format("%s,%s,%s\n", "UserID", "Budget", "Balance"));
            appendToFile(incomeFile, String.format("%s,%s,%s\n", "UserID", "Pay", "Time Rate"));
            appendToFile(expenseFile, String.format("%s,%s,%s,%s\n", "UserID","Category", "Rate", "Cost"));
            appendToFile(quickAddFile, String.format("%s,%s,%s\n", "UserID", "Transaction Number", "Amount"));
            appendToFile(quickRemoveFile, String.format("%s,%s,%s,%s,%s\n", "UserID", "Transaction Number 2", "Category 2", "Item", "Amount 2"));
*/

            appendToFile(infoFile, String.format("%d,%s,%s\n", account.getUserID(), account.getUsername(), account.getPassword()));
            appendToFile(balanceFile, String.format("%d,%s,%s,%s\n", account.getUserID(), account.getBudget(), account.getBalance(), account.getSavings()));
            appendToFile(incomeFile, String.format("%d,%s,%s\n", account.getUserID(), account.getPay(), account.getTimeRate()));
            appendToFile(expenseFile, String.format("%d,%s,%s,%s\n", account.getUserID(), account.getCategory(), account.getRate(), account.getCost()));
            appendToFile(quickAddFile, String.format("%d,%s,%s\n", account.getUserID(), account.getTransactionNumber(), account.getAmount()));
            appendToFile(quickRemoveFile, String.format("%d,%s,%s,%s,%s\n", account.getUserID(), account.getTransactionNumber2(), account.getCategory2(), account.getItem(), account.getAmount2()));

            Log.i(TAG, "CSV files created successfully in directory: " + userDirectory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to create CSV files in directory: " + userDirectory.getAbsolutePath());
        }
    }
    /**
     * Ensures that the provided file exists; if not, creates the file.
     *
     * @param file The file to be ensured.
     * @throws IOException If an I/O error occurs while ensuring the file.
     */
    private void ensureFileCreated(File file) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file: " + file.getAbsolutePath());
            }
        }
    }
    /**
     * Appends data to the specified file.
     *
     * @param file The file to which data is to be appended.
     * @param data The data to be appended to the file.
     * @throws IOException If an I/O error occurs while appending to the file.
     */
    private void appendToFile(File file, String data) throws IOException {
        FileWriter fw = new FileWriter(file, true); // Append mode
        fw.write(data);
        fw.close();
    }

    /*
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
*/
}

