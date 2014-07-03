package optimal.ghost.game;

import java.util.EnumSet;

public enum Player {

	HUMAN, COMPUTER;

	public static Player getFromOrdinal(int ordinal) {

		for (Player player : Player.values()) {
			if (player.ordinal() == ordinal) {
				return player;
			}
		}

		// This should never happen...
		throw new IllegalArgumentException("Illegal ordinal [" + ordinal + "]");
	}

	public static Player getFirstPlayer() {
		return getFromOrdinal(0);
	}

	public static EnumSet<Player> allPlayerExceptOne(Player player) {
		if (player == null) {
			return null;
		}

		return EnumSet.complementOf(EnumSet.of(player));
	}

	public static Player getNextPlayer(Player player) {
		if (player == null) {
			return Player.getFromOrdinal(0);
		}

		return getFromOrdinal((player.ordinal() + 1) % Player.values().length);
	}
}