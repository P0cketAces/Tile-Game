
package TilesGui;

import static java.lang.Math.abs;
import java.util.Random;

/*
This class contains the board information that's
used to compute heuristic value and display the board.
Read above each function for documentation.
*/

public class Board {
    
    private char[] board;
    
    /*
    These constructors set up the board with new values.
    One is used to automatically populate a random one
    and the other takes a string parameter.
    
    The randomized constructor works in a way which scrambles
    all indexes.
    */
    
    public Board()
    {
        int[] boardArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random randomGenerator = new Random();
        randomGenerator.setSeed( System.currentTimeMillis());
        
        for (int i = Constants.BOARD_SIZE - 1; i > 0; i--)
        {
          int index = randomGenerator.nextInt(i + 1);
          int numSwap = boardArray[index];
          boardArray[index] = boardArray[i];
          boardArray[i] = numSwap;
        }
        
        intToCharArray(boardArray);
    }
    
    /*
    See other constructor
    */
    
    public Board(String boardString)
    {
        board = boardString.toCharArray();
    }
    
    public char[] getBoard()
    {
        return board;
    }
    
    /*
    Used in the randomized constructor to turn
    the random integer values into characters
    to be used in the board.
    
    Adds the ascii value to convert to char.
    */
    public final void intToCharArray(int[] boardArray)
    {
        char[] charArray = new char[Constants.BOARD_SIZE];
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            charArray[i] = (char)(boardArray[i] + '0');
            if(charArray[i] == '9')
            {
                charArray[i] = ' ';
            }
        }
        
        board = charArray;
    }
    
    /*
    This method is used as a helper in the heuristic value
    method because it converts the characters to integers
    to be used in math.
    
    Utilizes the char ascii conversion values.
    */
    public int[] charToIntArray()
    {
        int[] intArray = new int[Constants.BOARD_SIZE];
        
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            if(board[i] == ' ')
            {
                intArray[i] = 9;
            }
            
            else
            {
                String charString = "" + board[i];
                intArray[i] = Integer.parseInt(charString);
            }
        }
        
        return intArray;
    }
    
    /*
    This method returns a string representation
    of the board to be used as the hash identifier.
    */
    public String getHashKey()
    {
        String key = new String(board);
        return key;
    }
    
    /*
    This function does the math to determine 
    heuristic value. Utilizes the rows and columns
    constants in Constants.java. This gives coordinates
    for each number, which allows for total distance
    to be calculated from the desired spot.
    */
    public int getHeuristicValue()
    {
        int heuristic = 0;
        int rowIndex, columnIndex;
        int rowValue, columnValue;
        int[] intArray;
        intArray = charToIntArray();
        
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            int positionValue = intArray[i] - 1;
            rowValue = Constants.ROWS[positionValue];
            columnValue = Constants.COLS[positionValue];
            
            rowIndex = Constants.ROWS[i];
            columnIndex = Constants.COLS[i];
            
            heuristic += abs(columnValue - columnIndex);
            heuristic += abs(rowValue - rowIndex);
        }
        
        return heuristic;
    }
    
    /*
    Uses the enumeration direction to determine
    which moved piece has a corresponding board.
    Didn't need to use this function in the end, but
    if used, can make things simpler. This shows all
    possible moves from current position if fed a
    direction.
    */
    public String getNextMoveBoard(Constants.direction direction)
    {
        char[] newBoard = new char[Constants.BOARD_SIZE];
        System.arraycopy(board, 0, newBoard, 0, Constants.BOARD_SIZE);
        
        int spaceIndex = findSpaceIndex();
        int rowIndex = Constants.ROWS[spaceIndex];
        int colIndex = Constants.COLS[spaceIndex];
        
        switch(direction)
        {
            case UP: 
                if(rowIndex == 0)
                {
                    return null;
                }
                newBoard[spaceIndex] = newBoard[spaceIndex - 3];
                newBoard[spaceIndex - 3] = ' ';
                break;
            case RIGHT:
                if(colIndex == 2)
                {
                    return null;
                }
                newBoard[spaceIndex] = newBoard[spaceIndex + 1];
                newBoard[spaceIndex + 1] = ' ';
                break;
            case DOWN:
                if(rowIndex == 2)
                {
                    return null;
                }
                newBoard[spaceIndex] = newBoard[spaceIndex + 3];
                newBoard[spaceIndex + 3] = ' ';
                break;
            case LEFT:
                if(colIndex == 0)
                {
                    return null;
                }
                newBoard[spaceIndex] = newBoard[spaceIndex - 1];
                newBoard[spaceIndex - 1] = ' ';
                break;
        }
        
        String boardString = new String(newBoard);
        return boardString;
    }
    
    /*
    Returns a value corresponding to the index
    of where the ' ' is located.
    */
    public int findSpaceIndex()
    {
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            if(board[i] == ' ')
            {
                return i;
            }
        }
        
        return 0;
    }
    
    /*
    Displays the board.
    */
    @Override
    public String toString()
    {
        String boardView = "";
        int newLineFlag = 3;
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
        {
            if(newLineFlag == 3)
            {
                newLineFlag = 0;
                boardView += "\n";
                boardView += "   ";
                boardView += board[i];
            }
            
            else
            {
                boardView += " " + board[i];
            }
            
            newLineFlag++;
        }
        
        return boardView;
    }
}
