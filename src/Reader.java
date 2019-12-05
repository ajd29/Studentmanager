import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a Reader object
 *
 * @author ajd29
 * @author collee57
 * @version Nov 27, 2019
 */
public class Reader
{
    // StudentstuManager object
    private StudentDatabase stuManager;

    // MemoryManager object
    private MemoryManager memManager;


    /**
     * Creates a new reader object
     * @throws FileNotFoundException
     */
    public Reader(int hashSize) throws FileNotFoundException
    {
        stuManager = new StudentDatabase(hashSize);
        memManager = new MemoryManager();
    }

    /**
     * Reads input file
     *
     * @param fileName
     *            of file to read in
     * @throws FileNotFoundException
     */
    public void readCommands(String fileName) throws FileNotFoundException
    {
        File newFile = new File(fileName);
        Scanner file = new Scanner(newFile);

        while (file.hasNextLine())
        {
            String line = file.nextLine().trim();
            String[] input = line.split("\\s+");

            if (input[0].equals("loadStudentData")) {
                stuManager.loadStudentData(input[1]);
            }
            else if (input[0].equals("insert")) {
                stuManager.insert(input[1], input[2]);
            }
            else if (input[0].equals("update")) {
                stuManager.update(input[1], input[2]);
            }
            else if (input[0].equals("essay")) {

                String essayLine = file.nextLine().trim();

                // string variable to hold the essay
                String essay = "";

                // while essay off has not been reached
                while (!essayLine.startsWith("essay")) {
                    essay += essayLine;
                    essayLine = file.nextLine().trim();
                }

                // save essay in byte array
                byte[] essayBytes = essay.getBytes();

                int essayLength = essayBytes.length;

                stuManager.essay(essay);
            }
            else if (input[0].equals("remove")) {
                stuManager.remove(input[1]);
            }
            else if (input[0].equals("search")) {
                stuManager.search(input[1]);
            }
            else if (input[0].equals("clear")) {
                stuManager.remove(input[1]);
            }
            else if (input[0].equals("print")) {
                stuManager.print();
            }
        }
    }

}
