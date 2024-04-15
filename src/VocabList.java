import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

// -----------------------------------------------------
// Assignment 3
// Written by:
// Duc Vinh Lam, 40282959
// Andrei Jianu, 40275653
// Due April 15th, 2024
// -----------------------------------------------------


/**
 * The double linked list class, titled VocabList, used to hold VocabNodes which themselves have single-linked WordLists.
 * Also contains the classes VocabNode, WordList, WordNode.
 */
public class VocabList {

    /**
     * Static boolean value to tell the addWord function whether it should be printing the "added at beginning" and "added first word" messages. It's especially annoying for loading files, but otherwise, it's wanted.
     */
    public static boolean loading = false;

    VocabNode head;
    VocabNode tail;


    /**
     * Vocab object
     * "an object wrapping a topic name and the vocabulary words associated with that topic."
     */
    public class Vocab {
        String topic;
        WordList words;

        public Vocab(String topic) {
            this.topic = topic;
            words = new WordList();
        }
    }

    /**
     * Adds a specified word to a specified topic.
     * Validation of whether the word is already under the existing topic is done outside.
     *
     * @param vocabTopic the topic to add to
     * @param word       the word to add
     */
    public void addWordToVocab(String vocabTopic, String word) {
        // Find the VocabNode with the specified topic
        VocabNode vocabNode = findNodeByTopic(vocabTopic);

        // Check if the VocabNode exists
        if (vocabNode != null) {
            // Add the word to the WordList of the found Vocab
            vocabNode.vocabObject.words.addWord(word);

        } else {
            System.out.println("Vocabulary \"" + vocabTopic + "\" not found.");
        }

    }

    /**
     * Operation on doubly-linked list inserting a topic before one specified by the user.
     *
     * @param beforeVoc the node before which we are trying to insert a new topic
     * @param topic     the topic to insert
     */
    public void insertTopicBefore(VocabNode beforeVoc, String topic) //will insert before
    {
        VocabNode newNode = new VocabNode(new Vocab(topic));
        newNode.next = beforeVoc;
        newNode.previous = beforeVoc.previous;
        if (beforeVoc.previous != null) {
            beforeVoc.previous.next = newNode;
        } else {
            head = newNode; // Update head if inserting before the first node
        }
        beforeVoc.previous = newNode;

    }


    public VocabNode findNodeByTopic(String topic) {
        VocabNode current = head;
        while (current != null) {
            if (current.vocabObject.topic.equalsIgnoreCase(topic)) {
                // Found the node with the specified topic
                return current;
            }
            current = current.next;
        }
        return null;
    }


    /**
     * Operation on doubly-linked list inserting a topic after one specified by the user.
     *
     * @param afterVoc the node after which we are trying to insert a new topic
     * @param topic    the topic to insert
     */
    public void insertTopicAfter(VocabNode afterVoc, String topic) {
        // Create a new node for the new topic
        VocabNode newNode = new VocabNode(new Vocab(topic));

        // Update the links of the new node to insert it after the specified node
        newNode.previous = afterVoc;
        newNode.next = afterVoc.next;

        // Update the links of the neighboring nodes
        if (afterVoc.next != null) {
            afterVoc.next.previous = newNode;
        } else {
            tail = newNode; // Update tail if inserting after the last node
        }
        afterVoc.next = newNode;
    }

    /**
     * The node object for the doubly linked VocabList.
     */
    private class VocabNode {
        public Vocab vocabObject;
        VocabNode next; //since it's going to be a doubly linked list we need next
        VocabNode previous; // and previous

        VocabNode(Vocab vocabObject) {
            this.vocabObject = vocabObject;
        }

        public Vocab getVocabObject() {
            return this.vocabObject;
        }
    }

    /**
     * The single linked list class, titled WordList, used to hold WordNodes which contain Strings associated to the words in each topic.
     */
    public class WordList {
        WordNode head; // head of wordList (first word basically)

        /**
         * The node object for the singly-linked WordList.
         */
        private class WordNode {
            String word;
            WordNode next;

            WordNode(String word) { //constructor to create new node
                this.word = word;
            }
        }

        /**
         * Parses through a WordList and returns the number of times a certain word is found.
         *
         * @param word the word to check for
         * @return the number of times that word appears in a list.
         */
        public int searchFor(String word) {
            int count = 0;
            WordNode current = head;
            while (current != null) {
                if (current.word.equals(word)) count++;
                current = current.next;
            }
            return count;
        }

        /**
         * This is option 8's primary function. It runs through all words in a given WordList, adds the ones
         * starting with the passed letter to an arraylist, then sorts it through "natural order" or alphabetical order.
         *
         * @param letter the letter to check for at the start of words.
         */
        public void searchForLetter(String letter) {

            ArrayList<String> extractedList = new ArrayList<>();
            WordNode current = head;
            while (current != null) {
                if (current.word.substring(0, 1).equals(letter)) {
                    extractedList.add(current.word);
                }
                current = current.next;

            }


            // there's a couple of alternatives here, all suggested by IDEs.
            // namely,
            // extractedList.sort(new Comparator<String>() {
            // @Override
            // public int compare(String s1, String s2) {
            //  return s1.compareTo(s2); // Sort in alphabetical
            //  }});
            extractedList.sort(Comparator.naturalOrder());

            if (!extractedList.isEmpty()) {
                for (String word : extractedList) {
                    System.out.println(word);
                }
            } else {
                System.out.println("No words with this letter have been found in this topic");
            }
        }

