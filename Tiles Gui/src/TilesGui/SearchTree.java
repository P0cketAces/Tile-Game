
package TilesGui;

import java.util.HashMap;
import java.util.PriorityQueue;

/*
Contains all storage for nodes. Hash map
and priority queue are stored here.
*/

public final class SearchTree {
    
    public HashMap map;
    public PriorityQueue<Node> queue;
    
    /*
    Initialize both map and queue.
    */
    public SearchTree()
    {
        map = new HashMap();
        initializeQueue();
    }
    
    /*
    Determines if node is already in the 
    hash map.
    */
    public boolean mapContains(Node node)
    {
        return map.containsKey(node.key);
    }
    
    /*
    Adds a node to the list of total
    visited nodes.
    */
    public void putTotalNodes(Node node)
    {
        map.put(node.key, node);
    }
    
    /*
    Method is never used but retrieves
    a node given a key.
    */
    public Node retrieveNode(String key)
    {
        return (Node)map.get(key);
    }
    
    /*
    Initializes the queue and sets its comparator
    function to the HeuristicComparator class.
    Starts with storage for 100 nodes.
    */
    public void initializeQueue()
    {
        HeuristicComparator comparator = new HeuristicComparator();
        queue = new PriorityQueue<>(100, comparator);
    }
}
