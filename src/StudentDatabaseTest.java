import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Tests the methods of the StudentDatabase class
 *
 * @author ajd29
 * @author collee57
 * @version Dec 3, 2019
 */
public class StudentDatabaseTest extends student.TestCase {
    // StudentDatabase object
    private StudentDatabase manager;
    private StudentDatabase data;
    private StudentDatabase base;

    // student
    private Student stu;


    /**
     * Set up is called before every test
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        manager = new StudentDatabase(32);
        data = new StudentDatabase(64);
        base = new StudentDatabase(32);

        manager.loadStudentData("SampleStudents.csv");
        data.loadStudentData("new.csv");

        stu = new Student("12345", "Allison", "DeSantis");
    }


    /**
     * Tests loadStudentData()
     * 
     * @throws IOException
     */
    public void testLoadStudentData() throws IOException {
        setUp();
        // testing with sample csv
        System.out.println("test load:\n");
        assertEquals(manager.getHash().size(), 15);
        assertEquals(manager.getHash().get("044722015").toString(),
            "Burton Frost at slot ");
        assertEquals(manager.getHash().get("815012117").toString(),
            "Ezekiel Ruiz at slot ");
        System.out.println(manager.getHash().getArrayString());
        System.out.println(manager.getHash().getSfoldKey("877002980"));
        System.out.println(manager.getHash().getSfoldKey("940597271"));

        // testing with new csv file
        System.out.println("test load2:\n");
        System.out.println(data.getHash().getArrayString());
        // assertEquals(data.getHash().get("21").toString(),
        // "Kylie Jenner at slot ");

    }


    /**
     * Tests insert()
     * 
     * @throws IOException
     */
    public void testInsert() throws IOException {
        System.out.println("test insert1:\n");

        // testing with sample
        // should be 13 since 2 are missing but is 15
        assertEquals(15, manager.getHash().size());
        manager.insert("123", "Colleen", "Schmidt");
        manager.search("123");

        manager.insert("1234", "Allison", "DeSantis");

        // should not be able to find
        manager.search("12");
        System.out.println(manager.getHash().getArrayString());

        assertEquals(17, manager.getHash().size());

        // testing with new csv file
        System.out.println("test insert2:\n");
        data.insert("36", "Chicken", "Nugget");
        assertEquals(35, data.getHash().size());
        // should print
        data.search("36");

    }


    /**
     * Tests search()
     * 
     * @throws FileNotFoundException
     */
    public void testSearch() throws FileNotFoundException {

        // testing with sample
        System.out.println("test search:\n");

        // should find Burton Frost
        manager.search("044722015");
        assertEquals(manager.getHash().get("044722015").toString(),
            "Burton Frost at slot ");

        // should not be able to find
        manager.search("1");

        System.out.println(manager.getHash().getArrayString());

        // testing with new csv
        System.out.println("test search2:\n");

        // should find Allison DeSantis
        data.search("03");
        assertEquals(data.getHash().get("03").toString(),
            "Allison  Desantis at slot ");
    }


    /**
     * Tests remove()
     * 
     * @throws IOException
     */
    public void testRemove() throws IOException {

        // testing with sample
        System.out.println("test remove1:\n");

        // remove burton frost, hash size should reduce, should not be able to
        // find
        manager.remove("044722015");
        assertEquals(14, manager.getHash().size());
        manager.search("044722015");

        manager.remove("815012117");
        assertEquals(13, manager.getHash().size());

        // testing with new csv
        System.out.println("test remove2:\n");
    }


    /**
     * Tests update()
     * 
     * @throws IOException
     */
    public void testUpdate() throws IOException {

        System.out.println("test update:\n");
        manager.update("044722015", "Chicken", "Nugget");
        assertEquals(manager.getHash().get("044722015").toString(),
            "Chicken Nugget at slot ");

    }


    /**
     * Tests essay()
     * 
     * @throws IOException
     */
    public void testEssay() throws IOException {
        manager.insert("123456789", "Chicken", "Nugget");
        manager.essay("hi my name is Colleen");
        assertEquals("hi my name is Colleen", manager.getHash().get("123456789")
            .getEssay());
        // should print "hi my name is colleen"
        manager.search("123456789");

        // should print error
        manager.essay("hello");

        // essay after update command

        data.update("123456789", "hello", "world");

        data.essay("i am in cs 3114");
        assertEquals("i am in cs 3114", data.getHash().get("123456789")
            .getEssay());

        // insert student to empty hash table
        base.insert("12345", "Allison", "DeSantis");
        assertEquals(base.getHash().size(), 1);

        // check that correct size was written to memory file
        int length = stu.getName().getBytes().length;
        assertEquals(base.getMem().getMemFile().length(), length);

        // give student essay
        length += stu.getEssay().getBytes().length;
        base.essay("first essay for a student\n" + "second line\n");
        assertEquals(base.getMem().getMemFile().length(), length);

    }


    /**
     * gets the hash table
     */
    public void testgetHash() {
        HashTable<String, Student> tab = manager.getHash();
        assertEquals(tab, manager.getHash());
    }


    /**
     * tests the get mem method
     */
    public void testgetMem() {
        MemoryManager mem = manager.getMem();
        assertEquals(mem, manager.getMem());
    }


    /**
     * tests the clear method
     * 
     * @throws IOException
     */
    public void testClear() throws IOException {
        // print error
        manager.clear("0");

        manager.insert("12", "Colleen", "Schmidt");
        // add essay
        manager.essay("hello world");
        assertEquals("hello world", manager.getHash().get("12").getEssay());

        // essay should be blank
        manager.clear("12");
        assertEquals("", manager.getHash().get("12").getEssay());

    }


    /**
     * tests the print method
     */
    public void testprint() {
        System.out.println("test print:/n");
        assertEquals(0, manager.getMem().getFreeList().size());
        manager.print();
    }

}
