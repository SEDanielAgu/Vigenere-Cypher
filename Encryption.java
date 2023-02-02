import java.io.BufferedReader;
import java.lang.*;
import java.util.Scanner;

public class Encryption {

    String input;
    String output;
    String command;
    String response;
    String argument;
    String password;

    public Encryption() {
        input = "";
        output = "";
        command = "";
        argument = "";
        password = "";
        response = "";
    }

    public void parseCommand(String entry) {
        input = entry;
        String parsedCommand[] = input.split(" ", 2);
        command = parsedCommand[0].trim();
        argument = parsedCommand[1].trim();
        input = command + " " + argument;
        input = input.toUpperCase();

        if (command.equalsIgnoreCase("passkey")) {
            passkey(argument);
        } else if (command.equalsIgnoreCase("encrypt")) {
            encrypt(argument);
        } else if (command.equalsIgnoreCase("decrypt")) {
            decrypt(argument);
        } else if (command.equalsIgnoreCase("quit")) {
            quit();
        }
    }

    public void passkey(String arg) {
        password = arg.toUpperCase();
        result("SUCCESS");
    }

    public void encrypt(String arg) {
        if (password.equals("")) {
            error("Password not set");
        } else if (arg.equals("")) {
            error("Invalid string");
        } else {
            arg = arg.toUpperCase();
            char passKey[] = new char[arg.length()];
            char encrypted[] = new char[arg.length()];
            for (int i = 0, keyIndex = 0; i < arg.length(); i++, keyIndex++) {
                if (keyIndex == password.length()) {
                    keyIndex = 0;
                }
                passKey[i] = password.charAt(keyIndex);
            }

            for (int i = 0, j = 0; i < arg.length(); i++, j++) {
                if (Character.isLetter(arg.charAt(i))) {
                    int temp = (((arg.charAt(i) + passKey[j]) % 26) + 'A');
                    encrypted[i] = (char) temp;
                } else {
                    encrypted[i] = arg.charAt(i);
                    j--;
                }
            }

            response = "";
            for (int i = 0; i < encrypted.length; i++) {
                response += encrypted[i] + "";
            }
            result(response);
        }
    }

    public void decrypt(String arg) {
        if (password.equals("")) {
            error("Password not set");
        } else if (arg.equals("")) {
            error("Invalid string");
        } else {
            arg = arg.toUpperCase();
            char passKey[] = new char[arg.length()];
            char decrypted[] = new char[arg.length()];
            for (int i = 0, keyIndex = 0; i < arg.length(); i++, keyIndex++) {
                if (keyIndex == password.length()) {
                    keyIndex = 0;
                }
                passKey[i] = password.charAt(keyIndex);
            }

            for (int i = 0, j = 0; i < arg.length(); i++, j++) {
                if (Character.isLetter(arg.charAt(i))) {
                    decrypted[i] = (char) ((((arg.charAt(i) - passKey[j]) + 26) % 26) + 'A');
                } else {
                    decrypted[i] = arg.charAt(i);
                    j--;
                }
            }

            response = "";
            for (int i = 0; i < decrypted.length; i++) {
                response += decrypted[i] + "";
            }
            result(response);
        }
    }

    public void quit() {
        System.exit(0);
    }

    public void result(String arg) {
        output = ("RESULT " + arg);
    }

    public void error(String arg) {
        output = ("ERROR " + arg);
    }

    public static void main(String[] args) {
        Encryption runner = new Encryption();
        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.nextLine();
            runner.parseCommand(command);
            System.out.println(runner.output);
        }
    }
}
