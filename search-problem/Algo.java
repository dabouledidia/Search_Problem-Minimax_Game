import java.util.ArrayList;

public class Algo {

    ArrayList<Integer> sortedList;
    private int expansions = 0;

    public Algo(ArrayList<Integer> sortedList) {
        this.sortedList = sortedList;
    }

    public Node runBFS(Node startNode) {
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(startNode);
        Node tempNode;

        while (!queue.isEmpty()) {
            // First item of queue
            tempNode = queue.get(0);

            // Add node's children to the queue
            expansions++;
            tempNode.makeChildren();
            queue.addAll(tempNode.getChildren());

            // Finish if queue item is the sorted list
            if (sortedList.equals((tempNode.getValue().getList()))) {
                System.out.println("FOUND!" + tempNode.getValue().getList() +
                        "\nExpansions:" + expansions);
                return tempNode;
            }
            queue.remove(0);
        }
        return null;
    }

    public Node runAlphaStar(Node startNode){
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(startNode);
        Node tempNode = null;

        double minValue = 10000;

        while (!queue.isEmpty()) {

            // Find the node with the least cost value
            for (Node node : queue) {
                if (node.heuristic() + node.getCost() < minValue) {
                    minValue = node.heuristic() + node.getCost();
                    tempNode = node;
                }
            }

            // TESTING
            /*
            System.out.println(minValue + "Min");
            for (int i = 0; i< queue.size(); i++) {
                System.out.print(queue.get(i).getValue().getList());
                System.out.println(" " + queue.get(i).heuristic() + " " + queue.get(i).getCost());
            }
            */

            minValue = 10000;
            queue.remove(tempNode);

            // Add node's children to the queue
            expansions++;
            tempNode.makeChildren();
            queue.addAll(tempNode.getChildren());

            // Finish if queue item is the sorted list
            if (sortedList.equals((tempNode.getValue().getList()))) {
                System.out.println("FOUND!" + tempNode.getValue().getList() +
                        "\nExpansions:" + expansions);
                return tempNode;
            }

        }
        return null;
    }
}
