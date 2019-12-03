

/**
 * Class to represent entire hash table
 * 
 * @author Colleen Schmidt collee57
 * @author Allison DeSantis ajd29
 *
 * @param <K>
 * @param <V>
 */
public class HashTable<K, V> {
    // bucketArray is used to store array of buckets
    private HashNode<K, V>[] bucketArray;

    // Current capacity of array, 32 spots in each bucket
    private int numBuckets;

    // Current size of array
    private int size;


    /**
     * constructor
     */
    public HashTable() {
        bucketArray = new HashNode[32];
        numBuckets = 32;
        size = 0;

        // Create empty chains
        for (int i = 0; i < numBuckets; i++)
            bucketArray[i] = null;
    }


    /**
     * gets the size of the hash table
     * 
     * @return int the size
     */
    public int size() {
        return size;
    }


    /**
     * returns if the table is empty or not
     * 
     * @return boolean true or false
     */
    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     * hash function to return index where value goes
     * 
     * @param key
     *            the key
     * 
     * @return long the index
     */
    public int sfold(K key) {
        int intLength = ((String)key).length() / 4;
        int sum = 0;
        for (int j = 0; j < intLength; j++) {
            char c[] = ((String)key).substring(j * 4, (j * 4) + 4)
                .toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char c[] = ((String)key).substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % size);
    }


    /**
     * removes an element
     * 
     * @param key
     *            the key associated with value
     * @return V the value
     */
    public V remove(K key) {
        // Apply hash function to find index for given key
        int bucketIndex = sfold(key);

        // Get head of chain
        HashNode<K, V> head = bucketArray[bucketIndex];

        // Search for key in its chain
        HashNode<K, V> prev = null;
        while (head != null) {
            // If Key found
            if (head.key.equals(key))
                break;

            // Else keep moving in chain
            prev = head;
            head = head.next;
        }

        // If key was not there
        if (head == null)
            return null;

        // Reduce size
        size--;

        // Remove key
        if (prev != null)
            prev.next = head.next;
        else
            bucketArray[bucketIndex] = head.next;

        return head.value;
    }


    // Returns value for a key
    public V get(K key) {
        // Find head of chain for given key
        int bucketIndex = sfold(key);
        HashNode<K, V> head = bucketArray[bucketIndex];

        // Search key in chain
        while (head != null) {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }


    // Adds a key value pair to the table
    public void add(K key, V value) {
        // Find head of chain for given key
        int bucketIndex = sfold(key);
        HashNode<K, V> head = bucketArray[bucketIndex];

        // Check if spot is already filled, then move to next node if it is full
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        
        // Insert key in bucket
        size++;
        head = bucketArray[bucketIndex];
        HashNode<K, V> newNode = new HashNode<K, V>(key, value);
        newNode.next = head;
        bucketArray[bucketIndex] = newNode;

        // If bucket is full,
        // double hash table size
        if (this.isFull()) {
            HashNode<K, V>[] temp = bucketArray;
            bucketArray = new HashNode[32];
            numBuckets = 2 * numBuckets;
            size = 0;
            for (int i = 0; i < numBuckets; i++)
                bucketArray[i] = null;

            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }

    }


    /**
     * sees if bucket is full
     * 
     * @return boolean true or false
     */
    public boolean isFull() {
        return size == 32;
    }


    /**
     * class that represents a node in the hash table
     * 
     * @author colle
     *
     * @param <K>
     * @param <V>
     */
    class HashNode<K, V> {
        K key;
        V value;

        // Reference to next node
        HashNode<K, V> next;


        // Constructor
        public HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
