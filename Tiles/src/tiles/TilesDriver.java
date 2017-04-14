
package tiles;

/*
Authors: idea and glow
Class: CS342, Fall 2016
System: Windows10 with NetBeans 8.1
Program 3: 8 Tiles.

Description: Either randomly generate
or create your own 9 x 9 board containing
a single space that allows you to move
pieces until you reach a natural ordering of
the numbers 1, 2, 3, 4, 5, 6, 7, 8, ' '
where ' ' is the 9th character spot. Pressing
's' will automatically solve the puzzle.
*/

import java.util.Scanner;

public class TilesDriver {
    
    public static int count = 1;
    public static Node lastManualChild = null;
    
    /*
    Main driver for the program. Prompts input
    to determine what style of game the user wants to play.
    Furthermore breaks each response into a switch statement
    that contains the logic to move each board piece by piece
    and give control to the auto solver if prompted.
    */
    public static void main(String[] args){

        System.out.println("Authors: idea and glow");
        System.out.println("Class: CS342, Fall 2016");
        System.out.println("Program: #3, 8 Tiles\n");
        System.out.println("Welcome to the 8-tiles puzzle.");
        System.out.println("Place the tiles in ascending numerical order. For each");
        System.out.println("move enter the piece to be moved into the blank square,");
        System.out.println("or 0 to exit the program.\n");
        
        int gameMode;
        
        do
        {
            System.out.println("Choose a game option");
            System.out.println("0. Exit Game");
            System.out.println("1. Start Playing");
            System.out.println("2. Set the starting configuration");
            System.out.println("Type 's' during a game to automatically solve");
            System.out.print("Enter your choice --> ");

            Scanner userInput;
            userInput = new Scanner(System.in);
            gameMode = userInput.nextInt();
            System.out.println();
        }
        while(gameMode != 0 && gameMode != 1 && gameMode != 2);
        
        char nextPiece;
        
        switch (gameMode) 
        {
            /*
            Exits program
            */
            case 0:
                System.out.println("Goodbye!");
                break;
            /*
            This is the case that you randomly generate a board.
            Until 's' is prompted for, continually factors in user
            input to create the next boards until a solution is found.
            */
            case 1:
                System.out.println();
                System.out.println("Initial board is:");
                Node node = new Node();
                do
                {
                    System.out.println(count + ".");
                    System.out.println(node.board.toString());
                    
                    int heuristic = node.board.getHeuristicValue();
                    if(heuristic == 0)
                    {
                        System.out.println("Congratulations you won!");
                        System.exit(0);
                    }
                    System.out.println("Heuristic Value is: " + heuristic);
                    
                    System.out.print("Piece to move: ");
                    Scanner userInput;
                    userInput = new Scanner(System.in);
                    nextPiece = userInput.next().charAt(0);
                    
                    if(nextPiece == '0')
                    {
                        System.exit(0);
                    }
                    
                    if(nextPiece == 's')
                    {
                        lastManualChild = node;
                        System.out.println("Solving puzzle automatically...............");
                        autoSolve(node);
                    }
                    
                    Node nodeCopy = node;
                    
                    node.setLeaves();
                    for(int i = 0; i < Constants.MAX_MOVES; i++)
                    {
                        Node child;
                        if((child = node.getLeaf(i)) != null)
                        {
                            if(node.validPieceMoved(nextPiece, child))
                            {
                                node = child;
                            }
                        }
                    }
                    
                    if(nodeCopy == node)
                    {
                        System.out.println("Invalid move. Please retry.");
                    }
                    else
                    {
                        count++;
                    }
                }
                while(true);
            /*
            This is the case that you input a string into the board.
            Until 's' is prompted for, continually factors in user
            input to create the next boards until a solution is found.
            */
            default:
                System.out.println();
                System.out.println("Some boards such as 728045163 are impossible.");
                System.out.println("Others such as 245386107 are possible");
                System.out.print("Enter a string of 9 digits (including 0) for the board --> ");
                Scanner userInput;
                userInput = new Scanner(System.in);
                String boardString = userInput.nextLine();
                
                System.out.println();
                System.out.println("Initial board is:");
                Node myNode = new Node(boardString);

                do
                {
                    System.out.println(count + ".");
                    System.out.println(myNode.board.toString());

                    int heuristic = myNode.board.getHeuristicValue();
                    if(heuristic == 0)
                    {
                        System.out.println("Congratulations you won!");
                        System.exit(0);
                    }
                    System.out.println("Heuristic Value is: " + heuristic);

                    System.out.print("Piece to move: ");
                    userInput = new Scanner(System.in);
                    nextPiece = userInput.next().charAt(0);

                    if(nextPiece == '0')
                    {
                        return;
                    }

                    if(nextPiece == 's')
                    {
                        lastManualChild = myNode;
                        System.out.println("Solving puzzle automatically...............");
                        autoSolve(myNode);
                    }
                    Node nodeCopy = myNode;
                    myNode.setLeaves();
                    for(int i = 0; i < Constants.MAX_MOVES; i++)
                    {
                        Node child;
                        if((child = myNode.getLeaf(i)) != null)
                        {
                            if(myNode.validPieceMoved(nextPiece, child))
                            {
                                myNode = child;
                            }
                        }
                    }
                    
                    if(nodeCopy == myNode)
                    {
                        System.out.println("Invalid move. Please retry.");
                    }
                    else
                    {
                        count++;
                    }
                }
                while(true);
        }
    }

