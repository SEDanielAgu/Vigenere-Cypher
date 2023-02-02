import java.io.*;
import java.util.Scanner;
import java.lang.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Driver {

    ArrayList<String> history = new ArrayList<>();

    public String viewHistory(){
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to use the history? (Y/N)");
        if (input.nextLine().equalsIgnoreCase("y")) {
            System.out.println("HISTORY: ");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
            }
            System.out.println((history.size() + 1) + ". (Go Back)");
            String choice = input.nextLine();
            if (Integer.parseInt(choice) == (history.size() + 1)) {
                return "";
            } else {
                return history.get(Integer.parseInt(choice) - 1);
            }
        }
        return "";
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Process encryption;
        InputStream encInStream;
        OutputStream encOutStream;
        Scanner fromEnc;
        PrintStream toEnc;

        Process logger;
        OutputStream logOutStream;
        PrintStream toLog;

        logger = Runtime.getRuntime().exec("javac -cp src src\\Logger.java");
        logger = Runtime.getRuntime().exec("java -cp src Logger logfile.txt");
        logOutStream = logger.getOutputStream();
        toLog = new PrintStream(logOutStream);

        encryption = Runtime.getRuntime().exec("javac -cp src src\\Encryption.java");
        encryption = Runtime.getRuntime().exec("java -cp src Encryption");
        encInStream = encryption.getInputStream();
        encOutStream = encryption.getOutputStream();
        fromEnc = new Scanner(encInStream);
        toEnc = new PrintStream(encOutStream);

        Scanner input = new Scanner(System.in);
        Driver runner = new Driver();

        while (true) {
            System.out.println("MENU: ");
            System.out.println("password - set the password for encryption/decryption");
            System.out.println("encrypt  - encrypt a string");
            System.out.println("decrypt  - decrypt a string");
            System.out.println("history  - show history");
            System.out.println("quit     - quit program\n");
            System.out.println("Enter Command: ");

            String command = input.nextLine();

            if (command.equalsIgnoreCase("password")) {
                String password = runner.viewHistory();
                if (password.equals("")) {
                    System.out.println("Enter Password:");
                    password = input.nextLine();
                    runner.history.add(password.toUpperCase());
                }

                toEnc.println("PASSKEY " + password);
                toLog.println("[SET_PASSKEY] " + password);
                toLog.flush();
                toEnc.flush();
                String output = fromEnc.nextLine();

                String parsedOutput[] = output.split(" ", 2);
                toLog.println("[SET_PASSKEY] " + parsedOutput[1]);
                toLog.flush();

                System.out.println(output + "\n");
            } else if (command.equalsIgnoreCase("encrypt")) {
                String toEncrypt = runner.viewHistory();
                if (toEncrypt == "") {
                    System.out.println("Enter String to ENCRYPT:");
                    toEncrypt = input.nextLine();
                    runner.history.add(toEncrypt.toUpperCase());
                }

                toEnc.println("ENCRYPT " + toEncrypt);
                toLog.println("[ENCRYPT] " + toEncrypt);
                toLog.flush();
                toEnc.flush();
                String output = fromEnc.nextLine();

                String parsedOutput[] = output.split(" ", 2);
                runner.history.add(parsedOutput[1]);
                toLog.println("[ENCRYPT] " + output);
                toLog.flush();

                System.out.println(output + "\n");
            } else if (command.equalsIgnoreCase("decrypt")) {
                String toDecrypt = runner.viewHistory();
                if (toDecrypt == "") {
                    System.out.println("Enter String to DECRYPT:");
                    toDecrypt = input.nextLine();
                    runner.history.add(toDecrypt.toUpperCase());
                }

                toEnc.println("DECRYPT " + toDecrypt);
                toLog.println("[DECRYPT] " + toDecrypt);
                toLog.flush();
                toEnc.flush();
                String output = fromEnc.nextLine();

                String parsedOutput[] = output.split(" ", 2);
                runner.history.add(parsedOutput[1]);
                toLog.println("[DECRYPT] " + output);
                toLog.flush();

                System.out.println(output + "\n");
            } else if (command.equalsIgnoreCase("history")) {
                System.out.println("HISTORY: \n");
                for (int i = 0; i < runner.history.size(); i++) {
                    System.out.println((i + 1) + ". " + runner.history.get(i));
                }
                System.out.println();
            } else if (command.equalsIgnoreCase("quit")) {
                toLog.println("QUIT");
                toEnc.println("QUIT");
                toLog.flush();
                toEnc.flush();
                System.out.println("Goodbye");
                System.exit(0);
            } else {
                System.out.println("Incorrect Command Try Again!");
            }
        }
    }
}
