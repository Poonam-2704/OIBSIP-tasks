import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATMSystem {
    private static Map<String, User> users = new HashMap<>();
    private User currentUser;

    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem();
        atmSystem.addDummyUsers();
        atmSystem.run();
    }

    private void addDummyUsers() {
        users.put("user1", new User("user1", "1234", "Poonam Suthar", 5000.0));
        users.put("user2", new User("user2", "2104", "Lokesh Suthar", 1000.0));
        users.put("user3", new User("user3", "1311", "Piyush Suthar", 2000.0));
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the ATM");
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (login(userId, pin)) {
                while (true) {
                    showMenu();
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            transactionHistory();
                            break;
                        case 2:
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            withdraw(withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            deposit(depositAmount);
                            break;
                        case 4:
                            System.out.print("Enter user ID to transfer to: ");
                            String toUserId = scanner.nextLine();
                            System.out.print("Enter amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            transfer(toUserId, transferAmount);
                            break;
                        case 5:
                            quit();
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else {
                System.out.println("Invalid user ID or PIN. Please try again.");
            }
        }
    }

    private boolean login(String userId, String pin) {
        User user = users.get(userId);
        if (user != null && user.getPin().equals(pin)) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    private void showMenu() {
        System.out.println("Welcome, " + currentUser.getName());
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    private void transactionHistory() {
        currentUser.printTransactionHistory();
    }

    private void withdraw(double amount) {
        if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            currentUser.withdraw(amount);
            System.out.println("Withdrawn: " + amount);
        }
    }

    private void deposit(double amount) {
        currentUser.deposit(amount);
        System.out.println("Deposited: " + amount);
    }

    private void transfer(String toUserId, double amount) {
        User toUser = users.get(toUserId);
        if (toUser == null) {
            System.out.println("Invalid user ID.");
        } else if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            currentUser.transfer(toUser, amount);
            System.out.println("Transferred: " + amount + " to " + toUserId);
        }
    }

    private void quit() {
        System.out.println("Thank you for using the ATM. Goodbye!");
        currentUser = null;
    }

    class User {
        private String userId;
        private String pin;
        private String name;
        private double balance;
        private ArrayList<Transaction> transactions;

        public User(String userId, String pin, String name, double initialBalance) {
            this.userId = userId;
            this.pin = pin;
            this.name = name;
            this.balance = initialBalance;
            this.transactions = new ArrayList<>();
        }

        public String getUserId() {
            return userId;
        }

        public String getPin() {
            return pin;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount));
        }

        public void withdraw(double amount) {
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount));
        }

        public void transfer(User toUser, double amount) {
            balance -= amount;
            toUser.deposit(amount);
            transactions.add(new Transaction("Transfer to " + toUser.getUserId(), amount));
            toUser.addTransaction(new Transaction("Transfer from " + userId, amount));
        }

        public void printTransactionHistory() {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }

        public void addTransaction(Transaction transaction) {
            transactions.add(transaction);
        }
    }

    class Transaction {
        private String type;
        private double amount;
        private java.util.Date date;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            this.date = new java.util.Date();
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "type='" + type + '\'' +
                    ", amount=" + amount +
                    ", date=" + date +
                    '}';
        }
    }
}
