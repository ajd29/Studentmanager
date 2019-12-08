import java.io.FileNotFoundException;
import java.io.IOException;

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

    // student objects
    Student stu;

    /**
     * Set up is called before every test
     * @throws FileNotFoundException
     */
    public void setUp() throws FileNotFoundException {
        manager = new StudentDatabase(64);
        stu = new Student("12345", "Allison", "DeSantis");
    }

    /**
     * Tests loadStudentData()
     * @throws IOException
     */
    public void testLoadStudentData() throws IOException {
        setUp();
        manager.loadStudentData("SampleStudents.csv");
        assertEquals(manager.getHash().size(), 15);
        assertEquals(manager.getHash().get("044722015").toString(),
            "Burton Frost at slot ");
        assertEquals(manager.getHash().get("815012117").toString(),
            "Ezekiel Ruiz at slot ");
        assertEquals(manager.getHash().get("711016998").toString(),
            "Zeph Serrano at slot ");
        assertEquals(manager.getHash().get("440978815").toString(),
            "Raphael Kramer at slot ");
        assertEquals(manager.getHash().get("477937023").toString(),
            "Fuller Frye at slot ");

        System.out.println("Fuller's sfold: " +
            manager.getHash().getSfoldKey("477937023"));
        System.out.println("Fuller's index: " +
            manager.getHash().getIndex("477937023"));

        System.out.println("Rooney's sfold: " +
            manager.getHash().getSfoldKey("770218626"));
        System.out.println("Rooney's index: " +
            manager.getHash().getIndex("770218626"));

        System.out.println("Burton's sfold: " +
            manager.getHash().getSfoldKey("044722015"));
        System.out.println("Burton's index: " +
            manager.getHash().getIndex("044722015"));

        assertEquals(manager.getHash().size(), 15);
        System.out.println(manager.getHash().getArrayString());
    }


    /**
     * Tests insert()
     * @throws IOException
     */
    public void testInsert() throws IOException {
        setUp();

        // insert student to hash
        manager.insert("12345", "Allison", "DeSantis");
        assertEquals(manager.getHash().size(), 1);

        // check that correct size was written to memory file
        int length = stu.getName().getBytes().length;
        assertEquals(manager.getMem().getMemFile().length(), length);
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
     * @throws IOException
     */
    public void testEssay() throws IOException {
        setUp();

        // insert student to hash
        manager.insert("12345", "Allison", "DeSantis");
        assertEquals(manager.getHash().size(), 1);

        // check that correct size was written to memory file
        int length = stu.getName().getBytes().length;
        assertEquals(manager.getMem().getMemFile().length(), length);

        // give student essay
        length += stu.getEssay().getBytes().length;
        manager.essay("first essay for a student\n" + "second line\n");
        assertEquals(manager.getMem().getMemFile().length(), length);
    }
}
