import java.io.IOException;

/**
 * Tests the methods in the MemoryManager class
 *
 * @author ajd29 Allison DeSantis
 * @author collee57 Colleen Schmidt
 * @version Dec 7, 2019
 */
public class MemoryManagerTest extends student.TestCase {
    // private memory manager object
    private MemoryManager manage;

    // private memory handle objects
    private MemoryHandle mem1;
    // private memory handle objects
    private MemoryHandle mem3;
    // private memory handle objects
    private MemoryHandle mem8;
    // private memory handle objects
    private MemoryHandle memName;

    // private student object
    private Student stu;
    // private student object
    private Student student;


    /**
     * Set up method called before every test
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        manage = new MemoryManager("mFile");
        mem3 = new MemoryHandle(3, 5);
        mem8 = new MemoryHandle(8, 10);
        mem1 = new MemoryHandle(1, 2);
        memName = new MemoryHandle(0, 7);
        stu = new Student("123", "allison", "desantis");
        student = new Student("122", "colleen", "schmidt");
        manage.getMemFile().setLength(0);
    }


    /**
     * Tests storeRecord()
     * 
     * @throws IOException
     */
    public void testStoreRecord() throws IOException {
        setUp();
        manage.storeRecord(stu);
        assertEquals(manage.getMemFile().length(), 16);
        stu.setEssay("\nstudent's new essay &8fdkjal\n");
        manage.storeRecord(stu);

        student.setEssay("already has essay");
        manage.storeRecord(student);

        // free block by changing essay
        stu.setEssay("short essay");
        manage.updateEssay(stu);

        // free block by changing name
        stu.setName("n", "n");
        manage.updateName(stu);

        // free block for essay
        student.setEssay("short");
        manage.storeRecord(student);
        assertEquals(manage.getFreeList().size(), 2);
    }


    /**
     * Tests updateName()
     * 
     * @throws IOException
     */
    public void testUpdateName() throws IOException {
        setUp();
        manage.storeRecord(stu);
        stu.setName("diff", "name");
        manage.updateName(stu);
        assertEquals(manage.getFreeList().size(), 0);

        manage.storeRecord(student);

        // writes new name in free block
        stu.setName("n", "name");
        manage.updateName(stu);

        assertEquals(manage.getFreeList().size(), 1);
    }


    /**
     * Tests updateEssay()
     * 
     * @throws IOException
     */
    public void testUpdateEssay() throws IOException {
        setUp();
        manage.storeRecord(stu);
        stu.setEssay("\nnewessay for student\nfdjksl");
        manage.updateEssay(stu);
        assertNotNull(stu.getEssayHandle());

        stu.setEssay("shorter essay");
        manage.updateEssay(stu);
        assertEquals(stu.getEssayHandle().getLength(), 13);

        // store second record with essay
        student.setEssay("student's essay");
        manage.storeRecord(student);

        // clear first essay
        manage.clearEssay(stu);

        stu.setEssay("shortessay");
        manage.updateEssay(stu);

        assertEquals(manage.getFreeList().size(), 1);
    }


    /**
     * Tests clearEssay()
     * 
     * @throws IOException
     */
    public void testClearEssay() throws IOException {
        setUp();
        manage.storeRecord(stu);
        manage.clearEssay(stu);
        assertEquals(manage.getFreeList().size(), 0);

        stu.setEssay("essay for student");
        manage.updateEssay(stu);
        manage.clearEssay(stu);
        assertEquals(manage.getFreeList().size(), 0);
        assertNull(stu.getEssayHandle());
    }


    /**
     * Tests removeRecord()
     * 
     * @throws IOException
     */
    public void testRemoveRecord() throws IOException {
        setUp();
        manage.storeRecord(stu);
        manage.removeRecord(stu);

        // end of file
        assertEquals(manage.getFreeList().size(), 0);
        stu.setEssay("now has essay");
        manage.storeRecord(stu);
        assertEquals(manage.getFreeList().size(), 0);
        assertEquals(manage.getMemFile().length(), 29);
        manage.removeRecord(stu);
        assertEquals(manage.getFreeList().size(), 0);
    }


    /**
     * Tests addFreeBlock()
     * 
     * @throws IOException
     */
    public void testAddFreeBlock() throws IOException {
        setUp();

        manage.addFreeBlock(mem8);
        assertEquals(manage.getFreeList().size(), 1);
        assertEquals(manage.getFreeList().get(0).getPos(), 8);

        manage.addFreeBlock(mem3);
        assertEquals(manage.getFreeList().size(), 2);
        assertEquals(manage.getFreeList().get(0).getPos(), 3);

        manage.addFreeBlock(mem1);
        assertEquals(manage.getFreeList().size(), 3);
        assertEquals(manage.getFreeList().get(0).getPos(), 1);
    }


