
package TilesGui;

/*
Author: idea
Class: CS342, Fall 2016
System: Windows10 with NetBeans 8.1
Program 4: 8 Tiles GUI.

Description: Either randomly generate
or create your own 9 x 9 board containing
a single space that allows you to move
pieces until you reach a natural ordering of
the numbers 1, 2, 3, 4, 5, 6, 7, 8, ' '
where ' ' is the 9th character spot. Pressing
autosolve will automatically solve the puzzle.
*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TilesDriver {
    /*
    Main driver for the program. Begins the
    layout.
    */
    public static void main(String[] args){
        
        StartingConfig startFrame = new StartingConfig();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
}


/*
This is the method that does all the heavy lifting.
The entire driver program is encapsulated in this
frame. All variables declared here and button
handlers handle the rest.
*/
class StartingConfig extends JFrame implements ActionListener{
    private JButton btnRandomize;
    private JButton btnConfigure;
    private final JPanel mainPanel;
    private JPanel btnPanel;
    private JPanel winPanel;
    private JPanel optionsPanel;
    private final JPanel heuristicPanel;
    private JPanel textPanel;
    private final Container container;
    private final GridLayout gridLayout;
    private final FlowLayout flowLayout;
    private Node node;
    private JButton btnAutosolve;
    private JButton btnExit;
    private final JLabel heuristicLabel;
    private String boardString;
    public static int count = 1;
    public static Node lastManualChild = null;
    private final JButton[] buttons = new JButton[9];;
    private int buttonCount = 0;
    boolean isSolved = false;
    SetupButtonHandler tempButtonHandler[] = new SetupButtonHandler[9];
    
    
    /*
    Constructer initializes the beginning layout with 
    instructions and 2 buttons: Randomize board or 
    configure own layout.
    */
    public StartingConfig(){
        super("Tile Puzzle");
        gridLayout = new GridLayout(3,3);
        flowLayout = new FlowLayout();
        
        heuristicPanel = new JPanel();
        heuristicPanel.setLayout(new GridLayout(1,1));
        heuristicLabel = new JLabel();
        heuristicLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        heuristicPanel.add(heuristicLabel);
        
        container = getContentPane();
        mainPanel = new JPanel();
        mainPanel.setLayout(flowLayout);
        container.add(mainPanel);
        configStartingLayout();
        
        this.setSize(350, 200);
        this.setVisible(true);
    }

    /*
    This method configures the starting layout
    by initializing the board.
    */
    public void configStartingLayout(){
        
        btnConfigure = new JButton("Configure");
        btnRandomize = new JButton("Randomize");
        
        btnConfigure.addActionListener(this);
        btnConfigure.setActionCommand("config");
        btnRandomize.addActionListener(this);
        btnRandomize.setActionCommand("rand");
        
        mainPanel.add(new JLabel("Author: idea"));
        mainPanel.add(new JLabel("Class: CS342, Fall 2016"));
        mainPanel.add(new JLabel("Program: #3, 8 Tiles"));
        mainPanel.add(new JLabel("Welcome to the 8-tiles puzzle."));
        mainPanel.add(new JLabel("Place the tiles in ascending numerical order. For each"));
        mainPanel.add(new JLabel("move click the piece to be moved into the blank square."));
                
        mainPanel.add(btnConfigure);
        mainPanel.add(btnRandomize);
    }
    
