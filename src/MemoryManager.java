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
public class MemoryManager
{

    // Linked list for tracking free blocks of memory
    private LinkedList<MemoryHandle> freeList;

    // Memory file to write to
    private RandomAccessFile         memFile;


    /**
     * Creates a new memory manager
     *
     * @throws FileNotFoundException
     */
    public MemoryManager()
        throws FileNotFoundException
    {
        freeList = new LinkedList<MemoryHandle>();
        memFile = new RandomAccessFile("memoryFile", "rw");
    }


    /**
     * Return free list
     *
     * @return LinkedList<MemoryHandle> free list
     */
    public LinkedList<MemoryHandle> getFreeList()
    {
        return freeList;
    }


    /**
     * Return memory file
     *
     * @return RandomAccessFile memory file
     */
    public RandomAccessFile getMemFile()
    {
        return memFile;
    }


    /**
     * Writes student record to memory file
     *
     * @param student
     * @throws IOException
     */
    public void storeRecord(Student student)
        throws IOException
    {
        byte[] nameArray = student.getName().getBytes();
        byte[] essayArray = student.getEssay().getBytes();

        int freePos;

        // write name
        if (anyFreeBlocks(nameArray))
        {
            MemoryHandle free = findFirstFree(nameArray);
            freePos = free.getPos();
            removeFreeBlock(free, nameArray);
        }
        else
        {
            freePos = (int)memFile.length();
        }
        memFile.seek(freePos);
        memFile.write(nameArray);
        student.setNameHandle(freePos, nameArray.length);

        // write essay if it exists
        if (student.getEssay().length() > 0)
        {
            if (anyFreeBlocks(essayArray))
            {
                MemoryHandle free = findFirstFree(essayArray);
                freePos = free.getPos();
                removeFreeBlock(free, essayArray);
            }
            else
            {
                freePos = (int)memFile.length();
            }
            memFile.seek(freePos);
            memFile.write(essayArray);
            student.setEssayHandle(freePos, essayArray.length);
        }
    }


    /**
     * Updates a student's name
     *
     * @param student
     *            to write to memory file
     * @throws IOException
     */
    public void updateName(Student student)
        throws IOException
    {
        byte[] nameArray = student.getName().getBytes();

        // free block where old name was
        freeBlock(student.getNameHandle());

        int pos;
        if (anyFreeBlocks(nameArray))
        {
            MemoryHandle free = findFirstFree(nameArray);
            pos = free.getPos();

            // remove free block, deal with leftover free space
            removeFreeBlock(free, nameArray);
        }
        else
        {
            pos = (int)memFile.length();
        }

        // write in free block and reset name handle
        memFile.seek(pos);
        memFile.write(nameArray);
        student.setNameHandle(pos, nameArray.length);
    }


    /**
     * Updates a student's essay
     *
     * @param student
     *            to store essay for
     * @throws IOException
     */
    public void updateEssay(Student student)
        throws IOException
    {
        byte[] essayArray = student.getEssay().getBytes();

        // free block where old essay was if there was an essay
        if (student.getEssayHandle() != null)
        {
            freeBlock(student.getEssayHandle());
        }

        int pos;
        if (anyFreeBlocks(essayArray))
        {
            MemoryHandle free = findFirstFree(essayArray);
            pos = free.getPos();
            removeFreeBlock(free, essayArray);
        }
        else
        {
            pos = (int)memFile.length();
        }
        memFile.seek(pos);
        memFile.write(essayArray);
        student.setEssayHandle(pos, essayArray.length);
    }


    /**
     * Removes a record from memory file
     *
     * @param student
     *            to remove from memory file
     * @throws IOException
     */
    public void removeRecord(Student student)
        throws IOException
    {
        freeBlock(student.getNameHandle());

        // seek to where essay starts
        if (student.getEssayHandle() != null)
        {
            freeBlock(student.getEssayHandle());
        }
    }


    /**
     * Clears a student's essay from memory file
     *
     * @param student
     *            to clear essay of
     * @throws IOException
     */
    public void clearEssay(Student student)
        throws IOException
    {
        if (student.getEssayHandle() != null)
        {
            freeBlock(student.getEssayHandle());
        }
    }


