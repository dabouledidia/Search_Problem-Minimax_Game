import java.util.Scanner;

class MainGame{
	public static void main(String[] args) {

		if (args.length < 3 ){
			System.out.println("USAGE: MainGame moveLimit depth initiateBlackBlocks(Boolean)");
			System.exit(0);
		}

		int moveLimit = Integer.valueOf(args[0]); // Defines the move limit of the game.
		int depth = Integer.valueOf(args[1]);	  // MAX looks depth moves ahead to decide its next move.
		boolean initiateBlackBlocks = Boolean.parseBoolean(args[2]); // Initiate start board with blacked blocks



		Scanner size = new Scanner(System.in);
		System.out.print("Please enter the size of rows:");
		int row = size.nextInt();
		System.out.print("Please enter the size of columns:");
		int col = size.nextInt();
		String [][] board = new String[row][col];
		Table table = new Table(board, moveLimit);

		table.startGame(initiateBlackBlocks);
		table.gamePlay(depth);


	}
}