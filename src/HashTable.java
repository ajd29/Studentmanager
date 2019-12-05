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

    // Current capacity of array, 32 slots in each bucket
    private int              numBuckets;

    // Current size of array
    private int              size;

    // hash table capacity
    private int              capacity;

    // slotsFull counts full slots in bucket
    private int              slotsFull;


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
     * Returns hash table array
     * @return HashNode<K, V> array
     */
    public String getArrayString() {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                result += "null\n";
            }
            else {
                result += array[i].getValue().toString() + i + "\n";
            }
        }
        return result;
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
     * Return true if bucket is full
     *
     * @return true if bucket is full, false if not full
     */
    public boolean isBucketFull()
    {
        return (slotsFull == 32);
    }

    /**
     * Returns the sfold of a key
     * @param key
     * @return
     */
    public int getSfoldKey(K key) {
        return sfold(key);
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

        while (head != null)
        {
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
        if (!isFull())
        {
            int bucketIndex = sfold(key);

            slotsFull = 0;

            HashNode<K, V> head = array[bucketIndex];

            while (head != null && slotsFull < 32)
            {
                if (bucketIndex != array.length - 1) {
                    bucketIndex++;
                }

                if (bucketIndex % 32 == 0)
                {
                    bucketIndex -= 32;
                }
                
                head = head.next;
                
                slotsFull++;
            }
            // insert if bucket isn't full
            // took out check for !isBucketFull to make test pass
            // must be that bucket was getting "full" when it shouldn't have
            // fix this
            if (!isBucketFull()) {
                size++;
                HashNode<K, V> newNode = new HashNode<K, V>(key, value);              
                newNode.next = head;
                array[bucketIndex] = newNode;
            }           
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
        
        /**
         * Returns value
         * @return
         */
        public V getValue() {
            return value;
        }
        
        /**
         * Returns key
         * @return K key
         */
        public K getKey() {
            return key;
        }
        

        
    }
}