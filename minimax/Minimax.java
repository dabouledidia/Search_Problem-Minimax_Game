import java.util.ArrayList;

public class Minimax {

    private final int depthLimit;

    public Minimax(int depthLimit){
        this.depthLimit = depthLimit;
    }

    // This method develops/constructs the movements tree
    public ArrayList<TNode> makeTree(ArrayList<TNode> nodeArray, int depth, boolean isMax){
        ArrayList<TNode> newNodeArray = new ArrayList<>();

        /*
        -----------FOR DEBUGGING-----------

        System.out.println("DEPTH: " + depth + " MAX: " + !isMax);
        for (TNode tempNode : nodeArray){
            tempNode.getState().makeBoard();
            System.out.println("Node cost: " + tempNode.getCost());
        }
        */

        // If desired depth is reached calculate the cost for each final node and return tree
        if (depth == depthLimit){
            for (TNode node : nodeArray){
                node.setCost(node.calculateCost());
            }
            return nodeArray;
        }

        // For each node return its children until desired depth is reached
        TNode node;

        if (isMax){
            for (int i = 0; i < nodeArray.size(); i++){
                node = nodeArray.get(i);
                newNodeArray.addAll(node.makeChildren(true));
            }

            if (newNodeArray.isEmpty()){
                return makeTree(nodeArray, depthLimit, false);
            }else{
                return makeTree(newNodeArray, depth+1, false);
            }
        }else{
            for (int i = 0; i < nodeArray.size(); i++){
                node = nodeArray.get(i);
                newNodeArray.addAll(node.makeChildren(false));
            }
            if (newNodeArray.isEmpty()){
                return makeTree(nodeArray, depthLimit, true);
            }else{
                return makeTree(newNodeArray, depth+1, true);
            }
        }
    }

    //This method is responsible for Minimax algorithm execution.
    public String runMinimax(ArrayList<TNode> nodeArray, int depth, boolean isMax){

        ArrayList<TNode> newNodeArray = new ArrayList<>();

        /*
        -----------FOR DEBUGGING-----------
        System.out.println("+++++++++++++++++++++++++++++++++");

        for (TNode node: nodeArray) {
            node.getState().makeBoard();
            System.out.println("Cost: " + node.getCost());
        }
        */

       // System.out.println(nodeArray);

        TNode firstNode = nodeArray.get(0);

        // If desired depth or parent node is reached
        // find node with max value and end
        if (depth == depthLimit-1 || firstNode.getParent().getParent() == null){
            int maxValue = nodeArray.get(0).getCost();
            TNode tempNode = nodeArray.get(0);
            for (TNode tNode : nodeArray) {
                if (tNode.getCost() > maxValue) {
                    maxValue = tempNode.getCost();
                    tempNode = tNode;
                }
            }

            /*
            for (TNode node: nodeArray) {
                //.getState().makeBoard();---------
                //System.out.println("Cost: " + node.getCost());-----------
            }
            */

            // Return best move for max
            return tempNode.getState().getMax().getPosition();
        }

        firstNode.getParent().setCost(firstNode.calculateCost());
        newNodeArray.add(firstNode.getParent());

        TNode lastNode;

        if (isMax){
            // For max's turn find the node with maximum value
            for (TNode node: nodeArray) {
                lastNode =  newNodeArray.get(newNodeArray.size()-1);
                if (node.getParent().equals(lastNode)){
                    // If current node has bigger value than last node swap (same parent)
                    if (node.getCost() > lastNode.getCost()){
                        newNodeArray.remove(lastNode);
                        node.getParent().setCost(node.getCost());
                        newNodeArray.add(node.getParent());
                    }
                    // If it's a node fromm different parent add node
                }else{
                    node.getParent().setCost(node.getCost());
                    newNodeArray.add(node.getParent());
                }
            }
            return runMinimax(newNodeArray, depth+1, false);
        }else{
            // For min's turn find the node with minimum value
            for (TNode node: nodeArray) {
                lastNode =  newNodeArray.get(newNodeArray.size()-1);
                if (node.getParent().equals(lastNode)){
                    // If current node has smaller value than last node swap (same parent)
                    if (node.getCost() < lastNode.getCost()){
                        newNodeArray.remove(lastNode);
                        node.getParent().setCost(node.getCost());
                        newNodeArray.add(node.getParent());
                    }
                    // If it's a node fromm different parent add node
                }else{
                    node.getParent().setCost(node.getCost());
                    newNodeArray.add(node.getParent());
                }
            }
            return runMinimax(newNodeArray, depth+1, true);
        }

    }

}
