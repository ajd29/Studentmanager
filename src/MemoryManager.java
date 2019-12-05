import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 *  Represents a memory manager
 *
 *  @author ajd29
 *  @author collee57
 *  @version Nov 27, 2019
 */
public class MemoryManager
{
    // Linked lists for free and used memory
    private LinkedList<Student> freeList;
    private LinkedList<Student> usedList;

    // Memory file
    private RandomAccessFile memFile;

    // Length of student record's name
    private int nLength;

    // Length of student record's essay
    private int eLength;

    /**
     * Creates a new memory manager
     * @throws FileNotFoundException
     */
    public MemoryManager() throws FileNotFoundException {
        freeList = new LinkedList<Student>();
        usedList = new LinkedList<Student>();
        memFile = new RandomAccessFile("memoryFile", "rw");
    }

    /**
     * Stores record in memory manager
     * @param student
     */
    public void storeRecord(Student student) {
        nLength = student.getName().length();
        eLength = student.getEssay().length();
    }

    /**
     * Requests record from memory manager
     * @param record to request from memory manager
     */
    public void requestRecord(Student record) {
        nLength = record.getName().length();
        eLength = record.getEssay().length();
    }

    /**
     * Merge free blocks into one
     */
    public void merge() {

    }
}
