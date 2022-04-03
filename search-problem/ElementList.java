import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class ElementList {

    private ArrayList<Integer> list;

    public ElementList(){}

    public ElementList(ArrayList<Integer> list) {
        this.list = list;
    }

    public ArrayList<Integer> makeList(int arraySize) {
        // A function to create an input list
        Scanner input = new Scanner(System.in);
        Integer num;
        System.out.println("Please enter the elements of the list: ");
        int elements = 1;

        while (true) {
            System.out.print("Element " + elements + ": ");
            num = input.nextInt();
            if (list.contains(num)) {
                System.out.println("Invalid input you cannot enter the same element twice" +
                        "\nTry again: ");
            } else {
                list.add(num);
                elements++;
                if (list.size() == arraySize) {
                    break;
                }
            }
        }
        return (list);
    }

    public void operator(int index) {
        List<Integer> head = list.subList(0, index);
        for (int i = 0, j = index - 1; i < j; i++) {
            head.add(i, head.remove(j));
        }
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> newList) {
        this.list = new ArrayList<>(newList);
    }

}