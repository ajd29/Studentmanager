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
    private HashTable<String, Student> hash;

    // true if update command just called
    private boolean update;

    // student of the previous command
    private Student currStudent;

    // byte position of student record in memory file
    private int position;


    /**
     * Creates a new StudentDatabase object
     * @param hashSize size of hash table
     */
    public StudentDatabase(int hashSize) {
        hash = new HashTable<String, Student>(hashSize);
    }

    /**
     * Return hash table
     * @return HashTable<String, Student> hash table
     */
    public HashTable<String, Student> getHash() {
        return hash;
    }

    /**
     * Loads student data from text file
     * @param fileName name of file to load
     * @throws FileNotFoundException
     */
    public void loadStudentData(String fileName) throws FileNotFoundException {

        String warnings = "";

        File file = new File(fileName);
        Scanner in = new Scanner(file);

        while (in.hasNextLine()) {
            String line = in.nextLine().trim();

            if (!line.equals(",,,") && !line.equals("") && !line.isEmpty()
                && !line.equals(",,,,,")) {
                String[] input = line.split(",\\s*");

                String p = input[0];
                if (input[0].length() < 9) {
                    p = "0" + input[0];
                }
                String first = input[1];
                String middle = input[2];
                String last = input[3];

                Student s = new Student(p, first + " " + last);
                if (middle.length() > 0) {
                    s.setMiddleName(middle);
                }

                if (hash.get(p) != null) {
                    warnings += "Student " + p + " " + s.getName() +
                        " is not loaded since a student with the same pid exists.\n";
                }
                else {
                    hash.add(p, s);
                    s.setNameHandle(position, s.getName().length());
                }

                if (hash.isBucketFull()) {
                    warnings += "There is no free place in the bucket to load " +
                " student " + p + " " + s.getName() + "\n";
                }
                // need to increment position by number of bytes
            }
        }
        System.out.println(fileName + " successfully loaded.\n" + warnings);
    }

    /**
     * Insert student into hash table
     * @param pid student's PID
     * @param fullName student's full name
     */
    public void insert(String pid, String fullName) {

        Student student = new Student(pid, fullName);

        if (hash.get(pid) == null) {
            // set current student
            currStudent = student;
            hash.add(pid, student);
            student.setNameHandle(position, student.getName().length());
            System.out.println(student.getName() + " inserted.");
        }
        else {
            System.out.println(student.getName() +
                " insertion failed since the pid " + pid +
                " belongs to another student");
        }

        // need to increment position by number of bytes

        // record the length of full name and essay
        int length = fullName.length();

        // use length to allocate a buffer into which
        // the record can be read and then store it into
        // the memory file and receive corresponding handles

        // byte buffer??
    }

    /**
     * Update a student's info
     * If PID does not exist, it
     * will be inserted
     * @param pid student's PID
     * @param fullName to update name to
     */
    public void update(String pid, String fullName) {

        Student student;

        // insert into hash if not there
        if (hash.get(pid) == null) {
            student = new Student(pid, fullName);
            this.insert(pid, fullName);
        }
        // update if there
        else {
            student = hash.get(pid);
            student.setName(fullName);
            student.setNameHandle(position, student.getName().length());
            currStudent = student;
            System.out.println("Student " + pid +
                " updated to " + student.getName());
        }
        // need to update position
    }

    /**
     * Remove student from hash table
     * @param pid to remove
     */
    public void remove(String pid) {

        if (hash.get(pid) == null) {
            System.out.println(pid + " is not found in the database");
        }
        else {
            hash.remove(pid);
            System.out.println(pid + " with full name " +
        hash.get(pid).getName() + " is removed from the database.");
        }
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
        currStudent.setEssayHandle(position,  essay.length());
        // increment position
        System.out.println("essay saved for " + currStudent.getName());
    }

    /**
     * Remove the essay associated with the sPID
     * @param pid to clear essay of
     */
    public void clear(String pid) {

        if (hash.get(pid) == null) {
            System.out.println(pid + " is not found in the database.");
        }
        else {
            hash.get(pid).setEssay("");
            hash.get(pid).setEssayHandle(position, 0);
            System.out.println("record with pid " + pid +
                " with full name " + hash.get(pid).getName() + " is cleared.");
        }
        // increment position
    }

    /**
     * Prints out info associated with PID if there
     * is one stored in the database
     * @param pid of student to search for
     */
    public void search(String pid) {

        if (hash.get(pid) == null) {
            System.out.println("Search Failed: Couldn't find "
                + "any student with ID " + pid);
        }
        else {
            System.out.println(pid + " " + hash.get(pid).getName()
                + ":\n" + hash.get(pid).getEssay());
        }
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
