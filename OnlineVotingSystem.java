import java.io.*;
import java.util.*;

class OnlineVotingSystem {
    static Scanner sc = new Scanner(System.in);
    static final String USER_FILE = "users.txt";
    static final String VOTE_FILE = "votes.txt";
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n ONLINE VOTING SYSTEM ");
            System.out.println("1. Register");
            System.out.println("2. Login & Vote");
            System.out.println("3. View Results");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginAndVote();
                    break;
                case 3:
                    displayResults();
                    break;
                case 4:
                    System.out.println("Thank you for using Online Voting System.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    //registration 
    static void registerUser() {
        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            if (userExists(username)) {
                System.out.println("User already exists!");
                return;
            }
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            FileWriter fw = new FileWriter(USER_FILE, true);
            fw.write(username + "," + password + ",false\n");
            fw.close();

            System.out.println("Registration successful!");

        } catch (IOException e) {
            System.out.println("Error during registration.");
        }
    }
    // login, voting
    static void loginAndVote() {
        try {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            File file = new File(USER_FILE);
            File temp = new File("temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

            String line;
            boolean authenticated = false;
            boolean alreadyVoted = false;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username) && data[1].equals(password)) {
                    authenticated = true;
                    if (data[2].equals("true")) {
                        alreadyVoted = true;
                    } else {
                        data[2] = "true";
                    }
                    bw.write(String.join(",", data) + "\n");
                } else {
                    bw.write(line + "\n");
                }
            }
            br.close();
            bw.close();
            file.delete();
            temp.renameTo(file);
            if (!authenticated) {
                System.out.println("Invalid login credentials!");
                return;
            }
            if (alreadyVoted) {
                System.out.println("You have already voted!");
                return;
            }
            castVote();
            System.out.println("Vote cast successfully!");
        } catch (IOException e) {
            System.out.println("Login error.");
        }
    }
    //voting
    static void castVote() {
        try {
            System.out.println("\nCandidates:");
            System.out.println("1. Candidate A");
            System.out.println("2. Candidate B");
            System.out.println("3. Candidate C");
            System.out.print("Enter your choice: ");

            int vote = sc.nextInt();
            String candidate = "";
            switch (vote) {
                case 1: candidate = "Candidate A"; break;
                case 2: candidate = "Candidate B"; break;
                case 3: candidate = "Candidate C"; break;
                default:
                    System.out.println("Invalid vote!");
                    return;
            }
            FileWriter fw = new FileWriter(VOTE_FILE, true);
            fw.write(candidate + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Voting error.");
        }
    }
    //result
    static void displayResults() {
        Map<String, Integer> results = new HashMap<>();
        try {
            File file = new File(VOTE_FILE);
            if (!file.exists()) {
                System.out.println("No votes cast yet.");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                results.put(line, results.getOrDefault(line, 0) + 1);
            }
            br.close();
            System.out.println("\n===== VOTING RESULTS =====");
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + " votes");
            }
        } catch (IOException e) {
            System.out.println("Error displaying results.");
        }
    }
    //CHECK USER EXISTS 
    static boolean userExists(String username) throws IOException {
        File file = new File(USER_FILE);
        if (!file.exists()) return false;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.split(",")[0].equals(username)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

}
