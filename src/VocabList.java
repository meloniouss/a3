//to do: search for words, search words with x first letter
//write to a txt file
//etc perchance
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

        public void printWordList() {
            int counter = 1;
            WordNode current = head;
            while (current != null) {
                System.out.println(counter++ + " " +current.word);
                current = current.next;
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


        }

    public void printVocabList() {
        int counter = 1;
        VocabNode current = head;
        while (current != null) {
            System.out.println(counter++ + " " +current.vocabObject.topic);
            current = current.next;
        }
    }

    public void removeVocab(Vocab vocabToRemove) {
        VocabList.VocabNode current = head;
        while (current != null) {
            if (current.vocabObject.equals(vocabToRemove)) {


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
}

