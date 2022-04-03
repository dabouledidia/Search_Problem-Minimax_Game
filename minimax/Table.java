import java.util.ArrayList;
import java.util.Scanner;

public class Table{

	private final int rowSize;
	private final int colSize;
	private Player max;
	private Player min;
	private String[][] table;
	private int moveLimit;

	public Table(String[][] table, int moveLimit) {
		this.rowSize = table.length;
		this.colSize = table[1].length;
		this.table = table;
		this.moveLimit = moveLimit;
	}

	public void startGame(boolean initiateBlackBlocks){
		String positionA;
		String positionB;
		Scanner pos = new Scanner(System.in);

		System.out.println("Move limit is: " + moveLimit);

		this.initiateBoard(initiateBlackBlocks);

		// Place max on the centre of the board.The best initial position as per our utility "rule".

		//positionA = this.mergePosition((int)Math.floor(rowSize/2), (int)Math.floor(colSize/2));
		positionA = this.placeMax();
		int rowA = this.splitPosition(positionA)[0];
		int colA = this.splitPosition(positionA)[1];

		max = new Player(this, positionA, "A");

		// Place max on the board

		table[rowA][colA] = max.getPlayerSymbol();
		this.makeBoard();

		// Input for player B
		System.out.print("Please enter the initial position of player B:");
		positionB = pos.next().toUpperCase();
		int rowB = this.splitPosition(positionB)[0];
		int colB = this.splitPosition(positionB)[1];
		while(true){
			if(rowSize <= rowB || rowB < 0|| colSize <= colB || colB < 0 || !table[rowB][colB].equals("0")){
				System.out.print("Invalid position.\n" + "Please try again: ");
			}else{
				// Place min on the board
				min = new Player(this, positionB, "B");
				table[rowB][colB] = min.getPlayerSymbol();
				break;
			}
			positionB = pos.next().toUpperCase();
			rowB = this.splitPosition(positionB)[0];
			colB = this.splitPosition(positionB)[1];

		}

		this.makeBoard();
	}

	public void gamePlay(int depth){
		Scanner	minInput = new Scanner(System.in);

		String maxTargetPosition;
		String minTargetPosition;


		Minimax minimax = new Minimax(depth);
		TNode node;
		ArrayList<TNode> nodeArray;

		System.out.println("----------START----------");
		while (true){

			// Max's turn

			// If min has no remaining moves max wins
			if (!this.getNeighbourhood(max, 1, true).contains("0")){
				System.out.println("MIN WON!");
				break;
			}

			// Create a parent node for current state
			node = new TNode(this, null, 0);
			nodeArray = new ArrayList<>();
			nodeArray.add(node);

			// Run minimax to get the optimal move
			nodeArray = minimax.makeTree(nodeArray, 0, true);
			maxTargetPosition =  minimax.runMinimax(nodeArray, 0, false);

			this.movePlayerTo(max, maxTargetPosition);
			this.makeBoard();

			// Min's turn

			// If max has no remaining moves min wins
			if (!this.getNeighbourhood(min, 1, true).contains("0")){
				System.out.println("MAX WON!");
				break;
			}

			System.out.print("(MIN)Please enter your next move:");
			minTargetPosition = minInput.next().toUpperCase();
			while(this.movePlayerTo(min, minTargetPosition) != 0){
				System.out.print("Please enter your next move: ");
				minTargetPosition = minInput.next().toUpperCase();
			}
			this.availableMoves(min);
			this.makeBoard();

		}

	}

