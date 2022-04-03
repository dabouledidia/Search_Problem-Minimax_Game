import java.util.ArrayList;

public class TNode {

    private final Table state;
    private int cost;
    private final TNode parent;

    public TNode(Table state, TNode parent, int cost){
        this.state = state;
        this.parent = parent;
        this.cost = cost;
    }

    public ArrayList<TNode> makeChildren(boolean isMax) {
        int moveLimit = state.getMoveLimit();
        ArrayList<String> moves;
        ArrayList<TNode> childArray = new ArrayList<>();
        Table table = this.getState();
        int newCost = 0;

        // Get the available moves
        if (isMax) {
            moves = new ArrayList<>(table.availableMoves(table.getMax()));
        } else {
            moves = new ArrayList<>(table.availableMoves(table.getMin()));
        }

        if (moves.isEmpty()){
            TNode newNode = new TNode(state, this, 0);
            childArray.add(newNode);
            return childArray;
        }


        for (int i = 0; i < moves.size(); i++) {

            // Get a copy from the original board
            String[][] copyTable = new String[table.getRowSize()][table.getColSize()];
            for (int j = 0; j < table.getRowSize(); j++) {
                System.arraycopy(table.getTable()[j], 0, copyTable[j], 0, table.getColSize());
            }

            // For each move make a new state of the board
            Table childBoard = new Table(copyTable, moveLimit);
            Player max = new Player(childBoard, table.getMax().getPosition(), "A");
            Player min = new Player(childBoard, table.getMin().getPosition(), "B");
            // Place the 2 players
            childBoard.setMin(min);
            childBoard.setMax(max);
            //As cost we refer to the utility score of each move.
            if (isMax) {
                childBoard.movePlayerTo(childBoard.getMax(), moves.get(i));
                // cost is the available moves that max has on his new position
                newCost = childBoard.availableMoves(childBoard.getMax()).size();
            } else {
                childBoard.movePlayerTo(childBoard.getMin(), moves.get(i));
                // cost is minus the available moves that min has on his new position
                newCost = -childBoard.availableMoves(childBoard.getMin()).size();
            }
            childArray.add(new TNode(childBoard, this, newCost));
        }
        return childArray;
    }

    // Calculation of the cost of each move. (We sum up/subtract every cost of each move -from the start till the end-
    // to calculate the utility score.
    public int calculateCost(){
        int calCost = 0;
        TNode tempNode = this;

        while(tempNode.parent != null){
            calCost = calCost + tempNode.getCost();
            tempNode = tempNode.parent;
        }
        return calCost;
    }

    public Table getState(){
        return state;
    }

    public int getCost() {
        return cost;
    }

    public TNode getParent(){
        return parent;
    }

    public void setCost(int cost){
        this.cost = cost;
    }
}
