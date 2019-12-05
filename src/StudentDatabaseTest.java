import java.io.FileNotFoundException;
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
     */
    public void setUp() {
        manager = new StudentDatabase(32);
    }

    /**
     * Tests loadStudentData()
     * @throws FileNotFoundException
     */
    public void testLoadStudentData() throws FileNotFoundException {
        setUp();
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
     */
    public void testInsert() {

    }

    /**
     * Tests search()
     */
    public void testSearch() {

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