    /*
    Here lies both event handlers for the respective buttons.
    Each call a different respective method.
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("config"))
        {
            container.removeAll();
            container.repaint();
            initializeNewLayout(false);
        }
        
        else if(e.getActionCommand().equals("rand"))
        {
            container.removeAll();
            container.repaint();
            initializeRandomizeMode();
        }
    }
    
    /*
    To randomly initialize the board, it creates
    a node with no constructor and then sets the
    new layouts to represent a grid of buttons and
    also buttons for autosolve and exit... as well
    as a heuristic label at top.
    */
    public void initializeRandomizeMode(){
        
        node = new Node();
        int heuristic = node.board.getHeuristicValue();
        heuristicLabel.setText("Heuristic value is: " + heuristic + " Count is: " + count);
        
        this.setSize(500, 500);
        btnPanel = new JPanel();
        btnPanel.setLayout(gridLayout);
        container.add(btnPanel, BorderLayout.CENTER);
        
        container.add(heuristicPanel, BorderLayout.NORTH);
        
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1,2));
        btnAutosolve = new JButton("Auto Solve");
        btnExit = new JButton("EXIT");
        
        AutoButtonHandler autoHandler = new AutoButtonHandler();
        btnAutosolve.addActionListener(autoHandler);
        
        ExitButtonHandler exitHandler = new ExitButtonHandler();
        btnExit.addActionListener(exitHandler);
        
        optionsPanel.add(btnAutosolve);
        optionsPanel.add(btnExit);
        container.add(optionsPanel, BorderLayout.SOUTH);
        validate();
        
        for(int i = 0; i < 9; i++)
        {
            char[] boardArray = node.board.getBoard();
            String boardLetter = "";
            boardLetter += boardArray[i];
            buttons[i] = new JButton(boardLetter);
            buttons[i].addActionListener(new FinalButtonHandler(true));
            btnPanel.add(buttons[i]);
        }
    }
    
    /*
    This is used in configuring a custom layout. The first
    time through, fBoard is false to signify that you are 
    still configuring. When all buttons are placed, it
    adds new button listeners to all buttons that apply
    the tile puzzle mechanics. This also calls the class
    FinalButtonHandler which is the main game.
    */
    public void initializeNewLayout(boolean fBoard){
        
        if(fBoard == false)
        {
            this.setSize(500, 500);
            btnPanel = new JPanel();
            btnPanel.setLayout(gridLayout);
            container.add(btnPanel, BorderLayout.CENTER);
            
            container.add(heuristicPanel, BorderLayout.NORTH);

            optionsPanel = new JPanel();
            optionsPanel.setLayout(new GridLayout(1,2));
            btnAutosolve = new JButton("Auto Solve");
            btnExit = new JButton("EXIT");
            
            AutoButtonHandler autoHandler = new AutoButtonHandler();
            btnAutosolve.addActionListener(autoHandler);
            btnAutosolve.setEnabled(false);

            ExitButtonHandler exitHandler = new ExitButtonHandler();
            btnExit.addActionListener(exitHandler);

            optionsPanel.add(btnAutosolve);
            optionsPanel.add(btnExit);
            container.add(optionsPanel, BorderLayout.SOUTH);
            validate();
        }
        
        for(int i = 0; i < 9; i++)
        {
            if(fBoard == false)
            {
                buttons[i] = new JButton("");
                tempButtonHandler[i] = new SetupButtonHandler();
                buttons[i].addActionListener(tempButtonHandler[i]);
                btnPanel.add(buttons[i]);
            }
            else
            {
                buttons[i].removeActionListener(tempButtonHandler[i]);
                buttons[i].setEnabled(true);
                buttons[i].addActionListener(new FinalButtonHandler(false));
                btnAutosolve.setEnabled(true);
            }
        
        }
        
    }
    
    /*
    This is a helper class for placing the pieces in
    configure mode.
    */
    class SetupButtonHandler implements ActionListener
    {
        private final String names[] = {" ", "1", "2", "3", "4", "5", "6", "7", "8"};

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton btn = (JButton) e.getSource();
            btn.setText(names[buttonCount]);
            btn.setFont(new Font("Arial", Font.PLAIN, 40));
            btn.setEnabled(false);
            System.out.println(buttonCount);
            buttonCount++;
            
            if(buttonCount == 9){
                initializeNewLayout(true);
            }
        }
    }
    
    /*
    This is the autosolver. It works in a way so
    that if there is no solution, it will print the
    best board at the bottom. It utilizes the search
    tree and queue to add accessed nodes to them.
    */
    class AutoButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            lastManualChild = node;
            autoSolveGui(node);
        }
        
        public void autoSolveGui(Node node)
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
                    printNodePathGui(node);
                    isSolved = true;
                    return;
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
            
            if(!isSolved)
            {
                JPanel textPanel = new JPanel();
                container.remove(btnPanel);
                container.add(textPanel, BorderLayout.CENTER);
                textPanel.setLayout(new GridLayout(3,3));

                char bestBoard[] = bestNode.board.getBoard();

                for(int i = 0; i < 9; i++)
                {
                    String bestBoardString = "";
                    bestBoardString += bestBoard[i];
                    JLabel finalPos = new JLabel(bestBoardString);
                    finalPos.setFont(new Font("Arial", Font.PLAIN, 40));
                    textPanel.add(finalPos);
                }

                int heur = bestNode.board.getHeuristicValue();
                container.add(heuristicPanel, BorderLayout.NORTH);
                heuristicLabel.setText("Best Solution's heuristic value is: " + heur);
                container.validate();

                btnAutosolve.setEnabled(false);
            }
            
        }
        
        /*
        If a path is found then this will print out the path.
        
        Currently this is the only part of the program that 
        doesn't work perfectly. The logic is perfectly there
        but it is missing a way to pause the while loop for each
        board.
        */
        public void printNodePathGui(Node node)
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
            
            textPanel = new JPanel();
            container.remove(btnPanel);
            container.add(textPanel, BorderLayout.CENTER);
            textPanel.setLayout(new GridLayout(3,3));
            
            //TimerHandler taskPerformer = new TimerHandler();
            
