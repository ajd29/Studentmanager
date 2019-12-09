/**
 * Tests for the Student class
 *
 * @author collee57
 * @author ajd29
 * @version 2019-10-26
 *
 */
public class StudentTest extends student.TestCase {

    // Student objects
    private Student s1;
    private Student s2;
    private Student s3;
    private Student s4;
    private int num;


    /**
     * Set up method
     */
    public void setUp() {
        s1 = new Student("12345678", "Colleen", "Schmidt");
        s2 = new Student("12345670", "Susan", "Pak");
        s3 = null;

        s4 = new Student("12345678", "Colon", "Schmidt");
        num = 9;
    }


    /**
     * Test method for getFirstName()
     *
     * @throws Exception
     */
    public void testGetFirstName() throws Exception {
        setUp();
        assertEquals(s1.getFirstName(), "Colleen");
        assertEquals(s2.getFirstName(), "Susan");

        Student blank = new Student("0", " ", " ");
        assertEquals(blank.getFirstName(), " ");

    }


    /**
     * Test method for getLastName()
     *
     * @throws Exception
     */
    public void testGetLastName() throws Exception {
        setUp();
        assertEquals(s1.getLastName(), "Schmidt");
        assertEquals(s2.getLastName(), "Pak");
        Student blank = new Student("0", "", "");
        assertEquals(blank.getLastName(), "");
    }


    /**
     * Tests getName() to ensure full name is returned
     *
     * @throws Exception
     */
    public void testGetName() throws Exception {
        setUp();
        Student student = new Student("111111111", "a", "a");
        // student.setMiddleName("b");

        assertEquals(student.getName(), "A A");
        assertEquals(student.getName().length(), 3);

        Student newStudent = new Student("111111111", "SusAN", "OBI");
        assertEquals(newStudent.getName(), "Susan Obi");

        Student blank = new Student("90", " ", "");
        assertEquals(blank.getName(), "  ");
    }


    /**
     * Test method for getSection()
     *
     * @throws Exception
     */
    public void testGetPID() throws Exception {
        setUp();

        Student stu = new Student("9", "b", "a");

        assertEquals(stu.getPID(), "9");
    }


    /**
     * Test method for setID()
     *
     * @throws Exception
     */
    public void testSetPID() throws Exception {
        setUp();

        s1.setPID("010001");
        assertEquals(s1.getPID(), "010001");

        s1.setPID("020002");
        assertEquals(s1.getPID(), "020002");
    }


    /**
     * Test method for equals()
     *
     * @throws Exception
     */
    public void testEquals() throws Exception {
        setUp();
        Student colleen = new Student("12345678", "collEEN", "SCHMIDt");

        assertTrue(colleen.equals(s1));

        assertTrue(s1.equals(s4));
        assertFalse(s1.equals(s2));
        assertFalse(s1.equals(s3));
        assertTrue(s1.equals(s1));

        String allison = "";
        assertFalse(allison.equals(colleen));

        assertFalse(colleen.equals(num));
    }


    /**
     * Tests the student's compareTo method
     * 
     * @throws Exception
     */
    public void testCompareTo() throws Exception {
        setUp();
        Student allison = new Student("0", "Allison", "DeSantis");
        Student alli = new Student("0", "Aaa", "DeSantis");

        // same last name, returns 1
        assertEquals(allison.compareTo(alli), 1);

        // same last name, returns -1
        Student al = new Student("0", "z", "DeSantis");
        assertEquals(allison.compareTo(al), -1);

        // same name
        Student allis = new Student("0", "allison", "DeSantis");
        assertEquals(allison.compareTo(allis), 0);

        // same first name, returns 1
        Student last = new Student("0", "Allison", "c");
        assertEquals(allison.compareTo(last), 1);

        // same first name, returns -1
        Student diff = new Student("0", "Allison", "Z");
        assertEquals(allison.compareTo(diff), -1);

        Student c1 = new Student("0", "Colleen", "Schmidt");
        // c1.setMiddleName("Elizabeth");
        Student c2 = new Student("0", "Colleen", "Schmidt");
        // c2.setMiddleName("Jane");

        assertEquals(c1.compareTo(c2), 0);

        assertEquals(c2.compareTo(c1), 0);

        Student c3 = new Student("0", "Colleen", "Schmidt");
        // c3.setMiddleName("Elizabeth");
        assertEquals(c1.compareTo(c3), 0);

    }


    /**
     * Tests the getEssay() method
     */
    public void testGetEssay() {
        assertEquals(s1.getEssay(), "");
        s1.setEssay("this student's essay");
        assertEquals(s1.getEssay(), "this student's essay");
    }


    /**
     * Tests the setEssay() method
     */
    public void testSetEssay() {
        s1.setEssay("new essay for s1");
        assertEquals(s1.getEssay(), "new essay for s1");
    }
}
