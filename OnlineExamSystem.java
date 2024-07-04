import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineExamSystem {
    private static Map<String, User> users = new HashMap<>();
    private static final int TIMER_DURATION = 30; 
    private User currentUser;
    private boolean isExamRunning;
    private Timer timer;

    public static void main(String[] args) {
        OnlineExamSystem system = new OnlineExamSystem();
        system.addDummyUsers();
        system.run();
    }

    private void addDummyUsers() {
        users.put("user1", new User("user1", "1234", "Dinesh"));
        users.put("user2", new User("user2", "5678", "Riya"));
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Online Exam System");
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            if (login(userId, pin)) {
                while (true) {
                    showMenu();
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); 
                    switch (choice) {
                        case 1:
                            updateProfile(scanner);
                            break;
                        case 2:
                            updatePassword(scanner);
                            break;
                        case 3:
                            startExam(scanner);
                            break;
                        case 4:
                            logout();
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
        System.out.println("1. Update Profile");
        System.out.println("2. Update Password");
        System.out.println("3. Start Exam");
        System.out.println("4. Logout");
    }

    private void updateProfile(Scanner scanner) {
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        currentUser.setName(newName);
        System.out.println("Profile updated successfully.");
    }

    private void updatePassword(Scanner scanner) {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        currentUser.setPin(newPassword);
        System.out.println("Password updated successfully.");
    }

    private void startExam(Scanner scanner) {
        if (isExamRunning) {
            System.out.println("Exam is already running.");
            return;
        }

        System.out.println("Starting exam...");
        isExamRunning = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSubmit();
            }
        }, TIMER_DURATION * 1000);

        for (int i = 1; i <= 3; i++) { // Example:
            System.out.println("Question " + i + ": What is the capital of India?");
            System.out.println("1. Mumbai");
            System.out.println("2. Delhi");
            System.out.println("3. Hyderabad");
            System.out.print("Your answer: ");
            int answer = scanner.nextInt();
            currentUser.submitAnswer(i, answer);
        }

        submitExam();
    }

    private void submitExam() {
        if (isExamRunning) {
            timer.cancel();
            System.out.println("Exam submitted successfully.");
            isExamRunning = false;
            currentUser.printAnswers();
        }
    }

    private void autoSubmit() {
        System.out.println("Time is up! Auto-submitting the exam...");
        submitExam();
    }

    private void logout() {
        if (isExamRunning) {
            autoSubmit();
        }
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    class User {
        private String userId;
        private String pin;
        private String name;
        private Map<Integer, Integer> answers;

        public User(String userId, String pin, String name) {
            this.userId = userId;
            this.pin = pin;
            this.name = name;
            this.answers = new HashMap<>();
        }

        public String getUserId() {
            return userId;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void submitAnswer(int questionNumber, int answer) {
            answers.put(questionNumber, answer);
        }

        public void printAnswers() {
            System.out.println("Your answers:");
            for (Map.Entry<Integer, Integer> entry : answers.entrySet()) {
                System.out.println("Question " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