	//This method checks if the given move is valid or invalid.
	public ArrayList<Integer> validMove(String currentPosition, String targetPosition){
		int currentRow = this.splitPosition(currentPosition)[0];
		int targetRow = this.splitPosition(targetPosition)[0];
		int currentColumn = this.splitPosition(currentPosition)[1];
		int targetColumn = this.splitPosition(targetPosition)[1];

		// Keep track of steps for target position
		ArrayList<String> steps = new ArrayList<>();
		ArrayList<Integer> blocks = new ArrayList<>();

		if (currentPosition.equals(targetPosition)){
			return blocks;
		}

		// Horizontal movement
		if (currentRow == targetRow) {
			if (currentColumn < targetColumn) {
				for (int i = currentColumn; i <= targetColumn; i++) {
					steps.add(table[currentRow][i]);
					blocks.add(currentRow);
					blocks.add(i);
				}
			}else {
				for (int i = currentColumn; i >= targetColumn; i--) {
					steps.add(table[currentRow][i]);
					blocks.add(currentRow);
					blocks.add(i);
				}
			}

		// Vertical movement
		}else if (currentColumn == targetColumn) {
			if (currentRow < targetRow){
				for (int i = currentRow; i <= targetRow; i++) {
					steps.add(table[i][currentColumn]);
					blocks.add(i);
					blocks.add(currentColumn);
				}
			}else{
				for (int i = currentRow; i >= targetRow; i--) {
					steps.add(table[i][currentColumn]);
					blocks.add(i);
					blocks.add(currentColumn);
				}
			}

		// Diagonal movement
		}else if (Math.abs(currentRow - targetRow) == Math.abs(currentColumn - targetColumn)){
			if (currentRow < targetRow && currentColumn < targetColumn){
				for (int i = currentRow,j = currentColumn; i <= targetRow && j <= targetColumn; i++,j++) {
					steps.add(table[i][j]);
					blocks.add(i);
					blocks.add(j);
				}
			}else if(currentRow < targetRow){
				for (int i = currentRow,j = currentColumn; i <= targetRow && j >= targetColumn; i++,j--) {
					steps.add(table[i][j]);
					blocks.add(i);
					blocks.add(j);
				}
			}else if(currentColumn < targetColumn){
				for (int i = currentRow,j = currentColumn; i >= targetRow && j <= targetColumn; i--,j++) {
					steps.add(table[i][j]);
					blocks.add(i);
					blocks.add(j);
				}
			}else{
				for (int i = currentRow,j = currentColumn; i >= targetRow && j >= targetColumn; i--,j--) {
					steps.add(table[i][j]);
					blocks.add(i);
					blocks.add(j);
				}
			}
		// Invalid Move
		}else {
			blocks.clear();
			return blocks;
		}

		// Check for black blocks
		steps.remove(0);
		for (String step : steps) {
			if ((step.equals("X")) || step.equals("A") || step.equals("B")){
				blocks.clear();
				return blocks;
			}else if(steps.size()-1 > moveLimit-1){
				blocks.clear();
				return blocks;
			}
		}
		return blocks;
	}
	//This method is responsible to execute the move the players to the given -valid- input.
	public int movePlayerTo(Player player, String targetPosition) {
		ArrayList<Integer> steps = this.validMove(player.getPosition(), targetPosition);

		// Copy initial board in case of invalid move
		String[][] restoreBoard = new String[rowSize][colSize];
		for (int i = 0; i < rowSize; i++){
			System.arraycopy(table[i], 0, restoreBoard[i], 0, colSize);
		}

		/*
		--------FOR DEBUGGING----------

		//Keep track of steps for target position

		System.out.println(player.getPlayerSymbol() + ": " + player.getPosition()
				+" -> " + targetPosition);
		*/

		// If move is invalid restore the board
		if (!steps.isEmpty()){
			for (int i =0; i < steps.size(); i=i+2){
					table[steps.get(i)][steps.get(i+1)] = "X";

			}
			table[steps.get(steps.size()-2)][steps.get(steps.size()-1)] = player.getPlayerSymbol();
			player.setPosition(targetPosition);
			return 0;
		}else{
			System.out.println("Invalid move!");
			table = restoreBoard;
			return -1;
		}
	}
	// Print board
	public void makeBoard(){

		int rowSize =  table.length;
		int colSize =  table[0].length;
		System.out.print("     ");
		for(int i = 0; i < colSize; i++){
			System.out.print(i + "  ");
		}
		System.out.print("\n" + "    ");

		for (int row = 0; row < rowSize; row++){
		    System.out.print("\n" + (char)(row + 65) + "  ");

		    for (int col = 0; col < colSize; col++){
		          System.out.print("  " + table[row][col] );
		    }

		}
	    System.out.println("\n");
	}

