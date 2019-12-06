import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 *  Tests the methods of the StudentDatabase class
 *
 *  @author ajd29
 *  @author collee57
 *  @version Dec 3, 2019
 */
public class StudentDatabaseTest extends student.TestCase
{
    // StudentDatabase object
    private StudentDatabase manager;
    
    
    
    /**
     * Set up is called before every test
     * @throws FileNotFoundException 
     */
    public void setUp() throws FileNotFoundException {
        manager = new StudentDatabase(32);
        
    }

    /**
     * Tests loadStudentData()
     * @throws FileNotFoundException
     */
    public void testLoadStudentData() throws FileNotFoundException {
        setUp();
        System.out.println("test load:");
        manager.loadStudentData("SampleStudents.csv");
        assertEquals(manager.getHash().size(), 15);
        assertEquals(manager.getHash().get("044722015").toString(),
            "044722015, Burton Frost at slot ");
        assertEquals(manager.getHash().get("815012117").toString(),
            "815012117, Ezekiel Ruiz at slot ");
        System.out.println(manager.getHash().getArrayString());
        System.out.println(manager.getHash().getSfoldKey("877002980"));
        System.out.println(manager.getHash().getSfoldKey("940597271"));
    }

    /**
     * Tests insert()
     * @throws IOException 
     */
    public void testInsert() throws IOException {
        System.out.println("test insert:");
        manager.loadStudentData("SampleStudents.csv");
        
        //should be 13 since 2 are missing but is 15
        assertEquals(15, manager.getHash().size());
        manager.insert("123", "Colleen Schmidt");
        manager.search("123");
        
        manager.insert("1234", "Allison DeSantis");
        //should not be able to find
        manager.search("12");
        System.out.println(manager.getHash().getArrayString());
        
        assertEquals(17, manager.getHash().size());
    }

    /**
     * Tests search()
     * @throws FileNotFoundException 
     */
    public void testSearch() throws FileNotFoundException {

        System.out.println("test search:");
        manager.loadStudentData("SampleStudents.csv");
        manager.search("044722015");
        System.out.println(manager.getHash().getArrayString());
    }

    /**
     * Tests remove()
     */
    public void testRemove() {

    }

    /**
     * Tests update()
     */
    public void testUpdate() {

    }

    /**
     * Tests essay()
     */
    public void testEssay() {

    }
}