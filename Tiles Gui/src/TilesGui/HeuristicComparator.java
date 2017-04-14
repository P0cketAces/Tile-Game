
package TilesGui;

import java.util.Comparator;

/*
This class is used as the comparator to 
determine which node is considered at the top
of the priority queue. It compares using heuristic value
and is utilized in the SearchTree class.
*/

public class HeuristicComparator implements Comparator<Node>
{
    @Override
    public int compare(Node x, Node y)
    {
        if (x.board.getHeuristicValue() < y.board.getHeuristicValue())
        {
            return -1;
        }
        
        if (x.board.getHeuristicValue() > y.board.getHeuristicValue())
        {
            return 1;
        }
        
        return 0;
    }
}
