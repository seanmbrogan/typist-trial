import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.time.temporal.TemporalAccessor;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 

public class TypistTrial
{
    static Scanner sc;
    static String[] fullwords;
    
    static {
        TypistTrial.sc = new Scanner(System.in);
        TypistTrial.fullwords = new String[1000];
    }
    
    public static void main(final String[] args) {
        TypistTrial.fullwords = initializewords();
        start("Start Playing? (y/n/options)");
    }
    
    public static String[] initializewords() {
        final String[] words = new String[1000];
        try {
            final File filewords = new File("words.txt");
            final Scanner filereader = new Scanner(filewords);
            for (int i = 0; i < 1000; ++i) {
                words[i] = filereader.next();
            }
            filereader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }
    
    public static File initializescores() {
        try {
            final File filescores = new File("scores.txt");
            filescores.createNewFile();
            return filescores;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void writeScores(final int totalWords, final double score, final String strdatetime) {
        try {
            final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(initializescores(), true)));
            final String output = "Total words: " + totalWords + " | Avg time: " + score + " seconds | Date: " + strdatetime;
            out.append(output);
            out.println();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getDateTime() {
        final ZonedDateTime rawdatetime = ZonedDateTime.now();
        final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final String dateTime = Formatter.format(rawdatetime);
        return dateTime;
    }
    
    public static void start(final String userprompt) {
        System.out.println(userprompt);
        final String answer = TypistTrial.sc.next();
        if (answer.equals("y") || answer.equals("Y")) {
            game();
        }
        else if (answer.equals("n") || answer.equals("N")) {
            start("Change your mind? (y/n)");
        }
        else if (answer.equals("options") || answer.equals("OPTIONS")) {
            options();
        }
        else {
            start("Enter a correct response. (y/n)");
        }
    }
    
    public static void game() {
        System.out.println("Type what you see.");
        double[] times = { 0.0 };
        boolean success;
        do {
            cls();
            final double startTime = System.currentTimeMillis() / 1000.0;
            final int next = (int)Math.round(Math.random() * 1000.0);
            final String nextword = TypistTrial.fullwords[next];
            System.out.println(nextword);
            final String input = TypistTrial.sc.next();
            if (input.equals(nextword)) {
                final double endTime = System.currentTimeMillis() / 1000.0;
                success = true;
                final double elapsed = endTime - startTime;
                final double roundElapsed = Math.round(elapsed * 100.0) / 100.0;
                System.out.println(roundElapsed);
                times[times.length - 1] = roundElapsed;
                times = Arrays.copyOf(times, times.length + 1);
            }
            else {
                success = false;
                final int totalWords = times.length - 1;
                final double average = avg(times);
                writeScores(totalWords, average, getDateTime());
                start("Oops! Start over? (y/n/options)\nYour total words were: " + totalWords + "\nYour average response time was: " + average + " seconds");
            }
        } while (success = true);
    }
    
    public static double avg(final double[] times) {
        double average = 0.0;
        final double arrSize = times.length - 1;
        for (int i = 0; i < arrSize; ++i) {
            average += times[i];
        }
        average /= arrSize;
        average = Math.round(average * 100.0) / 100.0;
        return average;
    }
    
    public static void cls() {
        try {
            new ProcessBuilder(new String[] { "cmd", "/c", "cls" }).inheritIO().start().waitFor();
        }
        catch (Exception E) {
            System.out.println(E);
        }
    }
    
    public static void clearScores() {
        try {
            final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(initializescores(), false)));
            out.println();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void printScores() {
        try {
            final BufferedReader in = new BufferedReader(new FileReader(initializescores()));
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                System.out.println(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void options() {
        System.out.println("Clear Saved Scores? (clear) \nView Saved Scores (view) \nResume Playing? (back)");
        final String answer = TypistTrial.sc.next();
        if (answer.equals("view") || answer.equals("VIEW")) {
            printScores();
        }
        else if (answer.equals("clear") || answer.equals("CLEAR")) {
            clearScores();
        }
        else if (answer.equals("back") || answer.equals("BACK")) {
            start("Start Playing? (y/n/options)");
        }
        else {
            options();
        }
        start("Resume Playing? (y/n/options");
    }
}