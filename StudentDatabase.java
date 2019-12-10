import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a student database object Contains commands
 *
 * @author ajd29 Allison DeSantis
 * @author collee57 Colleen Schmidt
 * @version Nov 27, 2019
 */
public class StudentDatabase {
    // HashTable object
    private HashTable<String, Student> hash;

    // true if successful insert or update called
    private boolean essayValid;

    // student of the previous command
    private Student currStudent;

    // Memory manager object
    private MemoryManager memManager;


    /**
     * Creates a new StudentDatabase object
     *
     * @param hashSize
     *            size of hash table
     * @param fileName
     *            name of memory file
     * @throws FileNotFoundException
     */
    public StudentDatabase(String hashSize, String fileName)
        throws FileNotFoundException {
        int size = Integer.parseInt(hashSize);
        hash = new HashTable<String, Student>(size);
        memManager = new MemoryManager(fileName);
    }


    /**
     * Return hash table
     *
     * @return HashTable<String, Student> hash table
     */
    public HashTable<String, Student> getHash() {
        return hash;
    }


    /**
     * Returns memory manager
     *
     * @return MemoryManager
     */
    public MemoryManager getMem() {
        return memManager;
    }


    /**
     * Loads student data from text file
     *
     * @param fileName
     *            name of file to load
     * @throws IOException
     */
    public void loadStudentData(String fileName) throws IOException {
        essayValid = false;

        String warnings = "";

        File file = new File(fileName);
        @SuppressWarnings("resource")
        Scanner in = new Scanner(file);

        while (in.hasNextLine()) {

            String line = in.nextLine().trim();
            String[] input = line.split(",\\s*");

            if (input.length == 4) {

                String pid = input[0];

                while (pid.length() < 9) {
                    pid = "0" + pid;
                }
                String first = input[1];
                String last = input[3];

                Student s = new Student(pid, first, last);

                if (hash.get(pid) == null) {
                    // tries to add
                    hash.add(pid, s);

                    if (hash.isBucketFull()) {
                        warnings =
                            "\nWarning: There is no free place in the bucket to"
                                + " load student " + pid + " " + s.getName();
                    }
                    else {
                        memManager.storeRecord(s);
                        currStudent = s;
                    }
                }
                else {
                    warnings = "\nWarning: Student " + pid + " " + s.getName()
                        + " is not loaded since a student "
                        + "with the same pid exists.";
                }
            }
        }
        System.out.println(fileName + " successfully loaded." + warnings);
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
        throws IOException {
        Student student = new Student(pid, fName, lName);

        // if student isn't already in hash table
        if (hash.get(pid) == null) {
            currStudent = student;
            hash.add(pid, student);

            if (hash.isBucketFull()) {
                System.out.println(student.getName()
                    + " insertion failed. Attempt to insert "
                    + "in a full bucket.");
                return;
            }

            memManager.storeRecord(student);
            essayValid = true;
            System.out.println(student.getName() + " inserted.");
        }
        else {
            essayValid = false;
            System.out.println(student.getName()
                + " insertion failed since the pid " + pid
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
        throws IOException {
        essayValid = false;
        Student student = new Student(pid, fName, lName);

        // insert into hash if not there
        if (hash.get(pid) == null) {
            this.insert(pid, fName, lName);
        }
        else {
            // if name is actually different
            if (!student.getName().equals(hash.get(pid).getName())) {
                hash.get(pid).setName(fName, lName);
                memManager.updateName(hash.get(pid));
            }
            currStudent = hash.get(pid);
            System.out.println("Student " + pid + " updated to " + student
                .getName());
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
    public void remove(String pid) throws IOException {
        essayValid = false;
        if (hash.get(pid) == null) {
            System.out.println(pid + " is not found in the database.");
        }
        else {
            memManager.removeRecord(hash.get(pid));
            System.out.println(pid + " with full name " + hash.get(pid)
                .getName() + " is removed from the database.");
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
    public void essay(String essay) throws IOException {
        if (essayValid) {
            currStudent.setEssay(essay);
            memManager.updateEssay(currStudent);
            System.out.println("essay saved for " + currStudent.getName());
        }
        else {
            System.out.println("essay commands can only follow "
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
    public void clear(String pid) throws IOException {
        essayValid = false;
        if (hash.get(pid) == null) {
            System.out.println(pid + " is not found in the database.");
        }
        else {
            hash.get(pid).setEssay("");
            memManager.clearEssay(hash.get(pid));
            System.out.println("record with pid " + pid + " with full name "
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
    public void search(String pid) {
        essayValid = false;

        if (hash.get(pid) == null) {
            System.out.println("Search Failed. Couldn't find "
                + "any student with ID " + pid);
        }
        else {
            String essay = hash.get(pid).getEssay();

            if (essay.length() > 0) {
                System.out.print(pid + " " + hash.get(pid).getName() + ":\n"
                    + essay);
            }
            else {
                System.out.print(pid + " " + hash.get(pid).getName() + ":");
            }
        }
    }


    /**
     * Print out a list of all records in the database
     */
    public void print() {
        essayValid = false;

        System.out.println("Students in the database:" + hash.getArrayString());

        memManager.printFreeList();
    }
}
