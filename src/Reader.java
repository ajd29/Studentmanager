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
    // StudentDatabase object
    private StudentDatabase stuManager;
    private Scanner         file;


    /**
     * Creates a new reader object
     *
     * @param hashSize
     *            size of hash table
     * @param memFile
     *            memory file name
     * @throws FileNotFoundException
     */
    public Reader(String hashSize, String memFile)
        throws FileNotFoundException
    {
        stuManager = new StudentDatabase(hashSize, memFile);
    }


    /**
     * Returns student database object
     *
     * @return StudentDatabase
     */
    public StudentDatabase getStudentManager()
    {
        return stuManager;
    }

    /**
     * Reads input file
     *
     * @param fileName
     *            of file to read in
     * @throws Exception
     */
    public void readCommands(String fileName)
        throws Exception
    {
        File commandFile = new File(fileName);
        file = new Scanner(commandFile);

        while (file.hasNextLine())
        {
            String line = file.nextLine().trim();

            String[] input = line.split("\\s+");

            if (!line.isEmpty())
            {
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
                    String essay = file.nextLine() + System.lineSeparator();

                    String essayLine = file.nextLine();

                    // while essay off has not been reached
                    while (!essayLine.startsWith("essay off"))
                    {
                        essay += (essayLine + System.lineSeparator());
                        essayLine = file.nextLine();
                    }

                    // remove last character
                    essay.replace(essay.substring(essay.length() - 1), "");

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
}
