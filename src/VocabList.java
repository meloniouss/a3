import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;

//to do: search for words, search words with x first letter
//write to a txt file
//etc. perchance
public class VocabList{
      VocabNode head;
      VocabNode tail;


    public class Vocab
    {
        String topic;
        WordList words;
        public Vocab(String topic)
        {
            this.topic = topic;
            words = new WordList();
        }
    }
    public void addWordToVocab(String vocabTopic, String word) {
        // Find the VocabNode with the specified topic
        VocabNode vocabNode = findNodeByTopic(vocabTopic);

        // Check if the VocabNode exists
        if (vocabNode != null) {
            // Add the word to the WordList of the found Vocab
            vocabNode.vocabObject.words.addWord(word);
            System.out.println("Word \"" + word + "\" added to vocabulary \"" + vocabTopic + "\".");
        } else {
            System.out.println("Vocabulary \"" + vocabTopic + "\" not found.");
        }

    }

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

    private class VocabNode
    {
        public Vocab vocabObject;
        VocabNode next; //since it's going to be a doubly linked list we need next
        VocabNode previous; // and previous
        VocabNode(Vocab vocabObject)
        {
            this.vocabObject = vocabObject;
        }

        public Vocab getVocabObject() {
            return this.vocabObject;
        }
    }
    public class WordList {
        WordNode head; // head of wordList (first word basically)

        private class WordNode {
            String word;
            WordNode next;

            WordNode(String word) { //constructor to create new node
                this.word = word;
            }
        }
        public boolean searchFor(String word) {
            WordNode current = head;
            while (current != null) {
                if (current.word.equals(word))
                    return true;
                current = current.next;
            }
            return false;

        }
        public  void searchForLetter(String letter)
        {

            ArrayList<String> extractedList = new ArrayList<String>();
            WordNode current = head;
            while(current != null) {
                if (current.word.substring(0, 1).equals(letter)) {
                    extractedList.add(current.word);
                }
                current = current.next;

            }

            if (extractedList.size() > 1)
            {
                for(String word : extractedList)
                {
                    System.out.println(word);
                }
            }
            else
            {
                System.out.println("No words with this letter have been found in this topic");
            }
        }

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
        public void addWord(String newWord) { // the words are added in alphabetical order
            WordNode currentNode = this.head;
            WordNode newWordnode = new WordNode(newWord);
            if (this.head == null) {
                this.head = newWordnode;
                System.out.println("Word " + newWord + " has been added as the first word.");
                return;
            }
            if(newWord.compareTo(this.head.word) < 0)
            {
                newWordnode.next = this.head;
                this.head = newWordnode;
                System.out.println("Word " + newWord + " has been added at the beginning.");
                return;
            }
            while(currentNode.next != null && newWord.compareToIgnoreCase(currentNode.next.word) >= 0 )
            {
                currentNode = currentNode.next;
            }
            newWordnode.next = currentNode.next;
            currentNode.next = newWordnode;
            System.out.println("Word " + newWord + " has been added.");

        }

        public void modifyWord(String wordToBe, String newWord) {
            WordNode current = this.head;
            WordNode newWordNode = new WordNode(newWord);
            if (this.head == null) {
                System.out.println("You have no words associated to this topic. Please try adding words first.");
            }
            while (current != null) {
                if (current.word.equalsIgnoreCase(wordToBe)) {
                    current.word = newWord;
                } else
                    current = current.next;
            }
        }

        public void removeWord(String toRemove) {
            if (head == null) {
                System.out.println("You have no words associated with this topic. Please try adding words first.");
                return;
            }

            // If the word to remove is the first node
            while (head != null && head.word.equals(toRemove)) {
                head = head.next;
            }

            WordNode current = head;
            WordNode previous = null;

            // Traverse the list to find and remove the node with the word to remove
            while (current != null) {
                if (current.word.equals(toRemove)) {
                    previous.next = current.next;
                } else {
                    // Move to the next node
                    previous = current;
                }
                // Move to the next node
                current = current.next;
            }

        }

        public  void extracted(VocabNode current, PrintWriter writer) {
            WordNode wordNode = current.vocabObject.words.head;
            while (wordNode != null) {
                writer.println(wordNode.word);
                wordNode = wordNode.next;
            }
        }
        }

    public void printVocabList() {
        int counter = 1;
        VocabNode current = head;
        while (current != null) {
            System.out.println(counter++ + " " +current.vocabObject.topic);
            current = current.next;
        }
    }

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
    public void addInitialTopic(String topic) {
        // Create a new VocabNode with the given topic
        VocabNode newNode = new VocabNode(new Vocab(topic));

        // Set both head and tail to the new node since it's the only node in the list
        head = tail = newNode;
    }
    public String getTopic(VocabNode node) {
        return node.vocabObject.topic;
    }

    //we use this to index vocab objects
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

    // helper method to get the size of the VocabList kinda useless for now
    private int getSize() {
        int size = 0;
        VocabNode current = head;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }
    public void searchWordInVocab(String word)
    {
        VocabNode current = head;
        if(current == null)
            System.out.println("You haven't added any topics yet. Make sure to add some topics and words before searching.");
        while(current != null)
        {
            if(current.vocabObject.words.searchFor(word))
            {
                System.out.println(word + " has been found in topic " + "'"+current.vocabObject.topic + "'");
            }
            current = current.next;
        }
       if(!current.vocabObject.words.searchFor(word))
           System.out.println("The word could not be found.");
    }
    public void searchLetterInVocab(String letter)
    {
        VocabNode current = head;
        if(current == null)
            System.out.println("You haven't added any topics yet. Make sure to add some topics and words before searching.");
        while(current !=null)
        {
            System.out.println("\nTopic: " + current.vocabObject.topic);
            current.vocabObject.words.searchForLetter(letter);
            current = current.next;
        }
    }


        public void writeToFile(String filePath) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
                VocabNode current = head;
                while (current != null) {
                    // Write the topic to the file preceded by '#'
                    writer.println("#" + current.vocabObject.topic);

                    // Iterate through words of the current vocabulary and write them
                    current.vocabObject.words.extracted(current,writer);

                    // Move to the next VocabNode
                    current = current.next;
                }
                System.out.println("Vocab list successfully written to " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

    

}

