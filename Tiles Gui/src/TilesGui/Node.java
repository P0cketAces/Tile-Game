
package TilesGui;

/*
This class stores the boards and keys for
easy storage in SearchTree data structures.
*/
public class Node {
    
    public String key;
    public Board board;
    public Node parent;
    public Node winningChild;
    private final Node[] leaves = new Node[Constants.MAX_MOVES];
    
    /*
    Creates default randomized board node
    and sets leaves to null.
    */
    public Node()
    {
        parent = null;
        winningChild = null;
        board = new Board();
        key = board.getHashKey();
        
        for(int i = 0; i < Constants.MAX_MOVES; i++)
        {
            leaves[i] = null;
        }
    }
    
    /*
    Creates node with board given
    as boardString input. Sets leaves
    to null.
    */
    public Node(String boardString)
    {
        parent = null;
        winningChild = null;
        boardString = boardString.replace('0', ' ');
        key = boardString;
        board = new Board(boardString);
        
        for(int i = 0; i < Constants.MAX_MOVES; i++)
        {
            leaves[i] = null;
        }
    }
    
    /*
    Iterates through all possible moves and
    sets up all leaves (child nodes) as nodes
    in an array.
    */
    public void setLeaves()
    {
        String boardString;
        for(int i = 0; i < Constants.MAX_MOVES; i++)
        {
            Constants.direction dir;
            dir = Constants.direction.values()[i];
            
            if((boardString = board.getNextMoveBoard(dir)) != null)
            {
                leaves[i] = new Node(boardString);
                leaves[i].parent = this;
            }
        }
    }
    
    /*
    Returns the leaf given an index. Not used.
    */
    public Node getLeaf(int index)
    {
        return leaves[index];
    }
    
    /*
    Determines whether the piece moved was valid or
    not which helps in error checking. Does so by
    comparing the board piece selected with any
    change between the parent and child node. (If
    there is no change between the 2 then it was
    and invalid move).
    */
    public boolean validPieceMoved(char number, Node child)
    {
        char[] keyArray;
        char[] childKeyArray;
        keyArray = this.key.toCharArray();
        childKeyArray = child.key.toCharArray();
        
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            if(keyArray[i] != childKeyArray[i])
            {
                if(keyArray[i] == number || childKeyArray[i] == number)
                {
                    return true;
                }
            }
            
        }
        
        return false;
    }
}