    /*
    Meat of the auto solving mechanism. This
    contains the logic of adding to the queue
    and map while receiving the corresponding
    queue min Node to determine an optimal path.
    Sends control to printNodePath when one is found.
    */
    public static void autoSolve(Node node)
    {
        Node bestNode = node;
        boolean firstIterationFlag = true;
        SearchTree tree = new SearchTree();
        tree.putTotalNodes(node);
        
        while(firstIterationFlag || !tree.queue.isEmpty())
        {
            firstIterationFlag = false;
            int heuristic = node.board.getHeuristicValue();
            if(heuristic == 0)
            {
                printNodePath(node);
            }
            
            node.setLeaves();
            for(int i = 0; i < Constants.MAX_MOVES; i++)
            {
                Node child;
                if((child = node.getLeaf(i)) != null)
                {
                    if(!tree.mapContains(child))
                    {
                        tree.putTotalNodes(child);
                        tree.queue.add(child);
                    }
                }
            }
            
            node = tree.queue.poll();
            if(node.board.getHeuristicValue() < bestNode.board.getHeuristicValue())
            {
                bestNode = node;
            }
        }
        
        System.out.println("All 181442 moves have been tried.");
        System.out.println("That puzzle is impossible to solve.  Best board found was:");
        System.out.println(bestNode.board.toString());
        System.out.println("Heuristic Value: " + bestNode.board.getHeuristicValue());
        System.out.println("Exiting program.");
        System.exit(0);
    }
    
    /*
    Iterates first through child to parent to make sure that
    the board is printed in order. Then iterates back down
    to the child nodes to print out all boards in order.
    */
    public static void printNodePath(Node node)
    {   
        count++;
        
        while(node.parent != null)
        {
            node.parent.winningChild = node;
            node = node.parent;
        }
        
        while(node != lastManualChild)
        {
            node = node.winningChild;
        }
        
        node = node.winningChild;
        
        while(node.winningChild != null)
        {
            System.out.println(count + ".");
            System.out.println(node.board.toString());
            System.out.println("Heuristic Value is: " + node.board.getHeuristicValue());
            count++;
            node = node.winningChild;
        }
        
        System.out.println(count + ".");
        System.out.println(node.board.toString());
        System.out.println("Heuristic Value is: " + node.board.getHeuristicValue());
        
        System.out.println("Done");
        System.exit(0);
    }
}
