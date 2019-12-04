/**
 * Class to represent hash table
 *
 * @author Colleen Schmidt collee57
 * @author Allison DeSantis ajd29
 * @param <K>
 * @param <V>
 */
public class HashTable<K, V>
{
    // Array of slots
    private HashNode<K, V>[] array;

    // Current capacity of array, 32 spots in each bucket
    private int              numBuckets;

    // Current size of array
    private int              size;

    // hash table capacity
    private int              capacity;

    /**
     * Default constructor
     *
     * @param hashSize
     *            size of hash table
     */
    public HashTable(int hashSize)
    {
        capacity = hashSize;
        array = new HashNode[hashSize];
        numBuckets = 32;
        size = 0;

        // Create empty chains
        for (int i = 0; i < numBuckets; i++)
        {
            array[i] = null;
        }
    }

    /**
     * Returns size of the hash table
     *
     * @return int the size
     */
    public int size()
    {
        return size;
    }

    /**
     * returns if the table is empty or not
     *
     * @return boolean true or false
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * hash function to return index where value goes
     *
     * @param key
     *            the key
     * @return long the index
     */
    private int sfold(K key)
    {
        int intLength = ((String)key).length() / 4;
        int sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char c[] =
                ((String)key).substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char c[] = ((String)key).substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % capacity);
    }

    /**
     * removes an element
     *
     * @param key
     *            the key associated with value
     * @return V the value
     */
    public V remove(K key)
    {
        // Apply hash function to find index for given key
        int bucketIndex = sfold(key);

        // Get head of chain
        HashNode<K, V> head = array[bucketIndex];

        // Search for key in its chain
        HashNode<K, V> prev = null;

        while (head != null) {
            // If key found
            if (head.key.equals(key))
                break;

            // else keep moving in chain
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
        {
            prev.next = head.next;
        }
        else
        {
            array[bucketIndex] = head.next;
        }

        return head.value;
    }

    /**
     * Returns a value for a key
     *
     * @param key
     *            to return value for
     * @return V value of key searched for
     */
    public V get(K key)
    {

        // Find head of chain for given key
        int bucketIndex = sfold(key);
        HashNode<K, V> head = array[bucketIndex];

        // Search key in chain
        while (head != null)
        {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }

    /**
     * Adds key value pair to hash table
     *
     * @param key
     * @param value
     */
    public void add(K key, V value)
    {
        // return if hash table is full
        if (!isFull())
        {
            // Find head of chain for given key
            int bucketIndex = sfold(key);
            int bucketsFull = 0;

            HashNode<K, V> head = array[bucketIndex];

            // Check if spot is already filled,
            // then move to next node if it is full
            while (head != null && bucketsFull < 32)
            {

                // increase bucket index
                bucketIndex++;

                // if at end of bucket
                if (bucketIndex % 32 == 0)
                {

                    // go back to beginning of bucket
                    bucketIndex -= 32;
                }

                /*
                 * if (head.key.equals(key)) { head.value = value; return; }
                 * head = head.next;
                 */
                bucketsFull++;
            }

            // Insert key in bucket
            size++;
            head = array[bucketIndex];
            HashNode<K, V> newNode = new HashNode<K, V>(key, value);
            newNode.next = head;
            array[bucketIndex] = newNode;

            // spec says that if bucket is full, insert is rejected
            // If bucket is full,
            // double hash table size
            /*
             * if (this.isFull()) { HashNode<K, V>[] temp = array; array = new
             * HashNode[32]; numBuckets = 2 * numBuckets; size = 0; for (int i =
             * 0; i < numBuckets; i++) { array[i] = null;} for (HashNode<K, V>
             * headNode : temp) { while (headNode != null) { add(headNode.key,
             * headNode.value); headNode = headNode.next; } }
             */
        }
    }

    /**
     * Checks if hash table is full
     *
     * @return boolean true or false
     */
    public boolean isFull()
    {
        return (size == capacity);
    }

    /**
     * Represents a node in the hash table
     *
     * @author collee57
     * @author ajd29
     * @param <T>
     * @param <M>
     */
    private class HashNode<K, V>
    {
        K              key;
        V              value;

        // Reference to next node
        HashNode<K, V> next;

        // Constructor
        public HashNode(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }
}
