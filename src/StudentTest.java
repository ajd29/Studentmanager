import java.io.IOException;

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
     * @param memoryFile to write what's stored in memory to
     * @throws IOException
     */
    public void main(String commandFile, int hashSize,
        String memoryFile) throws IOException {

        System.out.println("Created Hash Set with " + hashSize + " slots.");
        read = new Reader(hashSize);
        read.readCommands(commandFile);
    }

}
