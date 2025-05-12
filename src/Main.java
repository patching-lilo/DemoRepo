//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }asdasd
    }
}import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // === Registration ===
        System.out.println("=== Register a New User ===");

        System.out.print("Enter username (must include '_' and be max 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 chars, 1 uppercase, 1 number, 1 special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter cell number (+27XXXXXXXXX): ");
        String cellNumber = scanner.nextLine();

        Login login = new Login(username, password, firstName, lastName, cellNumber);
        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        if (!registrationResult.equals("User has been registered successfully.")) {
            System.out.println("Exiting program due to invalid registration.");
            return;
        }

        // === Login ===
        System.out.println("\n=== Log In ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        if (!login.loginUser(loginUsername, loginPassword)) {
            System.out.println("Username or password incorrect. Exiting...");
            return;
        }

        System.out.println("Welcome to QuickChat!");

        // === Message Menu ===
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    // Send message
                    System.out.print("Enter recipient cell number (+27XXXXXXXXX): ");
                    String recipient = scanner.nextLine();
                    if (!Message.checkRecipientCell(recipient)) {
                        System.out.println("Invalid recipient number.");
                        break;
                    }

                    System.out.print("Enter your message (max 250 chars): ");
                    String content = scanner.nextLine();
                    if (content.length() > 250) {
                        System.out.println("Please enter a message of less than 250 characters.");
                        break;
                    }

                    // Create and store the message
                    Message newMessage = new Message(recipient, content);
                    System.out.println("Message sent successfully!");
                    break;

                case 2:
                    // Show messages
                    System.out.println("Recently sent messages:");
                    Message.printMessages();
                    break;

                case 3:
                    System.out.println("Exiting. Total messages sent: " + Message.returnTotalMessages());
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {
    private static int messageCount = 0;
    private static List<String> messageHistory = new ArrayList<>();

    private String messageID;
    private String recipient;
    private String content;
    private String messageHash;

    public Message(String recipient, String content) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.content = content;
        this.messageHash = createMessageHash();
        messageCount++;
        storeMessage();
    }

    // Generates a random 10-digit ID
    private String generateMessageID() {
        Random rand = new Random();
        long num = 1000000000L + (long)(rand.nextDouble() * 8999999999L);
        return String.valueOf(num);
    }

    // Check if the Message ID is valid (max 10 chars)
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Check recipient number format
    public static boolean checkRecipientCell(String number) {
        return number != null && number.trim().matches("\\+27\\d{9}");
    }

    // Create hash using first 2 of ID, number of messages, and first+last words
    public String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        return messageID.substring(0, 2) + ":" + messageCount + ":" + firstWord + lastWord;
    }

    // User chooses to send/store/disregard message
    public static boolean sendMessageOption(String option) {
        return option.equals("1"); // Only "Send" adds it to list
    }

    // Display all sent messages
    public static void printMessages() {
        if (messageHistory.isEmpty()) {
            System.out.println("No messages have been sent.");
        } else {
            for (String msg : messageHistory) {
                System.out.println(msg);
                System.out.println("-------------------------");
            }
        }
    }

    // Return count of messages
    public static int returnTotalMessages() {
        return messageCount;
    }

    // Store message in list (you'll eventually convert this to store in JSON)
    private void storeMessage() {
        String msg = "Message ID: " + messageID +
                "\nMessage Hash: " + messageHash +
                "\nRecipient: " + recipient +
                "\nMessage: " + content;
        messageHistory.add(msg);
    }
}

public class User {
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;

    public User(String username, String password, String cellNumber, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

public class Login {
    private User registeredUser;
    private String enteredUsername;
    private String enteredPassword;

    // Constructor to register a user
    public Login(String username, String password, String firstName, String lastName, String cellNumber) {
        // Fixed parameter order to match User class constructor
        this.registeredUser = new User(username, password, cellNumber, firstName, lastName);
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean isLongEnough = password.length() >= 8;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return isLongEnough && hasUppercase && hasNumber && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber(String number) {
        // Expecting format: +27 followed by exactly 9 digits
        return number != null && number.trim().matches("\\+27\\d{9}");
    }

    // Validate and register the user
    public String registerUser() {
        String username = registeredUser.getUsername();
        String password = registeredUser.getPassword();
        String cell = registeredUser.getCellNumber();

        System.out.println("DEBUG: Username = [" + username + "]");
        System.out.println("DEBUG: Password = [" + password + "]");
        System.out.println("DEBUG: Cell number = [" + cell + "]");

        if (!checkUserName(username)) {
            return "Username is incorrectly formatted.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password does not meet the complexity requirements.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number is incorrectly formatted.";
        }

        return "User has been registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        this.enteredUsername = username;
        this.enteredPassword = password;

        return enteredUsername.equals(registeredUser.getUsername()) &&
                enteredPassword.equals(registeredUser.getPassword());
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + registeredUser.getFirstName() + " " + registeredUser.getLastName() + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Optional setter if needed
    public void setRegisteredUser(User user) {
        this.registeredUser = user;
    }
}
