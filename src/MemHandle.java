/**
 *  Represents a memory handle object
 *
 *  @author ajd29
 *  @author colle57
 *  @version Nov 27, 2019
 */
public class MemHandle
{
    private int position;
    private int length;

    /**
     * Creates a new memory handle with a 4-byte integer
     * parameter for position and a 4-byte integer parameter
     * for length
     * @param pos in file for associated record
     * @param len of associated record
     */
    public MemHandle(int pos, int len) {
        position = pos;
        length = len;
    }
}
