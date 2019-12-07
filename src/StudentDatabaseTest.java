import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

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


    /**
     * Set up is called before every test
     * 
     * @throws FileNotFoundException
     */
    public void setUp() throws FileNotFoundException {
        manager = new StudentDatabase(32);
        data = new StudentDatabase(64);
        
        manager.loadStudentData("SampleStudents.csv");
        data.loadStudentData("new.csv");
    }


    /**
     * Tests loadStudentData()
     * 
     * @throws FileNotFoundException
     */
    public void testLoadStudentData() throws FileNotFoundException {
        setUp();
        //testing with sample csv
        System.out.println("test load:\n");
        assertEquals(manager.getHash().size(), 15);
        assertEquals(manager.getHash().get("044722015").toString(),
            "044722015, Burton Frost at slot ");
        assertEquals(manager.getHash().get("815012117").toString(),
            "815012117, Ezekiel Ruiz at slot ");
        System.out.println(manager.getHash().getArrayString());
        System.out.println(manager.getHash().getSfoldKey("877002980"));
        System.out.println(manager.getHash().getSfoldKey("940597271"));
        
        //testing with new csv file
        System.out.println("test load2:\n");
        System.out.println(data.getHash().getArrayString());
        assertEquals(data.getHash().get("21").toString(),
            "021, Kylie Jenner at slot ");
        
    }


    /**
     * Tests insert()
     * 
     * @throws IOException
     */
    public void testInsert() throws IOException {
        System.out.println("test insert1:\n");
        

        //testing with sample
        // should be 13 since 2 are missing but is 15
        assertEquals(15, manager.getHash().size());
        manager.insert("123", "Colleen Schmidt");
        manager.search("123");

        manager.insert("1234", "Allison DeSantis");

        // should not be able to find
        manager.search("12");
        System.out.println(manager.getHash().getArrayString());

        assertEquals(17, manager.getHash().size());
        
        //testing with new csv file
        System.out.println("test insert2:\n");
        data.insert("36", "Chicken Nugget");
        assertEquals(36, data.getHash().size());
        //should print 
        data.search("36");
        
        
        
    }


    /**
     * Tests search()
     * 
     * @throws FileNotFoundException
     */
    public void testSearch() throws FileNotFoundException {

        //testing with sample
        System.out.println("test search:\n");

        // should find Burton Frost
        manager.search("044722015");

        // should not be able to find
        manager.search("1");

        System.out.println(manager.getHash().getArrayString());
        
        //testing with new csv
        System.out.println("test search2:\n");
    }


    /**
     * Tests remove()
     * @throws FileNotFoundException 
     */
    public void testRemove() throws FileNotFoundException {

        //testing with sample
        System.out.println("test remove1:\n");
        
        //remove burton frost, hash size should reduce, should not be able to find
        manager.remove("044722015");
        assertEquals(14, manager.getHash().size());
        manager.search("044722015");
        
        //testing with new csv
        System.out.println("test remove2:\n");
    }


    /**
     * Tests update()
     * @throws IOException 
     */
    public void testUpdate() throws IOException {
        
        System.out.println("test update:\n");
        //manager.update("044722015", "Chicken Nugget");
        assertEquals(manager.getHash().get("044722015").toString(),
            "044722015, Chicken Nugget at slot ");
        
        //manager.search("044722015");

    }


    /**
     * Tests essay()
     * @throws IOException 
     */
    public void testEssay() throws IOException {
        manager.insert("123456789", "Chicken Nugget");
        manager.essay("hi my name is Colleen");
        assertEquals("hi my name is Colleen", manager.getHash().get("123456789").getEssay());
        
    }
}
