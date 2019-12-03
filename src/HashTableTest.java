import student.TestCase;
/**
 * test class for hash table
 * @author colle
 *
 */
public class HashTableTest extends TestCase {

    //hash table to test with
    private HashTable table;
    
    /**
     * set up method
     */
    public void setUp() throws Exception {
        table = new HashTable<String, Integer>();
        
    }
    
    /**
     * tests the size method
     */
    public void testSize() {
        assertEquals(0, table.size());
    }
    
    /**
     * tests the is empty method
     */
    public void testisEmpty() {
        assertTrue(table.isEmpty());
    }
    
    /**
     * tests the add method
     */
    public void testadd() {
        table.add("cat", 4);
        assertEquals(1, table.size());
        assertEquals(4, table.get("cat"));
        
    }


    

}
