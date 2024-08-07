import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bank {
    static Map<String, AccountData> AllUsers = new HashMap<>();
    static Scanner stdin = new Scanner(System.in);
    static final String GREEN = "\u001B[32m", RED = "\u001B[31m", BLUE = "\u001B[34m", RESET = "\u001B[0m";

    public static void MainMenu() {
        try {
            // Pre-populate with some users
            AccountData obj1 = new AccountData("user1", "pass1", 1000);
            AccountData obj2 = new AccountData("user2", "pass2", 2000);
            AccountData obj3 = new AccountData("user3", "pass3", 3000);
            AllUsers.put(obj1.getUsername(), obj1);
            AllUsers.put(obj2.getUsername(), obj2);
            AllUsers.put(obj3.getUsername(), obj3);
        } catch (Exception e) {
            println(RED + "Error in user creation: " + e.getMessage() + RESET);
            return;
        }

        while (true) {
            print("Would you like to " + BLUE + "create" + RESET + " an account or " + BLUE + "login" + RESET + " to an existing account? You can enter 'XXX' to quit. ");
            String choice = stdin.nextLine().trim().toLowerCase();

            switch (choice) {
                case "create":
                    CreateNewUser();
                    break;
                case "login":
                    LogInCheck();
                    break;
                case "listall":
                    ListAllUsers();
                    break;
                case "xxx":
                    println("Goodbye!");
                    return;
                default:
                    println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void LoggedInMenu(String user) {
        while (true) {
            print("\nCurrently logged in as " + BLUE + user + RESET + ". Would you like to...\n"
                    + BLUE + "1" + RESET + ": Check Balance\n" + BLUE + "2" + RESET + ": Add Funds to Balance\n"
                    + BLUE + "3" + RESET + ": Send Money\n" + BLUE + "4" + RESET + ": Change Password\n"
                    + BLUE + "5" + RESET + ": Change Username\n" + BLUE + "6" + RESET + ": Delete Account\n"
                    + BLUE + "7" + RESET + ": Logout\nEnter choice: ");
            String LoggedInput = stdin.nextLine();
            switch (LoggedInput) {
                case "1":
                    println("\nAccount " + user + "'s balance is " + GREEN + "$" + format(AllUsers.get(user).getBalance()) + RESET + ".");
                    break;
                case "2":
                    AddFunds(user);
                    break;
                case "3":
                    SendMoney(user);
                    break;
                case "4":
                    ChangePassword(user);
                    break;
                case "5":
                    ChangeUsername(user);
                    break;
                case "6":
                    DeleteAccount(user);
                    return;
                case "7":
                    println("Logging out...\nLogout successful.");
                    return;
                default:
                    println("\nPlease enter a number from the options above.");
                    break;
            }
        }
    }

    private static void LogInCheck() {
        print("Enter your account's username: ");
        String CheckUser = stdin.nextLine().trim();
        if (!AllUsers.containsKey(CheckUser)) {
            println("Sorry, no such account exists. Try again.");
            return;
        }
        print("Enter your password: ");
        String CheckPass = stdin.nextLine();
        try {
            if (AllUsers.get(CheckUser).verifyPassword(CheckPass)) {
                println(GREEN + "Login successful." + RESET);
                LoggedInMenu(CheckUser);
            } else {
                println(RED + "Incorrect password. " + RESET);
            }
        } catch (Exception e) {
            println(RED + "Error during login: " + e.getMessage() + RESET);
        }
    }

    private static void CreateNewUser() {
        String NewUser, NewPassword;
        double NewBalance;

        while (true) {
            print("Enter a username, or '~' to quit: ");
            NewUser = stdin.nextLine().trim();
            if (NewUser.equals("~")) {
                println("Quitting.");
                return;
            }
            if (NewUser.isEmpty()) {
                println("Please enter a username.");
            } else if (AllUsers.containsKey(NewUser)) {
                println("Username already exists. Please choose another one.");
            } else break;
        }

        print("Great, now create a password, or '~' to quit: ");
        while (true) {
            NewPassword = stdin.nextLine();
            if (NewPassword.equals("~")) {
                println("Quitting.");
                return;
            }
            if (NewPassword.isEmpty()) {
                println("Please create a password.");
            } else break;
        }

        while (true) {
            try {
                print("Enter your balance, or '~' to quit: ");
                String TempBalance = stdin.nextLine().trim();
                if (TempBalance.equals("~")) {
                    println("Exiting...");
                    return;
                }
                NewBalance = Double.parseDouble(TempBalance);
                break;
            } catch (NumberFormatException e) {
                println("Invalid input. Please enter a valid number.");
            }
        }

        try {
            AccountData Account = new AccountData(NewUser, NewPassword, NewBalance);
            AllUsers.put(Account.getUsername(), Account);
            println(GREEN + "Success! Your account has been created." + RESET);
            println("Username: " + BLUE + Account.getUsername() + RESET);
            println("Balance: " + BLUE + "$" + format(Account.getBalance()) + RESET);
        } catch (Exception e) {
            println(RED + "Error in account creation: " + e.getMessage() + RESET);
        }
    }

    private static void ListAllUsers() {
        println("\nTotal number of current registered accounts: " + AccountData.TotalUsers);
        for (Map.Entry<String, AccountData> RegisteredUser : AllUsers.entrySet()) {
            println("Username: " + RegisteredUser.getValue().getUsername());
            println("Balance: $" + format(RegisteredUser.getValue().getBalance()) + "\n");
        }
    }

    private static void SendMoney(String sender) {
        print("Enter the name of the recipient: ");
        String recipient = stdin.nextLine().trim();
        if (!AllUsers.containsKey(recipient)) {
            println(RED + "\nError: Recipient does not exist." + RESET);
            return;
        } else if (recipient.equals(sender)) {
            println(RED + "\nYou can't send money to yourself!" + RESET);
            return;
        }

        print("Enter the amount to send: ");
        double amount;
        try {
            String AmountTemp = stdin.nextLine().trim();
            amount = Double.parseDouble(AmountTemp);
            if (amount <= 0) {
                println(RED + "\nError: Amount must be greater than zero." + RESET);
                return;
            }
            if (amount > AllUsers.get(sender).getBalance()) {
                println(RED + "\nError: Insufficient funds." + RESET);
                return;
            }
            print(BLUE + "Are you sure you'd like to send $" + format(amount) + " to " + recipient + "? (yes/no) " + RESET);
            String sendConfirmation = stdin.nextLine().trim();
            if (!sendConfirmation.equalsIgnoreCase("yes")) {
                println(GREEN + "\nTransaction cancelled successfully." + RESET);
                return;
            }
            AllUsers.get(sender).setBalance(AllUsers.get(sender).getBalance() - amount);
            AllUsers.get(recipient).setBalance(AllUsers.get(recipient).getBalance() + amount);
            println(GREEN + "\nSuccess! $" + format(amount) + " sent to " + recipient + ".\n" + RESET + "Your new balance is " + BLUE + "$" + format(AllUsers.get(sender).getBalance()) + RESET + ".");
        } catch (NumberFormatException e) {
            println(RED + "\nError: Invalid amount entered." + RESET);
        }
    }

    private static void AddFunds(String user) {
        while (true) {
            try {
                print("Enter amount to add, or 'xxx' to quit: ");
                String AddedFundsTemp = stdin.nextLine().trim();
                if (AddedFundsTemp.equalsIgnoreCase("xxx")) {
                    break;
                }
                double AddedAmount = Double.parseDouble(AddedFundsTemp);
                AllUsers.get(user).setBalance(AllUsers.get(user).getBalance() + AddedAmount);
                println(GREEN + "\n$" + format(AddedAmount) + RESET + " has been added to your account! Your new balance is $" + BLUE + format(AllUsers.get(user).getBalance()) + RESET + ".");
                break;
            } catch (NumberFormatException e) {
                println("\nOops, please only enter a valid number.");
            }
        }
    }

    private static void ChangePassword(String user) {
        print("Enter a new password, or nothing to quit: ");
        String NewPassword = stdin.nextLine();
        if (NewPassword.isEmpty()) return;
        try {
            AllUsers.get(user).setPassword(NewPassword);
            println("\nAccount " + user + "'s password has been changed.");
        } catch (Exception e) {
            println(RED + "Error in changing password: " + e.getMessage() + RESET);
        }
    }

    private static void ChangeUsername(String user) {
        print("Enter a new username, or nothing to quit: ");
        String NewUsername = stdin.nextLine().trim();
        if (NewUsername.isEmpty()) return;

        if (AllUsers.containsKey(NewUsername)) {
            println(RED + "\nSorry, the username already exists." + RESET);
        } else {
            AccountData temp = AllUsers.remove(user);
            temp.setUsername(NewUsername);
            AllUsers.put(NewUsername, temp);
            println(GREEN + "\nYour username has been changed to " + NewUsername + "." + RESET);
        }
    }

    private static void DeleteAccount(String user) {
        print(BLUE + "\nAre you sure you want to delete your account? (yes/no) " + RESET);
        String confirmation = stdin.nextLine().trim();
        if (confirmation.equalsIgnoreCase("yes")) {
            AllUsers.remove(user);
            AccountData.TotalUsers--;
            println(RED + "\nYour account has been deleted." + RESET);
        } else {
            println(GREEN + "\nAccount deletion cancelled." + RESET);
        }
    }

    // these following three functions are just to improve code readability by making the function names shorter
    private static String format(double number) {
        return String.format("%.2f", number);
    }

    public static void println(String input) {
        System.out.println(input);
    }

    public static void print(String input) {
        System.out.print(input);
    }

}