//            Timer timer = new Timer(300 ,taskPerformer);
//            timer.setRepeats(true);
//            timer.start();
                
            while(node.winningChild != null)
            {
                textPanel.removeAll();
                textPanel.revalidate();
                textPanel.repaint();
                
                char winningBoard[] = node.board.getBoard();
                for(int i = 0; i < 9; i++)
                {
                    String bestBoardString = "";
                    bestBoardString += winningBoard[i];
                    JLabel pos = new JLabel(bestBoardString);
                    pos.setFont(new Font("Arial", Font.PLAIN, 40));
                    textPanel.add(pos);
                }
                
                int heur = node.board.getHeuristicValue();
                container.add(heuristicPanel, BorderLayout.NORTH);
                heuristicLabel.setText("Solution found! Count is: " + count);
                container.validate();
                
                count++;
                node = node.winningChild;
                
            }
            
            textPanel.removeAll();
            textPanel.revalidate();
            textPanel.repaint();
            
            char winningBoard[] = node.board.getBoard();
            for(int i = 0; i < 9; i++)
            {
                String bestBoardString = "";
                bestBoardString += winningBoard[i];
                JLabel pos = new JLabel(bestBoardString);
                pos.setFont(new Font("Arial", Font.PLAIN, 40));
                textPanel.add(pos);
            }

            int heur = node.board.getHeuristicValue();
            container.add(heuristicPanel, BorderLayout.NORTH);
            heuristicLabel.setText("Solution found! Count is: " + count);
            container.validate();

            btnAutosolve.setEnabled(false);
        }
        
    }
    
//    class TimerHandler implements ActionListener
//    {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            
//            if(node.winningChild == null)
//            {
//                
//                
//            }
//            
//            textPanel.removeAll();
//            textPanel.revalidate();
//            textPanel.repaint();
//
//            char winningBoard[] = node.board.getBoard();
//            for(int i = 0; i < 9; i++)
//            {
//                String bestBoardString = "";
//                bestBoardString += winningBoard[i];
//                JLabel pos = new JLabel(bestBoardString);
//                pos.setFont(new Font("Arial", Font.PLAIN, 40));
//                textPanel.add(pos);
//            }
//
//            int heur = node.board.getHeuristicValue();
//            container.add(heuristicPanel, BorderLayout.NORTH);
//            heuristicLabel.setText("Solution found! Count is: " + count);
//            container.validate();
//
//            count++;
//            node = node.winningChild;
//        }
//        
//    }
    
    /*
    Exit button handler
    */
    class ExitButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
    
    /*
    This button handler is for the main part
    of the game. It deals with all events where
    clicking a tile moves it into another spot
    as well as validation.
    */
    class FinalButtonHandler implements ActionListener
    {
        private int heuristic;
        
        public FinalButtonHandler(boolean isRand)
        {
            if(!isRand)
            {
                boardString = getBoardString();
                node = new Node(boardString);
                heuristic = node.board.getHeuristicValue();
                heuristicLabel.setText("Heuristic value is: " + heuristic + " Count is: " + count);
            }
            
            else
            {
                heuristic = node.board.getHeuristicValue();
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            
            JButton btn = (JButton) e.getSource();
            String buttonNumber = btn.getText();
            
            if(!buttonNumber.equals(" "))
            {
                performAlgorithm(btn);
            }
        }
        
        /*
        In normal mode (not autosolved) this
        performs the logic that implements the main
        game and sends a winning screen if you win.
        */
        public void performAlgorithm(JButton btn)
        {
            if(heuristic == 0)
            {
                container.removeAll();
                container.repaint();
                winPanel = new JPanel();
                winPanel.setLayout(new FlowLayout());
                container.add(winPanel);
                JLabel youWin = new JLabel("WIN!");
                youWin.setFont(new Font("Arial", Font.PLAIN, 100));
                winPanel.add(youWin);
                container.validate();
            }
            
            Node nodeCopy = node;
            node.setLeaves();
            for(int i = 0; i < Constants.MAX_MOVES; i++)
            {
                Node child;
                if((child = node.getLeaf(i)) != null)
                {
                    String buttonNumber = btn.getText();
                    
                    char buttonNum = buttonNumber.charAt(0);

                    if(node.validPieceMoved(buttonNum, child))
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
             
            char[] board = node.board.getBoard();
            for(int i = 0; i < 9; i++)
            {
                String letter = "";
                letter += board[i];
                buttons[i].setText(letter);
            }
            
            heuristic = node.board.getHeuristicValue();
            heuristicLabel.setText("Heuristic value is: " + heuristic + " Count is: " + count);
        }
        
    }
    
    /*
    A helper function that gets the board string based
    on how the tiles look.
    */
    public String getBoardString()
    {
        String boardString = "";
        
        for(int i = 0; i < 9; i++)
        {
            boardString += buttons[i].getText();
        }
        
        return boardString;
    }
}
