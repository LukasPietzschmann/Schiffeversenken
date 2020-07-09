package logic;

import ai.AI;
import ai.Difficulty;
import network.Network;

import java.util.ArrayList;

/**
 * Die Klasse Logik steuert den kompletten Spielablauf.
 */
public class Logic {
	private final int MODE;
	/**
	 * Referenz auf einen der beiden Spieler.
	 */
	public Player ownPlayer;
	/**
	 * Referenz auf einen der beiden Spieler.
	 */
	public Player oppPlayer;
	private ArrayList<Ship> ships;
	
	private Logic(int MODE) {
		this.MODE = MODE;
	}
	
	/**
	 * Konstruktor, falls ein Save-Game geladen wird.
	 *
	 * @param id Die ID des Save-Games.
	 */
	public Logic(long id) {
		this(Launcher.SG);
		//TODO implement
	}

	/**
	 *
	 * @param mode
	 * @param ship2Count
	 * @param ship3Count
	 * @param ship4Count
	 * @param ship5Count
	 */
	private Logic(int mode, int ship2Count, int ship3Count, int ship4Count, int ship5Count) {
		this(mode);
		ships = new ArrayList<>();
		int[] ships = {ship2Count, ship3Count, ship4Count, ship5Count};
		
		for(int i = 0; i < ships.length; i++) {
			for(int j = 0; j < ships[i]; j++) {
				this.ships.add(new Ship(0, 0, Direction.north, i + 2));
			}
		}
	}

	/**
	 * Konstruktor für AI vs AI
	 * @param nameAI1
	 * @param nameAI2
	 * @param difficultyAI1
	 * @param difficultyAI2
	 * @param size
	 * @param ship2Count
	 * @param ship3Count
	 * @param ship4Count
	 * @param ship5Count
	 */
	public Logic(String nameAI1, String nameAI2, Difficulty difficultyAI1, Difficulty difficultyAI2, int size, int ship2Count, int ship3Count, int ship4Count, int ship5Count) {
		this(Launcher.AI_AI, ship2Count, ship3Count, ship4Count, ship5Count);
		ownPlayer = new AI(this, size, nameAI1, difficultyAI1);
		oppPlayer = new AI(this, size, nameAI2, difficultyAI2);
	}
	
	/**
	 * Konstruktor, falls eine {@link AI} gegen einen Gegner übers Netzwerk spielt und man selbst der Client ist.
	 *
	 * @param nameAI Der Name der AI.
	 * @param nameNW Der Name des Gegners.
	 * @param difficulty Die Schwierigkeit der AI.
	 * @param IP Die IP Adresse des Servers.
	 */
	public Logic(String nameAI, String nameNW, Difficulty difficulty, String IP) {
		this(Launcher.NW_CL_AI);
		oppPlayer = new Network(this, nameNW, IP);
		ships = ((Network) oppPlayer).getShips();
		ownPlayer = new AI(this, ((Network) oppPlayer).getSize(), nameNW, difficulty);
	}
	
	/**
	 * Konstruktor, falls eine {@link AI} gegen einen Gegner übers Netzwerk spielt und man selbst der Server ist.
	 *
	 * @param nameAI Der Name der AI.
	 * @param nameNW Der Name des Gegners.
	 * @param difficulty Die Schwierigkeit der AI.
	 * @param size Die Größe des Spielfelds.
	 */
	public Logic(String nameAI, String nameNW, Difficulty difficulty, int size, int ship2Count, int ship3Count, int ship4Count, int ship5Count) throws Exception {
		this(Launcher.NW_SV_AI, ship2Count, ship3Count, ship4Count, ship5Count);
		ownPlayer = new AI(this, size, nameAI, difficulty);
		oppPlayer = new Network(this, nameNW, size);
	}
	
