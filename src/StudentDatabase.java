import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 *  Represents a MemoryManager object
 *  Contains commands
 *
 *  @author ajd29
 *  @author collee57
 *  @version Nov 27, 2019
 */
public class StudentDatabase
{
    // HashTable object
    private HashTable hash = new HashTable<String, MemHandle>();

    // true if update command just called
    private boolean update;

    // student of the previous command
    private Student currStudent;


    /**
     * Creates a new MemoryManager object
     * @param table to insert, search, and remove from
     */
    public StudentDatabase() {

    }

    /**
     * Loads student data from text file
     * @param fileName name of file to load
     * @throws FileNotFoundException
     */
    public void loadStudentData(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        Scanner in = new Scanner(file);

        while (in.hasNextLine())
        {
            String line = in.nextLine().trim();

            if (!line.equals(",,,") && !line.equals("") && !line.isEmpty()
                && !line.equals(",,,,,"))
            {
                String[] input = line.split(",\\s*");

                String p = input[0];
                if (input[0].length() < 9)
                {
                    p = "0" + input[0];
                }

                String first = input[1];
                String middle = input[2];
                String last = input[3];

                Student s = new Student(p, first, last);
                if (middle.length() > 0)
                {
                    s.setMiddleName(middle);
                }
                // add to hash once HashTable's insert method is implemented
            }
        }
        System.out.println(fileName + " successfully loaded");
    }

    /**
     * Insert student into hash table
     * @param pid student's PID
     * @param fullName student's full name
     */
    public void insert(String pid, String fullName) {
        // insert student to hash
        // set current student
        // record the length of full name and essay
        int length = fullName.length();
        
        //might need another object instead of memory handle?? something to hold both memory handles
        //need to add position of name, using 0 as placeholder
        MemHandle name = new MemHandle(0, length);
        hash.add(pid, length);

        // use length to allocate a buffer into which
        // the record can be read and then store it into
        // the memory file and receive corresponding handles
        
        // byte buffer??
    }

    /**
     * Update a student's info
     * If PID does not exist, it
     * will be inserted
     * @param fullName to update name to
     */
    public void update(String pid, String fullName) {
        // if hash.search PID is null
            // insert into hash
        
        int length = fullName.length();
        //again, need position
        MemHandle name = new MemHandle(0, length);
        
        if (hash.get(pid)== null) {
            
            hash.add(pid, name);
        }
     // else if it's not null
        // update with full name
        else {
            hash.update(pid, name);
            update = true;
        }
        
        
        // set current Student
    }

    /**
     * Remove student from hash table
     * @param pid to remove
     */
    public void remove(String pid) {
        // if hash.search PID is not null
            // remove from hash
        // print pid with full name <full name> is removed
        // from the database.
        // else print
        System.out.println(pid +
            " is not found in the database.");
    }

    /**
     * Will immediately follow successful insert
     * or update command; the corresponding text will
     * be associated with the PID of the previous command
     * and stored as a byte array following UTF-8 encoding
     *
     */
    public void essay(String essay) {
        // if essay commands follow update
        // the message will replace the previous
        // essay associated with that PID

        currStudent.setEssay(essay);
    }

    /**
     * Remove the essay associated with the PID
     * @param pid to clear essay of
     */
    public void clear(String pid) {
        // if hash.search(pid) is null
        System.out.println(pid + " is not found in the database.");

        // else
        // print record with <pid> with full name <full name> is cleared.
    }

    /**
     * Prints out info associated with PID if there
     * is one stored in the database
     * @param pid of student to search for
     */
    public void search(String pid) {
        // if hash.search(PID) is not null
            // print <PID> <full name>, then essay on next line
    }

    /**
     * Print out a list of all records in the database
     */
    public void print() {
        // print <PID> <full name> for each
        // then indicate which slot of the hash
        // table it is stored in
        // print out a list of the free blocks currently
        // in the file, for each free block indicate its
        // starting byte position and its size, list blocks
        // in order from lowest to highest in order of byte
        // position (same order as they are in freelist)
    }
}