        /**
         * Prints the word list, keeping them neatly presented in 4 columns (4 words per line)
         */
        public void printWordList() {
            int counter = 1;
            WordNode current = head;
            while (current != null) {
                for (int i = 0; i < 4 && current != null; i++) {
                    System.out.printf("%-3d%-20s", counter++, current.word);
                    current = current.next;
                }
                System.out.println(); // Move to the next line after printing four words
            }
        }

        /**
         * Adds a new word to the list.
         *
         * @param newWord the word to add.
         */
        public void addWord(String newWord) { // the words are added in alphabetical order

            WordNode currentNode = this.head;
            WordNode newWordnode = new WordNode(newWord);
            if (this.head == null) {
                this.head = newWordnode;
                System.out.print(loading ? "" : ("Word " + newWord + " has been added as the first word.\n"));
                return;
            }
            if (newWord.compareTo(this.head.word) < 0) {
                newWordnode.next = this.head;
                this.head = newWordnode;
                System.out.print(loading ? "" : "Word " + newWord + " has been added at the beginning.\n");
                return;
            }
            while (currentNode.next != null && newWord.compareToIgnoreCase(currentNode.next.word) >= 0) {
                currentNode = currentNode.next;
            }
            newWordnode.next = currentNode.next;
            currentNode.next = newWordnode;
        }

        /**
         * Changes an existing word to another one specified by the user.
         *
         * @param wordToBe the word to impact by the change
         * @param newWord  the new word to replace the old one by
         */
        public void modifyWord(String wordToBe, String newWord) {
            WordNode current = this.head;
            if (this.head == null) {
                System.out.println("You have no words associated to this topic. Please try adding words first.");
            }
            while (current != null) {
                if (current.word.equalsIgnoreCase(wordToBe)) {
                    current.word = newWord;
                } else current = current.next;
            }
        }

        /**
         * Removes a word from the list.
         *
         * @param toRemove the word to remove.
         */
        public void removeWord(String toRemove) {
            if (head == null) {
                System.out.println("You have no words associated with this topic. Please try adding words first.");
                return;
            }

            // If the word to remove is the first node
            //while (head != null && head.word.equals(toRemove)) {
            if (head.word.equals(toRemove)) {
                head = head.next;
                return;
            }

            WordNode current = head;
            WordNode previous = null;

            // Traverse the list to find and remove the node with the word to remove
            while (current != null) {
                if (current.word.equals(toRemove)) {
                    previous.next = current.next;
                    return; //added this line to delete only one instance
                } else {
                    // Move to the next node
                    previous = current;
                }
                // Move to the next node
                current = current.next;
            }

        }

        /**
         * Runs through a vocab node's word list to extract and write each word to a file.
         *
         * @param current the vocab node to parse through
         * @param writer  the PrintWriter to use for writing to a file.
         */
        public void extracted(VocabNode current, PrintWriter writer) {
            WordNode wordNode = current.vocabObject.words.head;
            while (wordNode != null) {
                writer.println(wordNode.word);
                wordNode = wordNode.next;
            }
        }

        /**
         * Loops through a wordlist and uses removeWord() on any duplicate words found.
         *
         * @param current the VocabNode to pass through
         * @return the number of deleted, duplicate words.
         */
        public static int deleteDuplicateWords(VocabNode current) {
            int duplicateCount = 0;
            WordNode currentWordNode = current.vocabObject.words.head;
            while (currentWordNode != null) {
                int count = current.vocabObject.words.searchFor(currentWordNode.word);
                String possiblyDuplicatedWord = currentWordNode.word;
                while (currentWordNode != null && currentWordNode.word.equals(possiblyDuplicatedWord)) {
                    currentWordNode = currentWordNode.next;
                }
                if (count > 1) {

                    for (int i = 1; i < count; i++) { //removes all but one copy of the word.
                        current.vocabObject.words.removeWord(possiblyDuplicatedWord);
                        duplicateCount++;
                    }
                }
            }
            return duplicateCount;
        }
    }

    /**
     * Prints the vocab list, topic by topic with their associated index.
     */
    public void printVocabList() {
        int counter = 1;
        VocabNode current = head;
        while (current != null) {
            System.out.println(counter++ + " " + current.vocabObject.topic);
            current = current.next;
        }
    }