	/**
	 * Konstruktor, falls eine {@link AI} gegen einen {@link Human} spielt.
	 *
	 * @param nameAI Der Name der AI.
	 * @param namePL Der Name des Spielers.
	 * @param difficulty Die Schwierigkeit der AI.
	 * @param size Die Größe des Spielfelds.
	 */
	public Logic(String nameAI, String namePL, int size, Difficulty difficulty, int ship2Count, int ship3Count, int ship4Count, int ship5Count) {
		this(Launcher.PL_AI, ship2Count, ship3Count, ship4Count, ship5Count);
		ownPlayer = new Human(this, size, namePL);
		oppPlayer = new AI(this, size, nameAI, difficulty);
	}
	
	/**
	 * Konstruktor, falls ein {@link Human} gegen einen Gegner übers Netzwerk spielt und man selbst der Client ist.
	 *
	 * @param namePl Der Name des Spielers.
	 * @param nameNW Der Name des Gegners.
	 * @param IP Die IP Adresse des Servers.
	 */
	public Logic(String namePl, String nameNW, String IP) {
		this(Launcher.PL_NW_CL);
		oppPlayer = new Network(this, nameNW, IP);
		ships = ((Network) oppPlayer).getShips();
		ownPlayer = new Human(this, ((Network) oppPlayer).getSize(), namePl);
	}
	
	/**
	 * Konstruktor, falls ein {@link Human} gegen einen Gegner übers Netzwerk spielt und man selbst der Server ist.
	 *
	 * @param namePl Der Name des Spielers.
	 * @param nameNW Der Name des Gegners.
	 * @param size Die Größe des Spielfelds
	 */
	public Logic(String namePl, String nameNW, int size, int ship2Count, int ship3Count, int ship4Count, int ship5Count) throws Exception {
		this(Launcher.PL_NW_SV, ship2Count, ship3Count, ship4Count, ship5Count);
		ownPlayer = new Human(this, size, namePl);
		oppPlayer = new Network(this, nameNW, size);
	}
	
	/**
	 * Schießt auf einen Spieler.
	 *
	 * @param x x-Koordinate auf die geschossen wird.
	 * @param y y-Koordinate auf die geschossen wird.
	 * @param player Referenz auf den schießenden Spieler.
	 * @return {@code null}, falls nicht getroffen wurde. Das konkrete {@link Ship},falls getroffen wurde.
	 */
	public Ship shoot(int x, int y, Player player) {
		if(player == ownPlayer) {
			return oppPlayer.hit(x, y);
		}else {
			return ownPlayer.hit(x, y);
		}
	}
	
	/**
	 * Startet das Spiel.
	 */
	public void startGame() {
		// grobe Implementation
		// kannst liebend gerne anders schreiben
		boolean hit;
		
		Player currPlayer, otherPlayer;
		switch(MODE) {
			case Launcher.NW_CL_AI:
			case Launcher.PL_NW_CL:
				currPlayer = oppPlayer;
				otherPlayer = ownPlayer;
				break;
			default: //PL_AI, NW_SV_AI, PL_NW_SV
				currPlayer = ownPlayer;
				otherPlayer = oppPlayer;
		}
		
		currPlayer.placeShips();
		otherPlayer.placeShips();
		while(true) {
			hit = true;
			while(hit) {
				if(!otherPlayer.isAlive()) {
					System.out.println(String.format("%s hat gewonnen!!", currPlayer.name));
					return;
				}
				
				hit = currPlayer.doWhatYouHaveToDo();
				System.out.println(otherPlayer.name);
				((LocalPlayer) otherPlayer).dumpMap();
				System.out.println("\n");
			}
			
			Player temp = currPlayer;
			currPlayer = otherPlayer;
			otherPlayer = temp;
		}
	}
	
	/**
	 * Gibt alle Schiffe zurück, die platziert werden dürfen.
	 *
	 * @return Eine Liste an Schiffen, die platziert werden können.
	 */
	public ArrayList<Ship> getAvailableShips() {
		return ships;
	}
	
	public LocalPlayer getOwnPlayer(){
		return (LocalPlayer) ownPlayer;
	}
}