    /**
     * Tests mergeFree()
     * 
     * @throws IOException
     */
    public void testMergeFree() throws IOException {
        setUp();

        // free block starting at byte 8
        manage.addFreeBlock(mem8);

        // free block starting at byte 3
        manage.addFreeBlock(mem3);

        // free block starting at byte 1
        manage.addFreeBlock(mem1);

        assertEquals(manage.getFreeList().get(1).getPos(), 3);

        manage.mergeFree();
        assertEquals(manage.getFreeList().size(), 1);

        manage.addFreeBlock(mem8);
        manage.addFreeBlock(mem1);
        manage.mergeFree();
        assertEquals(manage.getFreeList().size(), 3);
    }


    /**
     * Tests mergeBlocks()
     * 
     * @throws IOException
     */
    public void testMergeBlocks() throws IOException {
        setUp();

        // only one block
        manage.addFreeBlock(mem8);
        manage.mergeBlocks();
        assertEquals(manage.getFreeList().size(), 1);

        // not valid merge
        manage.addFreeBlock(mem1);
        manage.mergeBlocks();
        assertEquals(manage.getFreeList().size(), 2);
    }


    /**
     * Tests canMerge()
     * 
     * @throws IOException
     */
    public void testCanMerge() throws IOException {
        setUp();
        manage.addFreeBlock(mem8);
        manage.addFreeBlock(mem3);
        manage.addFreeBlock(mem1);

        assertTrue(manage.canMerge());
        manage.mergeFree();
        assertFalse(manage.canMerge());

        assertEquals(manage.getFreeList().size(), 1);

        manage.addFreeBlock(mem8);
        assertFalse(manage.canMerge());
    }


    /**
     * Tests removeFreeBlock()
     * 
     * @throws IOException
     */
    public void testRemoveFreeBlock() throws IOException {
        setUp();
        manage.addFreeBlock(mem8);
        String name = "allison";
        byte[] array = name.getBytes();
        manage.removeFreeBlock(mem8, array);
        assertEquals(manage.getFreeList().size(), 1);
        assertEquals(manage.getFreeList().get(0).getPos(), 15);
        assertEquals(manage.getFreeList().get(0).getLength(), 3);

        manage.addFreeBlock(mem1);
        manage.removeFreeBlock(mem1, array);
        assertEquals(manage.getFreeList().size(), 1);

    }


    /**
     * Tests anyFreeBlocks()
     * 
     * @throws IOException
     */
    public void testAnyFreeBlocks() throws IOException {
        setUp();

        String name = "allison";
        byte[] array = name.getBytes();
        assertFalse(manage.anyFreeBlocks(array));

        manage.addFreeBlock(mem8);
        assertTrue(manage.anyFreeBlocks(array));

        manage.removeFreeBlock(mem8, array);
        assertFalse(manage.anyFreeBlocks(array));
    }


    /**
     * Tests findFirstFree()
     * 
     * @throws IOException
     */
    public void testFindFirstFree() throws IOException {
        setUp();

        String name = "allison";
        byte[] array = name.getBytes();

        manage.addFreeBlock(mem1);
        manage.addFreeBlock(mem8);

        assertEquals(manage.findFirstFree(array).getPos(), 8);
    }


    /**
     * Tests freeBlock()
     * 
     * @throws IOException
     */
    public void testFreeBlock() throws IOException {
        setUp();

        String name = "allison";
        byte[] array = name.getBytes();
        manage.getMemFile().write(array);

        manage.freeBlock(memName);

        assertEquals(memName.getLength(), 7);
        assertEquals(manage.getMemFile().getFilePointer(), 0);

        assertEquals(manage.getFreeList().size(), 0);

        String bob = "bob";
        manage.removeFreeBlock(memName, bob.getBytes());

        assertEquals(manage.getFreeList().size(), 1);
        assertEquals(manage.getFreeList().get(0).getPos(), 3);
        assertEquals(manage.getFreeList().get(0).getLength(), 4);
    }


    /**
     * Tests lastBlockFree()
     * 
     * @throws IOException
     */
    public void testLastBlockFree() throws IOException {
        setUp();

        String name = "allison";
        byte[] array = name.getBytes();

        manage.getMemFile().write(array);
        manage.getMemFile().write(array);

        manage.freeBlock(memName);
        assertFalse(manage.isLastBlockFree());

        manage.freeBlock(new MemoryHandle(7, 7));
        assertEquals(manage.getFreeList().size(), 0);
    }


    /**
     * Tests printFreeList()
     * 
     * @throws IOException
     */
    public void testPrintFreeList() throws IOException {
        setUp();

        manage.addFreeBlock(mem1);
        manage.addFreeBlock(mem3);

        manage.printFreeList();
        assertEquals(manage.getFreeList().size(), 2);

        manage.mergeFree();
        manage.printFreeList();
        assertEquals(manage.getFreeList().size(), 1);
    }
}
