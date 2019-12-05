import java.io.FileNotFoundException;

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
        manager.loadStudentData("SampleStudents_p4.csv");
        assertEquals(manager.getHash().size(), 15);
        assertEquals(manager.getHash().get("044722015").toString(),
            "044722015, Burton Frost, score = 0");
        assertEquals(manager.getHash().get("161829536").toString(),
            "161829536, Melinda Chen, score = 0");
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
