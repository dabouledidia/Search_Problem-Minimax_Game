import java.util.*;

public class MainEngine
{
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<>();
		ElementList elementList1 = new ElementList(list);

		Scanner inputSize = new Scanner(System.in); // Input size of list
		Scanner inputChoice = new Scanner(System.in);	// Input algorithm choice

		System.out.print("Please enter the size of the list: ");
		int arraySize = inputSize.nextInt();

		System.out.println("Start list: " + elementList1.makeList(arraySize));

		// Initialise list as first node of the tree
		Node startNode = new Node("StartNode", null, elementList1,0);
		ArrayList<Integer> sortedList = new ArrayList<>(list);
		Node endNode;
		Collections.sort(sortedList);
		System.out.println("Sorted List: " + sortedList);

		// Choose algorithm to run
		System.out.println("Choose which algorithm to run \n 1.UCS (BFS) \n 2.AlphaStar \n 3.Exit");
		int choice = inputChoice.nextInt();
		Algo algo = new Algo(sortedList);

		switch(choice){
			case 1:
				endNode = algo.runBFS(startNode);
				endNode.findPath();
				break;
			case 2:
				endNode = algo.runAlphaStar(startNode);
				endNode.findPath();
				break;
			case 3:
				break;
		}
	}
}