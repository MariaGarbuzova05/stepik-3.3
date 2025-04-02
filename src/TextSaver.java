import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextSaver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "user_input.txt"; // Name of the file to save the text.

        System.out.println("Enter text to save (type 'exit' to finish):");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String line;
            while (true) {
                line = scanner.nextLine();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                writer.write(line);
                writer.newLine(); // Add a new line character after each line.
            }
            System.out.println("Text saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}