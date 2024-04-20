import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

// -----------------------------------------------------
// Assignment 3
// Written by:
// Duc Vinh Lam, 40282959
// Andrei Jianu, 40275653
// Due April 15th, 2024
// -----------------------------------------------------

/**
 * The driver class for assignment 3.
 */
public class Driver {

    /**
     * The VocabList object used in the main method and throughout the project.
     */
    public static VocabList vocabList = new VocabList();

    /**
     * The main method
     *
     * @param args arguments i suppose
     */
    public static void main(String[] args) {
        showMainMenu();
    }


    /**
     * Display the main menu and prompts the user to make a choice among the other possible menus.
     */
    static void showMainMenu() {
        System.out.print("""
                ---------------------------------------------
                Vocabulary Control Center
                ---------------------------------------------
                """);
        System.out.print("""
                1 browse a topic
                2 insert a new topic before another one
                3 insert a new topic after another one
                4 remove a topic
                5 modify a topic
                6 search through topics for a word
                7 load from a file
                8 show all words starting with a given letter
                9 save to file
                                
                0 exit
                """);
        System.out.print("---------------------------------------------\n");
        System.out.println("Enter Your Choice: ");
        try {
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
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
                    removeTopic();
                    break;
                case 5:
                    modifyTopic();
                    break;
                case 6:
                    searchMethod();
                    break;
                case 7:
                    importFile();
                    break;
                case 8:
                    searchLetters();
                    break;
                case 9:
                    writeToFile();
                    break;
                case 0:
                    System.exit(0);

            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a number from 0 to 9");
            showMainMenu();
        } finally {
            showMainMenu();
        }
    }

    /**
     * Shows the list of topics, then prompts the user to select an index associated to a desired topic, then lists all items within that topic.
     */
    private static void showTopics() {
        if (vocabList.getSize() == 0) {
            System.out.println("No topics found!");
            return;
        }
        int choice;
        do {

            System.out.println("""
                    ---------------------------------------------
                                    Pick a Topic               \s
                    ---------------------------------------------""");


            Scanner topicScanner = new Scanner(System.in);

            vocabList.printVocabList();
            System.out.println("0 Exit");
            System.out.println("---------------------------------------------");
            System.out.println("Enter Your Choice: ");
            choice = topicScanner.nextInt();
            try {
                if (choice == 0) {
                    return;
                }
                if (vocabList.getVocabObjectAtIndex(choice) != null) {
                    VocabList.Vocab vocabObject = vocabList.getVocabObjectAtIndex(choice);
                    vocabObject.words.printWordList();
                } else {
                    System.out.println("Invalid choice. Please select a valid topic number.");
                    showTopics();
                    return;
                }
            } catch (NullPointerException e) {
                if (vocabList == null) {
                    System.out.println("\nTopic list is empty. Please insert topics or\nload data from an input file.\n");
                    showMainMenu();
                }

            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please enter an integer.");
                showTopics();
            }
        } while (choice != -1); //-1 instead of 0 because it returns anyway intellij just keeps giving me a warning and it's ugly
    }

    /**
     * Prompts the user to add a topic before an existing one.
     * If there are no topics currently listed, it creates a new one and assigns it as the head (and tail) of the dList.
     */
    private static void insertBefore() {
        Scanner input = new Scanner(System.in);
        String toInsert, insertAt;

        if (vocabList.getSize() == 0) {
            System.out.println("The vocab list is currently empty. Let's create a new topic!");
            do {
                System.out.println("Please enter the topic you would like to add");
                toInsert = input.next().trim();
            } while (toInsert.isEmpty());

            vocabList.addInitialTopic(toInsert);
            System.out.println("Topic '" + toInsert + "' has been created.");
            addWordsToTopic(toInsert);
            return;
        }

        vocabList.printVocabList();
        System.out.println("Choose which topic you would like to add and before which topic");
        System.out.println("Topic to insert : ");
        toInsert = input.nextLine();
        System.out.println("Insert before : ");
        insertAt = input.nextLine();
        if (vocabList.findNodeByTopic(insertAt) != null) {
            vocabList.insertTopicBefore(vocabList.findNodeByTopic(insertAt), toInsert);
            System.out.println("Topic '" + toInsert + "' has been inserted before '" + insertAt + "'.");
            addWordsToTopic(toInsert);
        } else {
            System.out.println("Topic '" + insertAt + "' not found. Please try again.");
            insertBefore();
        }

    }

