import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {
    public static VocabList vocabList = new VocabList();
    public static void main(String[] args)
    {
        showMainMenu();
    }
    static void showMainMenu()
    {
        System.out.print("""
                ---------------------------------------------
                Vocabulary Control Center
                ---------------------------------------------
                """);
        System.out.print(
                """
                        1 browse a topic
                        2 insert a new topic before another one
                        3 insert a new topic after another one
                        4 remove a topic
                        5 modify a topic
                        6 search topics for a word
                        7 load from a file
                        8 show all words starting with a given letter
                        9 save to file
                        0 exit
                        """);
        System.out.print("---------------------------------------------\n");
        System.out.println("Enter Your Choice: ");
        try{
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch(choice)
            {
                case 1:
                    showTopics();
                    break;
                case 2:
                    insertBefore();
                    break;
                case 3:
                    insertAfter();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    importFile();
                    break;
                case 0:
                    System.exit(0);

            }
        }

        catch (InputMismatchException e)
        {
            System.out.println("Please enter a number from 0 to 9");
            showMainMenu();
        }
        finally {
            showMainMenu();
        }
    }
    static void showTopics()
    {
        System.out.println("""
                ---------------------------------------------
                                Pick a Topic               \s
                ---------------------------------------------""");
        int choice = 0;
        try
        {
            Scanner topicScanner = new Scanner(System.in);
            vocabList.printVocabList();
            System.out.println("0 Exit test");
            System.out.println("---------------------------------------------");
            System.out.println("Enter Your Choice: ");
            choice = topicScanner.nextInt();
            if (choice == 0) {
                return;
            }
            if (vocabList.getVocabObjectAtIndex(choice) != null) {
                VocabList.Vocab vocabObject = vocabList.getVocabObjectAtIndex(choice);
                vocabObject.words.printWordList();
            }
            else {
                System.out.println("Invalid choice. Please select a valid topic number.");
                showTopics();
            }
        }
        catch (NullPointerException e)
        {
            if (vocabList == null)
            {
                System.out.println("\nTopic array is empty. Please insert topics or\nload data from an input file.\n");
                showMainMenu();
            }

        }
        catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter an integer (0-9).");
            showTopics();
        }
    }
    static void insertBefore()
    {
        Scanner input = new Scanner(System.in);
        vocabList.printVocabList();
        System.out.println("Choose which topic you would like to add and before which topic");
        System.out.println("Topic to insert : ");
        String toInsert = input.nextLine();
        System.out.println("Insert before : ");
        String insertAt = input.nextLine();
        if (vocabList.findNodeByTopic(insertAt) != null) {
            vocabList.insertTopicBefore(vocabList.findNodeByTopic(insertAt), toInsert);
            System.out.println("Topic '" + toInsert + "' has been inserted before '" + insertAt + "'.");
            addWordsToTopic(input, toInsert);
        } else {
            System.out.println("Topic '" + insertAt + "' not found. Please try again.");
            insertBefore();
        }

    }
    static void insertAfter()
    {
        Scanner input2 = new Scanner(System.in);
        vocabList.printVocabList();
        System.out.println("Choose which topic you would like to add and after which topic");
        System.out.println("Topic to insert : ");
        String toInsert = input2.next();
        System.out.println("Insert after : ");
        String insertAt = input2.next();
        if (vocabList.findNodeByTopic(insertAt) != null) {
            vocabList.insertTopicAfter(vocabList.findNodeByTopic(insertAt), toInsert);
            System.out.println("Topic '" + toInsert + "' has been inserted after '" + insertAt + "'.");
            addWordsToTopic(input2, toInsert);
        } else {
            System.out.println("Topic '" + insertAt + "' not found. Please try again.");
            insertAfter();
        }

    }

    private static void addWordsToTopic(Scanner input2, String toInsert) {
        input2.nextLine();
        System.out.println("Enter words for the topic '" + toInsert + "' (one word per line, empty line to finish):");
        String word = input2.nextLine().trim();
        while (!word.isEmpty()) {
            vocabList.addWordToVocab(toInsert, word);
            word = input2.nextLine().trim();
        }
        System.out.println("Words added successfully.");
    }

    static void removeTopic()
    {
        Scanner input3 = new Scanner(System.in);
        vocabList.printVocabList();
        System.out.println("Choose which topic you would like to add and after which topic");
    }
    static void importFile()
    {

        try{
            File textFile = new File("src/input.txt");
            Scanner reader = new Scanner(textFile);
            String line;
            String currentTopic = null;

            while (reader.hasNextLine()) {
                line = reader.nextLine();
                if (line.startsWith("#")) {
                    // extract the topic without "#"
                    currentTopic = line.substring(1).trim();
                    if (vocabList.head == null) {
                        vocabList.addInitialTopic(currentTopic);
                    } else {
                        vocabList.insertTopicAfter(vocabList.tail, currentTopic);
                    }
                } else if (!line.isEmpty() && currentTopic != null) {
                    // if the line is not empty and there's a current topic
                    vocabList.addWordToVocab(currentTopic, line.trim());
                }
            }

            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            showMainMenu();
        }
    }


}
