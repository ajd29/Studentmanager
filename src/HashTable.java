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

    // Current size of array
    private int              size;

    // hash table capacity
    private long             capacity;

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
        size = 0;

        // Create empty chains
        for (int i = 0; i < hashSize; i++)
        {
            array[i] = null;
        }
    }

    /**
     * Returns hash table array
     *
     * @return HashNode<K, V> array
     */
    public String getArrayString()
    {
        String result = "";
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] != null)
            {
                result += "\n" + array[i].value + i;
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
     * Returns the integer that sfold returns for a key
     *
     * @param key
     * @return long that sfold function returns for key
     */
    public long getSfoldKey(K key)
    {
        return sfold(key);
    }


    /**
     * hash function to return index where value goes
     *
     * @param key
     *            the key
     * @return long the index
     */
    private long sfold(K key)
    {
        int intLength = ((String)key).length() / 4;
        long sum = 0;
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
        slotsFull = 0;

        // Apply hash function to find index for given key
        long bucketIndex = sfold(key);

        HashNode<K, V> head = array[(int)bucketIndex];

        while (!isBucketFull())
        {
            // If key found, decrease size and return
            if (head != null && head.key.equals(key)) {
                size--;
                array[(int)bucketIndex] = null;
                return head.value;
            }

            if ((bucketIndex + 1) % 32 == 0) {
                bucketIndex -= 31;
            }
            else {
                bucketIndex++;
            }
            head = array[(int)bucketIndex];

            // not found
            slotsFull++;
        }
        return null;
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
        slotsFull = 0;

        // Find head of chain for given key
        long bucketIndex = sfold(key);

        HashNode<K, V> head = array[(int)bucketIndex];

        // Search key in chain
        while (head != null)
        {
            // not found
            if (isBucketFull()) {
                return null;
            }
            if (head.key.equals(key))
            {
                return head.value;
            }
            // if last slot in bucket go back to 0
            if ((bucketIndex + 1) % 32 == 0) {
                bucketIndex -= 31;
            }
            else {
                bucketIndex++;
            }
            head = array[(int)bucketIndex];

            // wasn't found
            slotsFull++;
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
        long bucketIndex = sfold(key);

        slotsFull = 0;

        HashNode<K, V> head = array[(int)bucketIndex];

        while (head != null && slotsFull < 32)
        {
            slotsFull++;

            // if at end of bucket
            if ((bucketIndex + 1) % 32 == 0)
            {
                bucketIndex -= 31;
            }
            else
            {
                bucketIndex++;
            }

            head = array[(int)bucketIndex];
        }
        if (!isBucketFull())
        {
            size++;
            HashNode<K, V> newNode = new HashNode<K, V>(key, value);
            newNode.next = head;
            array[(int)bucketIndex] = newNode;
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
     * @param <K>
     *            generic for key
     * @param <V>
     *            generic for value
     */
    private class HashNode<K, V>
    {
        K              key;
        V              value;

        // Reference to next node
        HashNode<K, V> next;


        // Constructor
        private HashNode(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }
}
