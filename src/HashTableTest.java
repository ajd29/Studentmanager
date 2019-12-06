import student.TestCase;

/**
 * test class for hash table
 * 
 * @author collee57 Colleen Schmidt
 * @author ajd29 Allison DeSantis
 * @version Dec 6 2019
 *
 */
public class HashTableTest extends TestCase {

    // hash table to test with
    private HashTable<String, Integer> table;

    // hash table with students
    private HashTable<String, Student> studentTable;

    // student objects
    private Student s1;
    private Student s2;


    /**
     * Set up method
     */
    public void setUp() throws Exception {
        table = new HashTable<String, Integer>(32);
        studentTable = new HashTable<String, Student>(64);
        s1 = new Student("123", "Sally student");
        s2 = new Student("1234", "s2 student");
    }


    /**
     * Tests size()
     * 
     * @throws Exception
     */
    public void testSize() throws Exception {
        setUp();
        // empty
        assertEquals(0, table.size());
        assertEquals(0, studentTable.size());
        // add one student
        studentTable.add("123", s1);
        assertEquals(1, studentTable.size());
        // add another
        studentTable.add("1234", s2);
        assertEquals(2, studentTable.size());
        // remove one
        studentTable.remove("123");
        assertEquals(1, studentTable.size());
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

        studentTable.add("123", s1);
        // studentTable.add("123", s1);
        // studentTable.add("123", s1);
        studentTable.add("1234", s2);
        assertEquals(studentTable.get("123"), s1);
        // assertEquals(studentTable.get("1"), s1);
        assertEquals(studentTable.get("1234"), s2);
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
        studentTable.add("3", s1);
        assertFalse(studentTable.isEmpty());

    }


    /**
     * Tests isFull()
     */
    public void testIsFull() throws Exception {
        setUp();
        //empty
        assertFalse(table.isFull());

        // fill hash table
        String pid = "123";
        for (int i = 0; i < 32; i++) {
            table.add(pid, 4);
            pid += "1";
        }
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
        assertEquals("123, Sally Student at slot ", studentTable.get("123")
            .toString());

        studentTable.add("1234", s2);
        assertEquals(2, studentTable.size());
        assertEquals("1234, S2 Student at slot ", studentTable.get("1234")
            .toString());

        // fill hash table
        String pid = "123";
        for (int i = 0; i < 30; i++) {
            table.add(pid, 4);
            pid += "1";
        }
        table.add("12", 3);
        assertEquals(table.size(), 32);

        // fill student table
        for (int i = 0; i < 62; i++) {
            studentTable.add(pid, s1);
            pid += "1";
        }
        studentTable.add("12", s1);
        assertEquals(studentTable.size(), 64);
    }


    /**
     * Tests remove()
     * 
     * @throws Exception
     */
    public void testRemove() throws Exception {
        setUp();

        studentTable.add("person", s1);
        studentTable.remove("person");
        assertEquals(studentTable.size(), 0);
        assertNull(studentTable.get("person"));

        studentTable.add("123", s1);
        studentTable.add("1234", s1);
        studentTable.add("1", s1);

        studentTable.remove("123");
        assertEquals(studentTable.size(), 2);

        studentTable.remove("1");
        assertEquals(studentTable.size(), 1);

        // already removed
        studentTable.remove("1");
        assertEquals(studentTable.size(), 1);
    }


    /**
     * tests the getsfoldkey method
     * 
     * @throws Exception
     */
    public void testgetSfoldKey() throws Exception {
        setUp();

        studentTable.add("123", s1);
        int key = studentTable.getSfoldKey("123");
        assertEquals(key, studentTable.getSfoldKey("123"));
        System.out.println(key);
    }


    /**
     * tests bucket full method
     * 
     * @throws Exception
     */
    public void testisBucketFull() throws Exception {
        setUp();

        assertFalse(studentTable.isBucketFull());

        String pid = "123";
        for (int i = 0; i < 30; i++) {
            studentTable.add(pid, s1);
            pid += "1";
        }

        System.out.println(studentTable.getSlotsFull());
        // assertTrue(studentTable.isBucketFull());
    }
    
    /**
     * tests the get slots full method
     * @throws Exception 
     */
    public void testgetSlotsFull() throws Exception {
        setUp();
        assertEquals(0, studentTable.getSlotsFull());
        studentTable.add("123", s1);
        studentTable.add("1234", s2);
        assertEquals(2, studentTable.getSlotsFull());
    }
}
