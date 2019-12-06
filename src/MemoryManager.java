import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
/**
 * Represents a memory manager
 *
 * @author ajd29
 * @author collee57
 * @version Nov 27, 2019
 */
public class MemoryManager {

    // Linked list for tracking free blocks of memory
    private LinkedList<MemoryHandle> freeList;

    // Memory file to write to
    private RandomAccessFile memFile;

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
     * Writes student record to memory file
     *
     * @param student
     * @throws IOException
     */
    public void storeRecord(Student student) throws IOException {

        byte[] nameArray = student.getName().getBytes();
        byte[] essayArray = student.getEssay().getBytes();

        int freePos;

        // write name
        if (anyFreeBlocks(nameArray.length)) {
            freePos = findFirstFree(nameArray.length);
        }
        else {
            freePos = (int) memFile.length();
        }
        memFile.seek(freePos);
        memFile.write(nameArray);
        student.setNameHandle(freePos, nameArray.length);

        // write essay if it exists
        if (student.getEssay().length() > 0) {
            if (anyFreeBlocks(essayArray.length)) {
                freePos = findFirstFree(essayArray.length);
            }
            else {
                freePos = (int) memFile.length();
            }
            memFile.seek(freePos);
            memFile.write(essayArray);
            student.setNameHandle(freePos, essayArray.length);
        }
    }

    /**
     * Updates a student's name
     * @param student to write to memory file
     * @throws IOException
     */
    public void updateName(Student student) throws IOException {

        byte[] nameArray = student.getName().getBytes();

        // free block where old name was
        freeBlock(student.getNameHandle());

        // add to free list
        freeList.add(student.getNameHandle());

        int pos;
        if (anyFreeBlocks(nameArray.length)) {
            pos = findFirstFree(nameArray.length);

        }
        else {
            pos = (int) memFile.length();
        }
        memFile.seek(pos);
        memFile.write(nameArray);
        student.setNameHandle(pos, nameArray.length);
    }

    /**
     * Updates a student's essay
     * @param student to store essay for
     * @throws IOException
     */
    public void updateEssay(Student student) throws IOException {

        byte[] essayArray = student.getEssay().getBytes();

        // free block where old essay was if there was an essay
        if (student.getEssayHandle() != null) {

            freeBlock(student.getEssayHandle());
            freeList.add(student.getEssayHandle());
        }

        int pos;
        if (anyFreeBlocks(essayArray.length)) {
            pos = findFirstFree(essayArray.length);
        }
        else {
            pos = (int) memFile.length();
        }
        memFile.seek(pos);
        memFile.write(essayArray);
        student.setNameHandle(pos, essayArray.length);
    }

    /**
     * Removes a record from memory file
     *
     * @param student to remove from memory file
     * @throws IOException
     */
    public void removeRecord(Student student) throws IOException {

        // seek to where name starts, free block
        memFile.seek(student.getNameHandle().getPos());
        freeBlock(student.getNameHandle());
        freeList.add(student.getNameHandle());

        // seek to where essay starts
        memFile.seek(student.getEssayHandle().getPos());
        freeBlock(student.getEssayHandle());
        freeList.add(student.getEssayHandle());
    }

    /**
     * Clears a student's essay from memory file
     * @param student to clear essay of
     * @throws IOException
     */
    public void clearEssay(Student student) throws IOException {
        if (student.getEssayHandle() != null) {
            memFile.seek(student.getEssayHandle().getPos());
            freeBlock(student.getEssayHandle());
            freeList.add(student.getEssayHandle());
        }
    }

    /**
     * Finds first free block that is large enough
     * @param size of data to find free block for
     * @return int position of first valid free block
     */
    public int findFirstFree(int size) {

        // get position in bytes of a free block
        int lowestPos = freeList.get(0).getPos();

        for (int i = 0; i < freeList.size(); i++) {
            if (freeList.get(i).getPos() < lowestPos &&
                freeList.get(i).getLength() >= size) {
                lowestPos = freeList.get(i).getPos();
            }
        }
        return lowestPos;
    }

    /**
     * Checks if there is a free block big enough
     * for the size in free list
     * @param size of data to store in free block
     * @return boolean true if there is a valid free block
     */
    public boolean anyFreeBlocks(int size) {

        boolean result = false;

        // if there are no free blocks
        if (freeList.size() == 0) {
            return result;
        }

        // check if there even is big enough free block
        for (int i = 0; i < freeList.size(); i++) {
            if (freeList.get(i).getLength() >= size) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Clears the data in the memory file from
     * the start position to the end position
     * @param handle
     * @throws IOException
     */
    public void freeBlock(MemoryHandle handle) throws IOException {
        memFile.seek(handle.getPos());
        String blank = new String();
        int end = handle.getPos() + handle.getLength();
        while (handle.getPos() < end) {
            memFile.writeBytes(blank);
        }
        mergeFree();
    }

    /**
     * Merge free blocks
     */
    public void mergeFree() {

        for (int i = 0; i < freeList.size() - 1; i++) {
            // check if where a free block ends is one before
            // where another free block starts, merge them
            int end = freeList.get(i).getPos() + freeList.get(i).getLength();
            int start = freeList.get(i + 1).getPos();
            if (end == start) {
                freeList.remove(i);
                freeList.remove(i + 1);
                int length = freeList.get(i).getLength() +
                    freeList.get(i + 1).getLength();
                MemoryHandle mergedBlock =
                    new MemoryHandle(freeList.get(i).getPos(), length);
                freeList.add(mergedBlock);
            }
        }
    }
}
