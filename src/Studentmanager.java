import java.io.FileNotFoundException;

/**
 *  Contains the main method for the program
 *
 *  @author ajd29
 *  @author collee57
 *  @version Nov 27, 2019
 */
public class Studentmanager
{
    private Reader read;


    /**
     * The main method drives the program
     * @param commandFile file with commands
     * @param hashSize size of hash table
     * @param memoryFile
     * @throws FileNotFoundException
     */
    public void main(String commandFile, int hashSize,
        String memoryFile) throws FileNotFoundException {

        read = new Reader();
        read.readCommands(commandFile);
    }

}
