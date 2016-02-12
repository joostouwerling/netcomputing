package org.netcomputing.sockets.knockknock;

public class KnockKnockProtocol {
	private static final int WAITING = 0;
	private static final int SENTKNOCKKNOCK = 1;
	private static final int SENTCLUE = 2;
	private static final int ANOTHER = 3;
	private static final int NUMJOKES = 5;
	private int state = WAITING;
	private int currentJoke = 0;
	private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
	private String[] answers = { "Turnip the heat, it's cold in here!", "I didn't know you could yodel!", "Bless you!", "Is there an owl in here?",
			"Is there an echo in here?" };

	public String processInput(String in) {
		String out = null;
		if (state == WAITING) {
			out = "Knock! Knock!";
			state = SENTKNOCKKNOCK;
		} else if (state == SENTKNOCKKNOCK) {
			if (in.equalsIgnoreCase("Who's there?")) {
				out = clues[currentJoke];
				state = SENTCLUE;
			} else {
				out = "You're supposed to say \"Who's there?\"! " + "Try again. Knock! Knock!";
			}
		} else if (state == SENTCLUE) {
			if (in.equalsIgnoreCase(clues[currentJoke] + " who?")) {
				out = answers[currentJoke] + " Want another? (y/n)";
				state = ANOTHER;
			} else {
				out = "You're supposed to say \"" + clues[currentJoke] + " who?\"" + "! Try again. Knock! Knock!";
				state = SENTKNOCKKNOCK;
			}
		} else if (state == ANOTHER) {
			if (in.equalsIgnoreCase("y")) {
				out = "Knock! Knock!";
				if (currentJoke == (NUMJOKES - 1))
					currentJoke = 0;
				else
					currentJoke++;
				state = SENTKNOCKKNOCK;
			} else {
				out = "Bye.";
				state = WAITING;
			}
		}
		return out;
	}
}
