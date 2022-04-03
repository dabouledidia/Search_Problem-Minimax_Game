import java.util.ArrayList;
import java.util.Collections;

class Node {
    private int cost;
    private final String nodeName;
    private final Node parent;
    private final ElementList value;
    private ArrayList<Node> children;

    // Constructor 
    public Node(String nodeName, Node parent, ElementList value, int cost) {
        this.nodeName = nodeName;
        this.parent = parent;
        this.value = value;
        this.cost = cost;
    }

    // A function to generate children for each node
    public void makeChildren() {
        ArrayList<Node> nodeArray = new ArrayList<>();
        ElementList[] elementListArray = new ElementList[value.getList().size() - 1];
        String nodeNameFormat;

        //Create one child for each operator (2 to N)
        for (int i = 0; i < value.getList().size() - 1; i++) {
            nodeNameFormat = String.format("NodeT%d", i + 2);
            elementListArray[i] = new ElementList();
            elementListArray[i].setList(value.getList());
            elementListArray[i].operator(i + 2);

            // Every next node's cost is increased by 1
            nodeArray.add(new Node(nodeNameFormat, this, elementListArray[i], cost + 1));
            nodeNameFormat = "";

        }
        children = nodeArray;
    }

    public void findPath() {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        Node tempNode = this;

        // Follow each node's parent to find path
        while (tempNode.parent != null) {
            path.add(tempNode.getValue().getList());
            tempNode = tempNode.parent;
        }
        Collections.reverse(path);
        System.out.println("COST: " + cost);
        System.out.println("PATH: ");
        for (ArrayList<Integer> list: path ) {
            System.out.println(list);
        }
    }

    private boolean heuristicInv1(ArrayList<Integer> list){
        ArrayList<Integer> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        boolean swapFlag = true;
        int j = list.size()-1;
        int i = list.size()-1;
        // Check first part is equal to inverted
        while(i > 0){
            if (sortedList.get(i).equals(list.get(i))) {
                j--;
            }else{
                break;
            }
            i--;
        }
        int sortedIndex = 0;

        // Check if rest of the list is sorted
        for (int index = j; index > 0; index--){
            if (!(sortedList.get(sortedIndex).equals(list.get(index)))){
                swapFlag = false;
            }
            sortedIndex++;
        }
        return swapFlag;
    }

    private boolean heuristicInv2(ArrayList<Integer> list){
        ArrayList<Integer> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        ArrayList<Integer> invertedList = new ArrayList<>(sortedList);
        Collections.reverse(invertedList);
        boolean swapFlag2 = true;

        int j = 0;
        int i = 0;

        // Check first part is equal to inverted
        while(i < list.size()){
            if (list.get(i).equals(invertedList.get(i))){
                j++;
            }else{
                break;
            }
            i++;
        }

        int sortedIndex = 0;

        // Check if rest of the list is sorted
        for (int index = j; index < list.size(); index++){
            if (!(sortedList.get(sortedIndex).equals(list.get(index)))){
                swapFlag2 = false;
            }
            sortedIndex++;
        }
        return swapFlag2;
    }

    public int heuristic() {
        ArrayList<Integer> list = new ArrayList<>(value.getList());
        ArrayList<Integer> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        ArrayList<Integer> invertedList = new ArrayList<>(sortedList);
        Collections.reverse(invertedList);
        int swap = 0;


        if (list.equals(sortedList)){
            return 0;
        }

        // If not execute second part of heuristic function
        for (int i = 0; i < list.size(); i++) {
            if (this.heuristicInv1(list)) {
                return  swap + 1;
            }
            if (this.heuristicInv2(list)){
                return swap + 2;
            }
            if (list.get(i) != i + 1) {
                Collections.swap(list, i, list.get(i) - 1);
                swap += 1;
            }
        }
        return swap;

    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public ElementList getValue() {
        return value;
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getCost() {
        return cost;
    }

    public void printStatus() {
        System.out.println("__________________\n" + nodeName);
        System.out.println(value.getList() + "\n__________________");
        for (int i = 0; i < value.getList().size() - 1; i++) {
            System.out.print(children.get(i).getNodeName());
            System.out.println(children.get(i).getValue().getList());

        }
    }

}