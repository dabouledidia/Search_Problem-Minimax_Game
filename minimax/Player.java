public class Player {
	private final String playerSymbol;
	private String position;
	private final Table table;

	public Player(Table table, String position, String playerSymbol) {
		this.table = table;
		this.position = position;
		this.playerSymbol = playerSymbol;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return position;
	}

	public String getPlayerSymbol() {
		return playerSymbol;
	}

}