    /**
     * Removes a vocab node from a vocab list, equivalent to getting rid of a topic.
     *
     * @param vocabToRemove the vocab node associated to the topic to remove.
     */
    public void removeVocab(VocabNode vocabToRemove) {
        VocabList.VocabNode current = head;
        while (current != null) {
            if (current.equals(vocabToRemove)) {

                // Update head if it's the first node
                if (current == head) {
                    head = current.next;
                }
                // Update tail if it's the last node
                if (current == tail) {
                    tail = current.previous;
                }

                // Update references of neighboring nodes
                if (current.previous != null) {
                    current.previous.next = current.next;
                }
                if (current.next != null) {
                    current.next.previous = current.previous;
                }

                // Remove references from the node to be removed
                current.next = null;
                current.previous = null;

                // Exit the loop since we found and removed the vocab
                break;
            }
            current = current.next;
        }
    }

    /**
     * Creates the initial node for the VocabList.
     * Called upon when trying to insert before or after in an empty list.
     *
     * @param topic the topic to add.
     */
    public void addInitialTopic(String topic) {
        // Create a new VocabNode with the given topic
        VocabNode newNode = new VocabNode(new Vocab(topic));

        // Set both head and tail to the new node since it's the only node in the list
        head = tail = newNode;
    }

    /**
     * returns the topic of a given VocabNode.
     *
     * @param node the node to retrieve the topic from.
     * @return the topic.
     */
    public String getTopic(VocabNode node) {
        return node.vocabObject.topic;
    }


    /**
     * Indexes vocab objects and retrieves the one asked for by the user, specified through a passed integer index.
     *
     * @param index the index of the desired vocab object.
     * @return the vocab object from the list associated to the passed index.
     */
    public Vocab getVocabObjectAtIndex(int index) {
        VocabNode current = head;
        int currentIndex = 1; // Start index from 1

        // traverse the list to find the node at the specified index
        while (current != null && currentIndex < index) {
            current = current.next;
            currentIndex++;
        }

        // Check if the current node is not null and the index matches
        if (current != null && currentIndex == index) {
            return current.getVocabObject();
        }

        // if index is out of bounds or node not found, return null
        return null;
    }

    /**
     * Used to retrieve a vocab node associated to a passed topic name.
     * Only use is for cleansing duplicates within a specific topic (namely the one created in insertAfter and insertBefore).
     *
     * @param topic the name of the topic associated with the VocabNode to look for
     * @return the Vocab node associated to the topic.
     */
    public VocabNode getVocabNodeWithTopicName(String topic) {
        VocabNode current = head;

        // traverse the list to find the node with the specified topic
        while (current != null && !current.vocabObject.topic.equalsIgnoreCase(topic)) {
            current = current.next;
        }

        // Check if the current node is not null and the topic matches
        if (current != null && current.vocabObject.topic.equalsIgnoreCase(topic)) {
            return current;
        }

        // if index is out of bounds or node not found, return null
        return null;
    }

    /**
     * Retrieves the size of the VocabList.
     * YES, THE MORE OPTIMAL SOLUTION WOULD BE A COUNTER.
     *
     * @return the number of nodes in the VocabList.
     */
    public int getSize() {
        int size = 0;
        VocabNode current = head;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }

    /**
     * Parses through all vocabs and prints each instance of the passed word found within the lists.
     *
     * @param word the word to search for.
     */
    public void searchWordInVocab(String word) {
        boolean found = false;
        VocabNode current = head;
        //head == null case taken care of in driver through getSize() == 0
        while (current != null) {
            if (current.vocabObject.words.searchFor(word) > 0) {
                System.out.println(word + " has been found in topic " + "'" + current.vocabObject.topic + "'");
                found = true;
            }
            current = current.next;
        }
        if (!found) System.out.println("The word could not be found.");
    }

    /**
     * Runs searchForLetter() on every existing vocab in the list.
     * Functionally it goes through every vocab object and seeks out words that start with a given letter.
     *
     * @param letter the letter to check for at the start of the words.
     */
    public void searchLetterInVocab(String letter) {
        VocabNode current = head;
        //head == null case taken care of in driver through getSize() == 0
        while (current != null) {
            System.out.println("\nTopic: " + current.vocabObject.topic);
            current.vocabObject.words.searchForLetter(letter);
            current = current.next;
        }
    }

    /**
     * Writes the VocabList, word by word and topic by topic to a file with a format that can be reused as input.
     *
     * @param filePath the filepath/name to write to.
     */
    public void writeToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            VocabNode current = head;
            while (current != null) {
                // Writing the topic to the file preceded by '#'
                writer.println("#" + current.vocabObject.topic);

                // Iterating through words of the current vocabulary and write them
                current.vocabObject.words.extracted(current, writer);

                // Moving to the next VocabNode
                current = current.next;
            }
            System.out.println("Vocab list successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

    }

    /**
     * Removes all existing duplicate words in a vocab list.
     */
    public void cleanseDuplicates() {
        int duplicateCounter = 0;

        if (head == null) {
            System.err.println("No topics to cleanse.");
            return;
        }
        VocabNode current = head;
        while (current != null) {
            duplicateCounter += WordList.deleteDuplicateWords(current);
            current = current.next;
        }

        System.out.println(duplicateCounter + " duplicates found and deleted.");

    }

}