    /**
     * Finds first free block that is large enough for the given array of bytes
     * to be added
     *
     * @param array
     *            of bytes
     * @return MemoryHandle of first valid free block
     */
    public MemoryHandle findFirstFree(byte[] array)
    {
        // get position in bytes of a free block
        int lowestPos = freeList.get(0).getPos();
        MemoryHandle freeBlock = freeList.get(0);

        for (int i = 0; i < freeList.size(); i++)
        {
            if (freeList.get(i).getPos() < lowestPos
                && freeList.get(i).getLength() >= array.length)
            {
                lowestPos = freeList.get(i).getPos();
                freeBlock = freeList.get(i);
            }
        }
        return freeBlock;
    }


    /**
     * Checks if there is a free block big enough for the size in free list
     *
     * @param array
     *            of bytes to be added
     * @return boolean true if there is a valid free block
     */
    public boolean anyFreeBlocks(byte[] array)
    {
        boolean result = false;

        // if there are no free blocks
        if (freeList.size() == 0)
        {
            return result;
        }

        // check if there even is big enough free block
        for (int i = 0; i < freeList.size(); i++)
        {
            if (freeList.get(i).getLength() >= array.length)
            {
                result = true;
            }
        }
        return result;
    }


    /**
     * Add a free block to the free list in order of byte position in memory
     * file
     *
     * @param freeBlock
     *            to add to free list
     */
    public void addFreeBlock(MemoryHandle freeBlock)
    {
        int index = 0;
        for (int i = 0; i < freeList.size(); i++)
        {
            if (freeBlock.getPos() > freeList.get(i).getPos())
            {
                index++;
            }
        }
        freeList.add(index, freeBlock);
    }


    /**
     * Clears the data in the memory file from the start position to the end
     * position
     *
     * @param handle
     * @throws IOException
     */
    public void freeBlock(MemoryHandle handle)
        throws IOException
    {
        String blank = new String();

        int end = handle.getPos() + handle.getLength();
        int length = end - handle.getPos();

        for (int i = 0; i < length; i++)
        {
            blank += blank;
        }

        byte[] blanks = blank.getBytes();
        memFile.seek(handle.getPos());
        memFile.write(blanks);

        // add freed block to free list
        addFreeBlock(handle);
        mergeFree();

        System.out.println("size of free list " + freeList.size());
    }


    /**
     * Removes free block to be written to from list
     *
     * @param free
     *            block to write to, then remove
     * @param array
     *            of bytes to write in free block
     */
    public void removeFreeBlock(MemoryHandle free, byte[] array)
    {

        if (free.getLength() > array.length)
        {

            int position = free.getPos() + array.length;
            int len = free.getLength() - array.length;
            MemoryHandle leftover = new MemoryHandle(position, len);

            // add leftover free space
            addFreeBlock(leftover);
        }
        // remove free block
        freeList.remove(free);
    }


    /**
     * Merge free blocks
     */
    public void mergeFree()
    {
        while (canMerge())
        {
            mergeBlocks();
        }
    }

    /**
     * Merges two free blocks into one block
     */
    public void mergeBlocks() {
        for (int i = 0; i < freeList.size(); i++) {

            int firstPos = freeList.get(i).getPos();
            int end = firstPos + freeList.get(i).getLength();

            if (i + 1 < freeList.size() &&
                end == freeList.get(i + 1).getPos()) {

                int length = freeList.get(i).getLength()
                    + freeList.get(i + 1).getLength();

                MemoryHandle merged = new MemoryHandle(firstPos, length);

                freeList.remove(i);
                freeList.remove(i + 1);
                addFreeBlock(merged);
            }
        }
    }

    /**
     * Returns true if blocks can be merged in free list
     *
     * @return true if blocks in free list can be merged, false if not
     */
    public boolean canMerge()
    {
        for (int i = 0; i < freeList.size(); i++)
        {
            int end = freeList.get(i).getPos() + freeList.get(i).getLength();

            // if the next block's position is the end of the block
            if (i + 1 < freeList.size() && end == freeList.get(i + 1).getPos())
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Print free blocks in order they are in free list
     */
    public void printFreeList()
    {
        String result = "Free Block List:";
        for (int i = 0; i < freeList.size(); i++)
        {
            result += "\nFree Block " + i + " starts from Byte "
                + freeList.get(i).getPos() + " with length "
                + freeList.get(i).getLength() + "\n";
        }
        System.out.println(result);
    }
}
