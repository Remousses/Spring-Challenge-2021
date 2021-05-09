package league.bronze;

import java.util.*;

class Player {
	int oldTreeNumber = 0;

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		Game game = new Game();

		int numberOfCells = in.nextInt();
		for (int i = 0; i < numberOfCells; i++) {
			int index = in.nextInt();
			int richness = in.nextInt();
			int neigh0 = in.nextInt();
			int neigh1 = in.nextInt();
			int neigh2 = in.nextInt();
			int neigh3 = in.nextInt();
			int neigh4 = in.nextInt();
			int neigh5 = in.nextInt();
			int neighs[] = new int[] { neigh0, neigh1, neigh2, neigh3, neigh4, neigh5 };
			Cell cell = new Cell(index, richness, neighs);
			if (richness == 0) {
				cell.setBusy(true);
			}
			game.board.add(cell);
		}

		while (true) {
			clearData(game);
			game.day = in.nextInt();
			game.nutrients = in.nextInt();
			game.mySun = in.nextInt();
			game.myScore = in.nextInt();
			game.opponentSun = in.nextInt();
			game.opponentScore = in.nextInt();
			game.opponentIsWaiting = in.nextInt() != 0;
			System.err.println("game.day : " + game.day);
			
			int numberOfTrees = in.nextInt();
			for (int i = 0; i < numberOfTrees; i++) {
				int cellIndex = in.nextInt();
				int size = in.nextInt();
				boolean isMine = in.nextInt() != 0;
				boolean isDormant = in.nextInt() != 0;
				Tree tree = new Tree(cellIndex, size, isMine, isDormant);
				game.trees.add(tree);
				game.board.get(cellIndex).setBusy(true);
			}

			int numberOfPossibleActions = in.nextInt();
			in.nextLine();
			for (int i = 0; i < numberOfPossibleActions; i++) {
				String possibleAction = in.nextLine();
				game.possibleActions.add(Action.parse(possibleAction));
			}

			Action action = game.getNextAction();
			System.out.println(action);
		}
	}
	
	private static void clearData(final Game game) {
		game.seeds.clear();
		game.twigs.clear();
		game.mediumTrees.clear();
		game.bigTrees.clear();
		game.trees.clear();
		game.possibleActions.clear();
	}
}