    /**
     * Prompts the user to add a topic after an existing one.
     * If there are no topics currently listed, it creates a new one and assigns it as the head (and tail) of the dList.
     */
    private static void insertAfter() {
        Scanner input = new Scanner(System.in);
        String insertAt, toInsert;
        if (vocabList.getSize() == 0) {
            System.out.println("The vocab list is currently empty. Let's create a new topic!");
            do {
                System.out.println("Please enter the topic you would like to add");
                toInsert = input.next().trim();
            } while (toInsert.isEmpty());

            vocabList.addInitialTopic(toInsert);
            System.out.println("Topic '" + toInsert + "' has been created.");
            addWordsToTopic(toInsert);
            return;
        }

        vocabList.printVocabList();
        System.out.println("Choose which topic you would like to add and after which topic");
        System.out.println("Topic to insert : ");
        toInsert = input.next();
        System.out.println("Insert after : ");
        insertAt = input.next();
        if (vocabList.findNodeByTopic(insertAt) != null) {
            vocabList.insertTopicAfter(vocabList.findNodeByTopic(insertAt), toInsert);
            System.out.println("Topic '" + toInsert + "' has been inserted after '" + insertAt + "'.");
            addWordsToTopic(toInsert);
        } else {
            System.out.println("Topic '" + insertAt + "' not found. Please try again.");
            insertAfter();
        }
    }

    /**
     * Extension method for adding to a topic that prompts the user after the topic has successfully been inserted
     * to add words right away, taking each line to be a word and ending once an empty line is read.
     *
     * @param topic the topic to insert into.
     */
    private static void addWordsToTopic(String topic) {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter words for the topic '" + topic + "' (one word per line, empty line to finish):");
        String word = input.nextLine().trim();

        //disabling "added at beginning" and "added first" messages
        VocabList.loading = true;

        while (!word.isEmpty()) {
            vocabList.addWordToVocab(topic, word);
            word = input.nextLine().trim();
        }
        VocabList.loading = false;
        VocabList.WordList.deleteDuplicateWords(vocabList.getVocabNodeWithTopicName((topic)));
        System.out.println("Words added successfully. (duplicates automatically removed!)");
    }

    /**
     * Prompts the user to remove an existing topic from the list.
     */
    private static void removeTopic() {

        if (vocabList.getSize() == 0) {
            System.out.println("No topics here! Please import a file or create a new topic.");
            return;
        }

        try {
            Scanner input = new Scanner(System.in);
            vocabList.printVocabList();
            System.out.println("Choose which topic you would like to remove: ");
            int toDelete = input.nextInt();
            if (vocabList.getVocabObjectAtIndex(toDelete) != null) {
                System.out.println("Topic " + "'" + vocabList.getVocabObjectAtIndex(toDelete).topic + "'" + " has been successfully removed.");
                vocabList.removeVocab(vocabList.findNodeByTopic(vocabList.getVocabObjectAtIndex(toDelete).topic));
            } else {
                System.out.println("No topic named " + vocabList.getVocabObjectAtIndex(toDelete).topic + " could be found. Please enter a valid topic.");
                removeTopic();
            }
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter an integer.");
            removeTopic();
        }

    }

    /**
     * Prompts the user for a file to import, then parses it and adds all vocab terms to the list.
     */
    private static void importFile() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the name of your file please: ");
            String filePath = input.nextLine().trim();
            File textFile = new File("src/" + filePath);
            Scanner reader = new Scanner(textFile);
            String line;
            String currentTopic = null;

            VocabList.loading = true;

            while (reader.hasNextLine()) {
                //remove whitespace before and after using trim
                line = reader.nextLine().trim();
                if (line.trim().startsWith("#")) {
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
            VocabList.loading = false;
            reader.close();
            System.out.println("Loading of '" + filePath + "' completed.");
            System.err.println("OPTIONAL: enter 'Y' if you wish to delete all duplicates." +
                    "\nenter literally anything else if not");
            if (input.nextLine().equalsIgnoreCase("Y")) vocabList.cleanseDuplicates(); //NO DUPLICATES ALLOWED HERE!!!


        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            showMainMenu();
        }
    }

