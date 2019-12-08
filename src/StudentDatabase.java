import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a MemoryManager object Contains commands
 *
 * @author ajd29
 * @author collee57
 * @version Nov 27, 2019
 */
public class StudentDatabase
{
    // HashTable object
    private HashTable<String, Student> hash;

    // true if successful insert or update called
    private boolean                    essayValid;

    // student of the previous command
    private Student                    currStudent;

    // byte position of student record in memory file
    private int                        position;

    // Memory manager object
    private MemoryManager              memManager;


    /**
     * Creates a new StudentDatabase object
     *
     * @param hashSize
     *            size of hash table
     * @throws FileNotFoundException
     */
    public StudentDatabase(int hashSize)
        throws FileNotFoundException
    {
        hash = new HashTable<String, Student>(hashSize);
        memManager = new MemoryManager();
    }


    /**
     * Return hash table
     *
     * @return HashTable<String, Student> hash table
     */
    public HashTable<String, Student> getHash()
    {
        return hash;
    }

    /**
     * Returns memory manager
     *
     * @return MemoryManager
     */
    public MemoryManager getMem()
    {
        return memManager;
    }

    /**
     * Loads student data from text file
     *
     * @param fileName
     *            name of file to load
     * @throws IOException
     */
    public void loadStudentData(String fileName)
        throws IOException
    {

        essayValid = false;

        String warnings = "Warning: ";

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
                    // s.setMiddleName(middle);
                }

                if (hash.get(p) == null)
                {
                    // tries to add
                    hash.add(p, s);

                    if (hash.isBucketFull())
                    {
                        // don't store in memory if bucket is full
                        warnings +=
                            "There is no free place in the bucket to load "
                                + " student " + p + " " + s.getName();
                    }
                    else
                    {
                        memManager.storeRecord(s);
                        currStudent = s;
                    }
                }
                else
                {
                    warnings += "Student " + p + " " + s.getName()
                        + " is not loaded since a student with the same pid exists.";
                }
            }
        }
        System.out.println(fileName + " successfully loaded.\n" + warnings);
    }

    /**
     * Insert student into hash table
     *
     * @param pid
     *            student's PID
     * @param fName
     *            first name
     * @param lName
     *            last name
     * @throws IOException
     */
    public void insert(String pid, String fName, String lName)
        throws IOException
    {

        Student student = new Student(pid, fName, lName);

        // if student isn't already in hash table
        if (hash.get(pid) == null)
        {
            currStudent = student;
            hash.add(pid, student);

            memManager.storeRecord(student);

            essayValid = true;

            System.out.println(student.getName() + " inserted.");
        }
        else
        {
            essayValid = false;
            System.out.println(
                student.getName() + " insertion failed since the pid " + pid
                    + " belongs to another student.");
        }
    }

    /**
     * Update a student's info If PID does not exist, it will be inserted
     *
     * @param pid
     *            student's PID
     * @param fName
     *            first name
     * @param lName
     *            last name
     * @throws IOException
     */
    public void update(String pid, String fName, String lName)
        throws IOException
    {
        essayValid = false;
        Student student = new Student(pid, fName, lName);

        // insert into hash if not there
        if (hash.get(pid) == null)
        {
            this.insert(pid, fName, lName);
        }
        else
        {
            // if name is actually different
            if (!student.getName().equals(hash.get(pid).getName()))
            {
                hash.get(pid).setName(fName, lName);
                memManager.updateName(hash.get(pid));
            }
            currStudent = hash.get(pid);
            System.out
                .println("Student " + pid + " updated to " + student.getName());
        }
        essayValid = true;
    }

    /**
     * Remove student from hash table
     *
     * @param pid
     *            to remove
     * @throws IOException
     */
    public void remove(String pid)
        throws IOException
    {
        essayValid = false;
        if (hash.get(pid) == null)
        {
            System.out.println(pid + " is not found in the database.");
        }
        else
        {
            memManager.removeRecord(hash.get(pid));
            System.out.println(
                pid + " with full name " + hash.get(pid).getName()
                    + " is removed from the database.");
            hash.remove(pid);
        }
    }

    /**
     * Will immediately follow successful insert or update command; the
     * corresponding text will be associated with the PID of the previous
     * command and stored as a byte array following UTF-8 encoding
     *
     * @param essay
     *            to store for student
     * @throws IOException
     */
    public void essay(String essay)
        throws IOException
    {
        if (essayValid)
        {
            currStudent.setEssay(essay);
            memManager.updateEssay(currStudent);
            System.out.println("essay saved for " + currStudent.getName());
        }
        else
        {
            System.out.println(
                "essay commands can only follow "
                    + "successful insert or update commands");
        }
        essayValid = false;
    }

    /**
     * Remove the essay associated with the sPID
     *
     * @param pid
     *            to clear essay of
     * @throws IOException
     */
    public void clear(String pid)
        throws IOException
    {
        essayValid = false;
        if (hash.get(pid) == null)
        {
            System.out.println(pid + " is not found in the database.");
        }
        else
        {
            hash.get(pid).setEssay("");
            hash.get(pid).setEssayHandle(position, 0);
            memManager.clearEssay(hash.get(pid));
            System.out.println(
                "record with pid " + pid + " with full name "
                    + hash.get(pid).getName() + " is cleared.");
        }
    }

    /**
     * Prints out info associated with PID if there is one stored in the
     * database
     *
     * @param pid
     *            of student to search for
     */
    public void search(String pid)
    {
        essayValid = false;
        if (hash.get(pid) == null)
        {
            System.out.println(
                "Search Failed. Couldn't find " + "any student with ID " + pid);
        }
        else
        {
            String essay = hash.get(pid).getEssay();
            if (hash.get(pid).getEssay().length() > 0)
            {
                essay = "\n" + hash.get(pid).getEssay();
            }
            System.out.println(
                pid + " " + hash.get(pid).getName() + ":" + essay);

        }
    }

    /**
     * Print out a list of all records in the database
     */
    public void print()
    {
        essayValid = false;

        System.out.println("Students in the database:" +
        hash.getArrayString());

        System.out.println("SIZE OF FREELIST: " + memManager.getFreeList().size());
        memManager.printFreeList();
    }
}
