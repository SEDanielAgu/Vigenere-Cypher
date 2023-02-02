import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Logger {

    String filename;

    public Logger(){
        filename = "log.txt";
        File logFile = new File(filename);
    }

    public Logger(String name) {
        filename = name;
    }

    public void writeToFile(String message) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filename,true));
        pw.append(message);
        pw.println();
        pw.close();
    }

    public String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return (dtf.format(date) + " ");
    }

    public void quit() throws IOException {
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        Logger runner = new Logger(args[0]);
        Scanner input = new Scanner(System.in);
        runner.writeToFile((runner.getTimeStamp()) + "[START] Logging Started");
        while (true) {
            String command = input.nextLine();
            if (command.equalsIgnoreCase("QUIT")) {
                runner.writeToFile((runner.getTimeStamp())  + "[STOPPED] Logging Stopped");
                runner.quit();
            }
            String timeStamp = runner.getTimeStamp();
            runner.writeToFile(timeStamp + command);
        }
    }
}
