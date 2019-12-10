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
     * @param fileName
     * @throws FileNotFoundException
     */
    public MemoryManager(String fileName)
        throws FileNotFoundException
    {
        freeList = new LinkedList<MemoryHandle>();
        memFile = new RandomAccessFile(fileName, "rw");
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
            byte[] essayArray = student.getEssay().getBytes();

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
            student.clearEssay();
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

            // sets handle to null
            student.clearEssay();
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
        MemoryHandle freeBlock = freeList.get(0);

        for (int i = 0; i < freeList.size(); i++)
        {
            if (freeList.get(i).getLength() >= array.length)
            {
                return freeList.get(i);
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
        // check if there even is big enough free block
        for (int i = 0; i < freeList.size(); i++)
        {
            if (freeList.get(i).getLength() >= array.length)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a free block to the free list in order of byte position in memory
     * file
     *
     * @param freeBlock
     *            to add to free list
     * @throws IOException
     */
    public void addFreeBlock(MemoryHandle freeBlock)
        throws IOException
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
        String blank = "";

        memFile.seek(handle.getPos());

        for (int i = 0; i < handle.getLength(); i++)
        {
            blank += " ";
        }

        memFile.write(blank.getBytes());

        // add freed block to free list
        addFreeBlock(handle);

        // merge possible free blocks at end
        mergeFree();

        // if last block is free, remove it shorten file length
        if (isLastBlockFree())
        {
            removeLastFree();
        }
    }

    /**
     * Removes free block to be written to from list
     *
     * @param free
     *            block to write to, then remove
     * @param array
     *            of bytes to write in free block
     * @throws IOException
     */
    public void removeFreeBlock(MemoryHandle free, byte[] array)
        throws IOException
    {
        // remove free block
        freeList.remove(free);

        int len = free.getLength() - array.length;

        if (len > 0)
        {
            int position = free.getPos() + array.length;
            MemoryHandle leftover = new MemoryHandle(position, len);

            // add leftover free space
            addFreeBlock(leftover);
        }
    }

    /**
     * Merge free blocks
     *
     * @throws IOException
     */
    public void mergeFree()
        throws IOException
    {
        while (canMerge())
        {
            mergeBlocks();
        }
    }

    /**
     * Merges two free blocks into one block
     *
     * @throws IOException
     */
    public void mergeBlocks()
        throws IOException
    {
        for (int i = 0; i < freeList.size(); i++) {
            int firstPos = freeList.get(i).getPos();
            int end = firstPos + freeList.get(i).getLength();

            if ((i + 1) < freeList.size() && end == freeList.get(i + 1).getPos())
            {
                int length =
                    freeList.get(i).getLength() + freeList.get(i + 1).getLength();

                MemoryHandle merged = new MemoryHandle(firstPos, length);

                freeList.remove(i);
                freeList.remove(i);
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
        if (freeList.size() < 2) {
            return false;
        }
        for (int i = 0; i < freeList.size(); i++)
        {
            int end = freeList.get(i).getPos() + freeList.get(i).getLength();

            // if the next block's position is the end of the block
            if ((i + 1) < freeList.size()
                && end == freeList.get(i + 1).getPos())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if last block in memory file is free
     *
     * @return true if last block is free
     * @throws IOException
     */
    public boolean isLastBlockFree()
        throws IOException
    {
        MemoryHandle lastFree = freeList.get(freeList.size() - 1);

        if (lastFree.getPos() + lastFree.getLength() == memFile.length())
        {
            return true;
        }
        return false;
    }

    /**
     * Removes last free block from list and shortens file length
     *
     * @throws IOException
     */
    public void removeLastFree()
        throws IOException
    {
        // shorten file length to position of last free block
        memFile.setLength(freeList.get(freeList.size() - 1).getPos());

        // remove last free block from free list
        freeList.remove(freeList.get(freeList.size() - 1));
    }

    /**
     * Print free blocks in order they are in free list
     */
    public void printFreeList()
    {
        String result = "Free Block List:";
        for (int i = 0; i < freeList.size(); i++)
        {
            result += "\nFree Block " + (i + 1) + " starts from Byte "
                + freeList.get(i).getPos() + " with length "
                + freeList.get(i).getLength();
        }
        System.out.println(result);
    }
}
