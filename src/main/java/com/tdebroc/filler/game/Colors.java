package com.tdebroc.filler.game;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Colors {

	private static char[] colors = "ROJVBI".toCharArray();

	private static Scanner scanner = new Scanner(System.in);

	public static char getRandomColor() {
		return colors[(int)(colors.length * Math.random())];
	}

	public static char askColor(List<Player> players) {
		char c;
		System.out.println("Enter a color from the list: " + Arrays.toString(colors));
		do {
			c = (scanner.nextLine()).toUpperCase().charAt(0);
			System.out.println("You choosed : " + c + " ");
		} while (!isValidColor(c, players));
		System.out.println("Color is ok");
		return c;
	}

    public static boolean isValidColor(char c, List<Player> players) {
        return isExistingColor(c) && !isTakenColor(c, players);
    }

	public static boolean isTakenColor(char c, List<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerColor() == c) {
				System.out.println("color " + c + " is taken by player " + i);
				return true;
			}
		}
		return false;
	}


	public static boolean isExistingColor(char c) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i] == c) {
				return true;
			}
		}
		System.out.println("Wrong color");
		return false;
	}

	public static char[] getColors() {
		return colors;
	}

	public static void setColors(char[] colors) {
		Colors.colors = colors;
	}
}
