import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import com.sun.media.util.ByteBuffer;

/**
 * Represents a memory manager
 *
 * @author ajd29
 * @author collee57
 * @version Nov 27, 2019
 */
public class MemoryManager {
    
    // Linked list for tracking free and used blocks of memory
    private LinkedList<MemoryHandle> freeList;

    // Memory file
    private RandomAccessFile memFile;

    // Length of student record's name
    private int nLength;

    // Length of student record's essay
    private int eLength;
    
    // name buffer
    private ByteBuffer nBuffer; 
    
    // essay buffer
    private ByteBuffer eBuffer;


    /**
     * Creates a new memory manager
     * 
     * @throws FileNotFoundException
     */
    public MemoryManager() throws FileNotFoundException {
        freeList = new LinkedList<MemoryHandle>();
        memFile = new RandomAccessFile("memoryFile", "rw");
    }


    /**
     * Writes record to memory file, retrieves memory handles
     * 
     * @param student
     * @throws IOException 
     */
    public void storeRecord(Student student) throws IOException {
        
        nLength = student.getName().length();
        eLength = student.getEssay().length();

        byte[] nameArray = student.getName().getBytes();
        byte[] essayArray = student.getEssay().getBytes();
        
        if (freeList.size() == 0) {     
            int nPos = (int)memFile.getFilePointer();
            memFile.write(nameArray);
            student.setNameHandle(nPos, nameArray.length);
            
            int ePos = (int)memFile.getFilePointer();
            memFile.write(essayArray);
            student.setEssayHandle(ePos, essayArray.length);
        }
        else {
            int lowestPos = freeList.get(0).getPos();
            
            for (int i = 0; i < freeList.size(); i++) {
                if (freeList.get(i).getPos() < lowestPos && 
                    freeList.get(i).getLength() >= (nameArray.length + essayArray.length)) {
                    lowestPos = freeList.get(i).getPos();
                }
            }           
            memFile.seek(lowestPos);
            
            memFile.write(nameArray);
            student.setNameHandle(lowestPos, nameArray.length);
            
            int ePos = (int)memFile.getFilePointer();
            memFile.write(essayArray);
            student.setEssayHandle(ePos, essayArray.length);
        }
    }


    /**
     * Requests record from memory manager
     * 
     * @param record
     *            to request from memory manager
     */
    public void requestRecord(Student record) {
        nLength = record.getName().length();
        eLength = record.getEssay().length();
    }
}
