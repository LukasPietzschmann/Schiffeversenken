package logic;

/**
 * Die Klasse Player modelliert einen Spieler auf der höchsten Abstraktionsebene.
 */
public abstract class Player {
	
	/**
	 * "Zurück-Referenz" auf das Logik Objekt.
	 */
	protected Logic logic;
	/**
	 * Der vom Spieler festgelegte Name. Dient nur zur Anzeige in der GUI.
	 */
	protected String name;
	
	/**
	 * @param logic "Zurück-Referenz" auf das Logik Objekt. Typischerweise {@code this}.
	 * @param name Der vom Spieler festgelegte Name. Dient nur zur Anzeige in der GUI.
	 */
	public Player(Logic logic, String name) {
		this.logic = logic;
		this.name = name;
	}
	
	/**
	 * Weißt den Spieler darauf hin, dass er an der Reihe ist, seinen Zug zu machen.
	 *
	 * @return {@code true}, falls der Spieler erfolgreich getroffen hat. Sonst {@code false}.
	 */
	public abstract boolean doWhatYouHaveToDo();
	
	/**
	 * Wird von der Klasse {@link Logic} aufgerufen, um auf den Spieler zu schießen.
	 *
	 * @param x Die x-Koordinate des Schusses
	 * @param y Die y-Koordinate des Schusses
	 * @return {@code null}, falls nicht getroffen wurde. Das konkrete {@link Ship}, falls getroffen wurde.
	 */
	public abstract Ship hit(int x, int y);
	
	/**
	 * Weißt den Spieler darauf hin, dass er an der Reihe ist seine Schiffe zu platzieren.
	 */
	public abstract void placeShips();
	
	/**
	 * Gibt zurück, ob der Spieler noch Schiffe besitzt.
	 *
	 * @return {@code true}, falls der Spieler noch "lebende" Schiffe hat. Sonst {@code false}.
	 */
	public abstract boolean isAlive();
}