	//This method checks the "Neighbourhood" (taken or not positions in MAX's move limit).
	public ArrayList<String> getNeighbourhood(Player player, int size, Boolean value) {
		int currentRow = this.splitPosition(player.getPosition())[0];
		int currentColumn = this.splitPosition(player.getPosition())[1];

		ArrayList<String> neighbours = new ArrayList<>();

		for (int i = currentRow - size ; i < currentRow + size +1; i++){
			for (int j = currentColumn-size; j < currentColumn + size +1; j++){
				if(i >= 0 && i < rowSize && j >= 0 && j < colSize){
					if (value){
						neighbours.add(table[i][j]);
					}else{
						neighbours.add(this.mergePosition(i, j));
					}
				}
			}
		}
		return neighbours;
	}

	private int[] splitPosition(String positionStr){
		int[] position = new int[2];
		char letter = positionStr.charAt(0);
		char number = positionStr.charAt(1);
		position[0] = (int) letter - 65;
		position[1] = (int) number - 48;
		return position;
	}

	private String mergePosition(int letter, int number){
		String positionLetter = String.valueOf((char) (letter + 65));
		String positionNum = String.valueOf((char) (number + 48));
		return  positionLetter+positionNum;
	}

	// Checks for possible moves.
	public ArrayList<String> availableMoves(Player player){
		ArrayList<String> neighbourhood = this.getNeighbourhood(player, moveLimit, false);
		ArrayList<String> availableMoves = new ArrayList<>();

		for (String move : neighbourhood) {
			if (!this.validMove(player.getPosition(), move).isEmpty()) {
				availableMoves.add(move);
			}
		}
		//System.out.println(availableMoves);
		return availableMoves;
	}

	public ArrayList<String> initiateBoard(boolean blackBlocks){
		Scanner input = new Scanner(System.in);
		ArrayList<String> board = new ArrayList<>();
		ArrayList<String> blocks = new ArrayList<>();

		// Initiate table with zeros
		for (int i=0; i < rowSize; i++) {
			for (int j=0; j < colSize; j++) {
				table[i][j] = "0";
			}
		}

		if (blackBlocks){
			this.makeBoard();
			// 1/3 of the board is allowed to fill with black blockss
			int maximumBlocks = (int) Math.floor(rowSize * colSize - rowSize * colSize * 2 / 3);
			System.out.print("Type \"exit\" to exit \n" + blocks.size() + "/" + maximumBlocks +
					" Please enter block to mark: ");
			String block = input.next().toUpperCase();
			int blockRow = this.splitPosition(block)[0];
			int blockCol = this.splitPosition(block)[1];

			while(!block.equals("EXIT")){
				if ((blockRow >= 0 && blockRow < rowSize) && (blockCol >= 0 && blockCol < colSize)
						&& !blocks.contains(block)){
					table[blockRow][blockCol] = "X";
					blocks.add(block);
					this.makeBoard();
					if(blocks.size() > maximumBlocks){
						System.out.println("All blocks marked");
						break;
					}
					System.out.print(blocks.size() + "/" + maximumBlocks + " Please enter next block or exit:");
				}else{
					System.out.print("Invalid block\nPLease try again:");
				}

				block = input.next().toUpperCase();
				blockRow = this.splitPosition(block)[0];
				blockCol = this.splitPosition(block)[1];
			}
		}
		return board;
	}

	public String placeMax(){
		Player tempPlayer = new Player(this, "A0", "T");
		String position = tempPlayer.getPosition();
		String currentPosition;
		int maxValue = 0;
		int value;
		for (int i = 0; i < rowSize; i++){
			for (int j = 0; j< colSize; j++){
				currentPosition = this.mergePosition(i, j);
				tempPlayer.setPosition(currentPosition);
				value = this.availableMoves(tempPlayer).size();
				//System.out.print(value + "   ");
				if (value > maxValue && table[i][j].equals("0")){
					maxValue = value;
					position = tempPlayer.getPosition();

				}
			}
			//System.out.println(" ");
		}
		return position;
	}

	public int getColSize() {
		return colSize;
	}

	public int getRowSize() {
		return rowSize;
	}

	public String[][] getTable(){
		return table;
	}

	public Player getMax(){
		return max;
	}

	public Player getMin(){
		return min;
	}

	public int getMoveLimit(){
		return moveLimit;
	}

	public void setMax(Player max){
		this.max = max;
	}

	public void setMin(Player min){
		this.min = min;
	}

}