    /**
     * Allows the user to select a topic to then add, remove, or modify a word from.
     */
    private static void modifyTopic() {

        if (vocabList.getSize() == 0) {
            System.out.println("No topics here! Please import a file or create a new topic.");
            return;
        }
        System.out.println("""
                ---------------------------------------------
                                Pick a Topic               \s
                ---------------------------------------------""");
        int choice;
        try {
            Scanner topicScanner = new Scanner(System.in);
            vocabList.printVocabList();
            System.out.println("0 Exit");
            System.out.println("---------------------------------------------");
            System.out.println("Enter Your Choice: ");
            choice = Integer.parseInt(topicScanner.nextLine().trim());
            if (choice == 0) {
                return;
            }
            if (vocabList.getVocabObjectAtIndex(choice) != null) {
                System.out.println("Choose what you would like to do to this topic: ");
                System.out.println("1. Add word\n2. Modify word\n3. Delete word");
                Scanner sc = new Scanner(System.in);
                int choice2 = Integer.parseInt(sc.nextLine().trim());
                boolean passDuplicateCheck = false;
                switch (choice2) {
                    case 1:
                        System.out.println("Enter the word you would like to add: ");
                        String wordToAdd = sc.nextLine();

                        if (vocabList.getVocabObjectAtIndex(choice).words.searchFor(wordToAdd) == 0) {
                            vocabList.getVocabObjectAtIndex(choice).words.addWord(wordToAdd);
                            System.out.println(wordToAdd + " has been added successfully.");
                        } else {
                            System.out.println("The word \"" + wordToAdd + "\" already exists in the topic \"" + vocabList.getVocabObjectAtIndex(choice).topic + "\", please try again.");
                        }
                        break;
                    case 2:
                        String wordToChange;
                        boolean wordExists;
                        do {
                            System.out.println("Please enter the word you would like to modify: ");
                            wordToChange = sc.nextLine();
                            wordExists = vocabList.getVocabObjectAtIndex(choice).words.searchFor(wordToChange) > 0;
                            if (wordExists) {
                                System.out.println("Replacing the word \"" + wordToChange + "\".");
                            } else {
                                System.out.println("The word " + wordToChange + " was not found in the lists. Please try again.");
                            }

                        } while (!wordExists);

                        String newWord;
                        do {
                            System.out.println("Please enter the new word: ");
                            newWord = sc.nextLine();
                            if (vocabList.getVocabObjectAtIndex(choice).words.searchFor(newWord) == 0) {
                                passDuplicateCheck = true;
                                vocabList.getVocabObjectAtIndex(choice).words.modifyWord(wordToChange, newWord);
                            } else {
                                System.out.println("The word \"" + newWord + "\" already exists in the topic \"" + vocabList.getVocabObjectAtIndex(choice).topic + "\", please try again.");
                            }

                        } while (!passDuplicateCheck);
                        System.out.println(wordToChange + " has been changed to " + newWord);

                        break;
                    case 3:
                        String toDelete;
                        System.out.println("Please enter the word you would like to delete: ");
                        toDelete = sc.nextLine().trim();
                        if (vocabList.getVocabObjectAtIndex(choice).words.searchFor(toDelete) > 0) {
                            vocabList.getVocabObjectAtIndex(choice).words.removeWord(toDelete);
                            System.out.println(toDelete + " has been deleted successfully.");
                        } else {
                            System.out.println(toDelete + " could not be found under the specified topic.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        modifyTopic();
                        break;
                }
            } else {
                System.out.println("Invalid choice. Please select a valid topic number.");
                showTopics();
            }
        } catch (NullPointerException e) {
            if (vocabList == null) {
                System.out.println("\nTopic list is empty. Please insert topics or\nload data from an input file.\n");
                showMainMenu();
            }

        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter an integer.");
            showTopics();
        }
    }

    /**
     * Allows the user to search for a word in the lists.
     */
    private static void searchMethod() {
        if (vocabList.getSize() == 0) {
            System.out.println("You haven't added any topics yet. Make sure to add some topics and words before searching.");
            return;
        }

        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter which word you would like to search for: ");
            String searchingFor = input.nextLine().trim();
            vocabList.searchWordInVocab(searchingFor);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            searchMethod();
        }
    }

    /**
     * Prompts the user for a letter and outputs all words through all vocab lists that start by that letter.
     */
    private static void searchLetters() {
        if (vocabList.getSize() == 0) {
            System.out.println("You haven't added any topics yet. Make sure to add some topics and words before searching.");
            return;
        }
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the letter you would like the words to start with : ");
            String letter = String.valueOf(input.next().charAt(0)); // first character of the word regardless of if the user enters a char or a string
            vocabList.searchLetterInVocab(letter);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            searchLetters();
        }
    }

    /**
     * Prompts the user for a file path and writes the current vocab lists to it.
     */
    private static void writeToFile() {
        System.out.println("Please specify the path/file name you wish to write to.");
        Scanner input = new Scanner(System.in);
        String path = input.nextLine().trim();
        vocabList.writeToFile(path + ".txt");
    }

}
