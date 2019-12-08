import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * Creates a new reader object
     * @param hashSize size of hash table
     *
     * @throws FileNotFoundException
     */
    public Reader(int hashSize)
        throws FileNotFoundException
    {
        stuManager = new StudentDatabase(hashSize);

    }


    /**
     * Reads input file
     *
     * @param fileName
     *            of file to read in
     * @throws IOException
     */
    public void readCommands(String fileName)
        throws IOException
    {
        File newFile = new File(fileName);
        Scanner file = new Scanner(newFile);

        while (file.hasNextLine())
        {
            String line = file.nextLine().trim();
            String[] input = line.split("\\s+");

            if (input[0].equals("loadstudentdata"))
            {
                stuManager.loadStudentData(input[1]);
            }
            else if (input[0].equals("insert"))
            {
                stuManager.insert(input[1], input[2], input[3]);
            }
            else if (input[0].equals("update"))
            {
                stuManager.update(input[1], input[2], input[3]);
            }
            else if (input[0].equals("essay"))
            {
                String essayLine = file.nextLine().trim();

                String essay = "";

                // while essay off has not been reached
                while (!essayLine.startsWith("essay")) {
                    essay += essayLine;
                    essayLine = file.nextLine().trim();
                }
                stuManager.essay(essay);
            }
            else if (input[0].equals("remove"))
            {
                stuManager.remove(input[1]);
            }
            else if (input[0].equals("search"))
            {
                stuManager.search(input[1]);
            }
            else if (input[0].equals("clear"))
            {
                stuManager.clear(input[1]);
            }
            else if (input[0].equals("print"))
            {
                stuManager.print();
            }
        }
    }
}
