/**
 * test class for hash table
 * 
 * @author collee57 Colleen Schmidt
 * @author ajd29 Allison DeSantis
 * @version dec 10 2019
 *
 */
public class HashTableTest extends student.TestCase {

    // hash table to test with
    private HashTable<String, Integer> table;

    // hash table with students
    private HashTable<String, Student> studentTable;

    // student objects
    private Student s1;
    // student objects
    private Student s2;


    /**
     * Set up method
     */
    public void setUp() throws Exception {
        table = new HashTable<String, Integer>(32);
        studentTable = new HashTable<String, Student>(64);
        s1 = new Student("123", "Sally", "student");
        s2 = new Student("1234", "s2", "student");
    }


    /**
     * Tests size()
     * 
     * @throws Exception
     */
    public void testSize() throws Exception {
        setUp();
        assertEquals(0, table.size());
        assertEquals(0, studentTable.size());
    }


    /**
     * Tests get()
     * 
     * @throws Exception
     */
    public void testGet() throws Exception {
        setUp();

        // not there
        assertNull(table.get("1"));

        studentTable.add("1", s1);
        studentTable.add("2", s1);
        studentTable.add("3", s1);
        studentTable.add("4", s2);
        assertEquals(studentTable.get("2").toString(), s1.toString());
        assertEquals(studentTable.get("1").toString(), s1.toString());
        assertEquals(studentTable.get("4").toString(), s2.toString());

        // bucket full
        String pid = "1";
        for (int i = 0; i < 64; i++) {
            studentTable.add(pid, s1);
            pid += "1";
            studentTable.get(pid);
        }
    }


    /**
     * Tests the getSfoldKey() method
     * 
     * @throws Exception
     */
    public void testGetSfoldKey() throws Exception {
        setUp();
        assertEquals(studentTable.getSfoldKey("36584246"), 25);
        assertEquals(studentTable.getSfoldKey("477937023"), 52);
    }


    /**
     * Tests isEmpty()
     * 
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        setUp();
        assertTrue(table.isEmpty());
        assertTrue(studentTable.isEmpty());
        studentTable.add("stu", s1);
        assertFalse(studentTable.isEmpty());
    }


    /**
     * Tests isBucketFull()
     * 
     * @throws Exception
     */
    public void testIsBucketFull() throws Exception {
        setUp();
        assertFalse(table.isBucketFull());

        // fill bucket
        String pid = "123";
        for (int i = 0; i < 32; i++) {
            studentTable.add(pid, s1);
        }
        studentTable.add("123", s1);
        assertTrue(studentTable.isBucketFull());
    }


    /**
     * Tests isBucketFull() when bucket is full
     * 
     * @throws Exception
     */
    public void testIsBucketFullTrue() throws Exception {
        setUp();

        String pid = "1";
        for (int i = 0; i < 32; i++) {
            table.add(pid, 3);
        }
        assertFalse(table.isBucketFull());
    }


    /**
     * Tests isFull()
     * 
     * @throws Exception
     */
    public void testIsFull() throws Exception {
        setUp();

        assertFalse(table.isFull());

        // fill hash table
        String pid = "123";
        for (int i = 0; i < 32; i++) {
            table.add(pid, 4);
        }
        assertEquals(table.size(), 32);
        assertTrue(table.isFull());
    }


    /**
     * Tests add()
     * 
     * @throws Exception
     */
    public void testAdd() throws Exception {

        setUp();

        table.add("cat", 4);
        assertEquals(1, table.size());
        assertEquals("4", table.get("cat").toString());

        table.add("dog", 1);
        assertEquals(2, table.size());
        assertEquals("1", table.get("dog").toString());

        studentTable.add("123", s1);
        assertEquals(1, studentTable.size());
        assertEquals("Sally student at slot ", studentTable.get("123")
            .toString());

        studentTable.add("1234", s2);
        assertEquals(2, studentTable.size());
        assertEquals("s2 student at slot ", studentTable.get("1234")
            .toString());

        // fill hash table
        String pid = "123";
        for (int i = 0; i < 30; i++) {
            table.add(pid, 4);
        }
        table.add("12", 3);
        assertEquals(table.size(), 32);

        // fill student table
        for (int i = 0; i < 62; i++) {
            studentTable.add(pid, s1);
        }
        studentTable.add("12", s1);
        assertEquals(studentTable.size(), 32);

        // add one that exists, doesn't add
        studentTable.add("12", s1);
        assertEquals(studentTable.size(), 32);
    }


    /**
     * Tests remove()
     * 
     * @throws Exception
     */
    public void testRemove() throws Exception {
        setUp();

        // add one, remove that one
        studentTable.add("person", s1);
        studentTable.remove("person");
        assertEquals(studentTable.size(), 0);
        assertNull(studentTable.get("person"));

        // add three
        studentTable.add("123", s1);
        studentTable.add("1234", s1);
        studentTable.add("1", s1);

        // remove one
        studentTable.remove("1234");
        assertEquals(studentTable.size(), 2);

        studentTable.remove("123");
        assertEquals(studentTable.size(), 1);

        studentTable.remove("1");
        assertEquals(studentTable.size(), 0);

        // already removed
        studentTable.remove("1");
        assertEquals(studentTable.size(), 0);
    }


    /**
     * Tests getArrayString()
     * 
     * @throws Exception
     */
    public void testGetArrayString() throws Exception {
        setUp();
        studentTable.add("person", s1);
        studentTable.getArrayString();
        assertEquals(studentTable.size(), 1);
    }
}
