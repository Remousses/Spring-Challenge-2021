package league.bronze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

class Game {
	int day;
	int nutrients;
	List<Cell> board;
	List<Action> possibleActions;
	List<Tree> seeds;
	List<Tree> twigs;
	List<Tree> mediumTrees;
	List<Tree> bigTrees;
	List<Tree> trees;
	int mySun, opponentSun;
	int myScore, opponentScore;
	boolean opponentIsWaiting;
	private static final int[][] CELL_RICHESS = new int [][] {
		{0, 1, 2, 3, 4, 5, 6},
		{7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
		{19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37}
	};

	public Game() {
		board = new ArrayList<>();
		possibleActions = new ArrayList<>();
		seeds = new ArrayList<>();
		twigs = new ArrayList<>();
		mediumTrees = new ArrayList<>();
		bigTrees = new ArrayList<>();
		trees = new ArrayList<>();
	}

	public Action getNextAction() {
		// TODO: write your algorithm here

		this.addMineTreeToCategory(this.seeds, this.twigs, this.mediumTrees, this.bigTrees);
		
		this.possibleActions.sort((data1, data2) -> {
			if (data1.getTargetCellIdx() != null && data2.getTargetCellIdx() != null) {
				return data1.getTargetCellIdx().compareTo(data2.getTargetCellIdx());
			}

			return 1;
		});
		this.possibleActions.forEach(System.err::println);
		
		List<Tree> seedsToGrow = new ArrayList<>();
		List<Tree> twigsToGrow = new ArrayList<>();
		List<Tree> mediumTreesToGrow = new ArrayList<>();
		List<Tree> treesToComplete = new ArrayList<>();
		List<Action> treesToSeed = new ArrayList<>();

		this.possibleActions.forEach(action -> {
			switch (action.getType()) {
				case Action.SEED:
					treesToSeed.add(action);
					break;
				case Action.GROW:
					this.trees.stream().filter(tree -> tree.getCellIndex() == action.getTargetCellIdx()).findFirst().ifPresent(tree -> {
						this.addMineTreeToCategory(seedsToGrow, twigsToGrow, mediumTreesToGrow, null);
					});
					break;
				case Action.COMPLETE:
					this.trees.stream().filter(tree -> tree.getCellIndex() == action.getTargetCellIdx()).findFirst().ifPresent(tree -> {
						treesToComplete.add(tree);
					});
					break;
				case Action.WAIT:
				default:
					break;
			}
		});

		Map<Integer, List<Cell>> neighboursCell = new HashMap<>();

		for (Cell cell : this.board) {
			// Retrieve all neighbours
			List<Cell> neighbours = new ArrayList<>();
			this.handleNeighbours(cell, neighbours);

			neighboursCell.put(cell.getIndex(), neighbours);
		}
		
		// Grow
		final boolean goodSeedCost = this.computeCostToSeedGrow(seedsToGrow);
		final boolean goodTwigCost = this.computeCostToTwigGrow(twigsToGrow);
		final boolean goodMediumTreeCost = this.computeCostToMediumTreeGrow(mediumTreesToGrow);
		final boolean goodCompleteTreeCost = this.computeCostToCompleteTree(treesToComplete);
		
		if (day > 19) {
			for (Tree tree : mediumTreesToGrow) {
				final Action action = this.toDoAction(goodMediumTreeCost, tree);
				if (action != null) {
					return Action.parse(action.toString());
				}
			}
			for (Tree tree : treesToComplete) {
				final Action action = this.toDoAction(goodCompleteTreeCost, tree);
				if (action != null) {
					return Action.parse(action.toString());
				}
			}
		}
		
		if (day >= 15 || day <= 18) {
			for (Tree tree : mediumTreesToGrow) {
				final Action action = this.toDoAction(goodMediumTreeCost, tree);
				if (action != null) {
					return Action.parse(action.toString());
				}
			}
		}
		
		if (!this.checkMaxBigTreesSize()) {
			// Ordonne de la plus petite richesse à la plus grande
			final TreeMap<Cell, Action> treesToCut = new TreeMap<>((cell1, cell2) -> cell1.getRichess().compareTo(cell2.getRichess()));
			for (Tree tree : treesToComplete) {
				final Action action = this.toDoAction(goodCompleteTreeCost, tree);
				if (action != null) {
					System.err.println("My action : " + action);
					final Optional<Cell> treeToCut = this.board.stream().filter(cell -> {
							return cell.getIndex() == action.getTargetCellIdx();
					}).findFirst();
					System.err.println("My treeToCut : " + treeToCut);
					if (treeToCut.isPresent()) {
						treesToCut.put(treeToCut.get(), action);
					}
				}
			}
			if (!treesToCut.isEmpty()) {
				return Action.parse(treesToCut.firstEntry().getValue().toString());
			}
		}
		// si il y a plus de 3 arbres moyens alors en faire grandir un
		if (!this.checkMaxMediumTreesSize()) {
			for (Tree tree : mediumTreesToGrow) {
				final Action action = this.toDoAction(goodMediumTreeCost, tree);
				if (action != null) {
					return Action.parse(action.toString());
				}
			}
		}
		// si il y a plus de 3 branches alors en faire grandir une
		if (!this.checkMaxTwigsSize()) {
			for (Tree tree : twigsToGrow) {
				final Action action = this.toDoAction(goodTwigCost, tree);
				if (action != null) {
					return Action.parse(action.toString());
				}
			}
		}
		for (Tree tree : seedsToGrow) {
			final Action action = this.toDoAction(goodSeedCost, tree);
			if (action != null) {
				return Action.parse(action.toString());
			}
		}
		for (Tree tree : twigsToGrow) {
			final Action action = this.toDoAction(goodTwigCost, tree);
			if (action != null) {
				return Action.parse(action.toString());
			}
		}
		for (Tree tree : mediumTreesToGrow) {
			final Action action = this.toDoAction(goodMediumTreeCost, tree);
			if (action != null) {
				return Action.parse(action.toString());
			}
		}

		System.err.println("this.seeds.isEmpty() : " + this.seeds.isEmpty());
		System.err.println("!treesToSeed.isEmpty() : " + !treesToSeed.isEmpty());
		
		// Free move (planting only one seed)
		if (this.seeds.isEmpty() && !treesToSeed.isEmpty()) {
			System.err.println("treesToSeed");
			treesToSeed.forEach(System.err::println);
			Cell neighboursTarget = null;
			// Ordonner de la plus grande richess à la plus petite
			final List<Cell> targetRichess1 = new ArrayList<>();
			final List<Cell> targetRichess2 = new ArrayList<>();
			final List<Cell> targetRichess3 = new ArrayList<>();
			
			final BiConsumer<List<Cell>, Cell> consumer = (listCell, cell) -> listCell.add(cell);
			for (Tree tree : this.trees) {
				if (tree.isMine() && !tree.isDormant()) {
					System.err.println("yolo");
					
//					neighboursCell.entrySet().stream().filter(neighbour -> neighbour.getKey() == tree.getCellIndex()).flatMap(neighbour -> neighbour.getValue().stream())
//						.filter(cell -> !cell.isBusy()).forEach(cell -> {
//							System.err.println("CELELLELELELEL : "+ cell);
//							switch (cell.getRichess()) {
//							case 1:
//								consumer.accept(targetRichess1, cell);
//								break;
//							case 2:
//								consumer.accept(targetRichess2, cell);
//								break;
//							case 3:
//								consumer.accept(targetRichess3, cell);
//								break;
//							default:
//								break;
//							}
//						});
					System.err.println("The tree : " + tree);
					neighboursCell.get(tree.getCellIndex()).stream().filter(cell -> {
						System.err.println("CELELLELELELEL : "+ cell);
						return !cell.isBusy();
						}).forEach(cell -> {
						switch (cell.getRichess()) {
						case 1:
							consumer.accept(targetRichess1, cell);
							break;
						case 2:
							consumer.accept(targetRichess2, cell);
							break;
						case 3:
							consumer.accept(targetRichess3, cell);
							break;
						default:
							break;
						}
					});
				}
			}

			System.err.println("targetRichess3");
			targetRichess3.forEach(System.err::println);
			if (!targetRichess3.isEmpty()) {
				// Try to get good neighbour
				neighboursTarget = targetRichess3.get(0);
			}

			System.err.println("targetRichess2");
			targetRichess2.forEach(System.err::println);
			if (neighboursTarget == null && !targetRichess2.isEmpty()) {
				// Try to get good neighbour
				neighboursTarget = targetRichess2.get(0);
			}

			System.err.println("targetRichess1");
			targetRichess1.forEach(System.err::println);
			if (neighboursTarget == null && !targetRichess1.isEmpty()) {
				// Try to get good neighbour
				neighboursTarget = targetRichess1.get(0);
			}

			if (neighboursTarget != null) {
				System.err.println("neighboursTarget : " + neighboursTarget);
				System.err.println("treesToSeed for neighboursTarget");
				treesToSeed.forEach(System.err::println);

				for (Action action : treesToSeed) {
					if (action.getTargetCellIdx() == neighboursTarget.getIndex()) {
						System.err.println("Action.parse(action.toString()) : " + Action.parse(action.toString()));
						return Action.parse(action.toString());
					}
				}
			}
		}
		
		return Action.parse(Action.WAIT);
	}
	
	private boolean checkMaxTwigsSize() {
		return this.twigs.size() <= 3;
	}
	
	private boolean checkMaxMediumTreesSize() {
		return this.mediumTrees.size() <= 2;
	}
	
	private boolean checkMaxBigTreesSize() {
		return this.bigTrees.size() <= 3;
	}

	/**
	 * Faire pousser une graine en un arbre de taille 1 coûte 1 point de soleil + le nombre d'arbres de taille 1 que vous possédez déjà.
	 * @param seedsToGrow 
	 * @return
	 */
	private boolean computeCostToSeedGrow(final List<Tree> seedsToGrow) {
		if (seedsToGrow.isEmpty()) {
			return false;
		}
		final int initialCost = 1;
		final int nbAllMyTwigs = this.twigs.size();
		System.err.println("computeCostToSeedGrow : " + (initialCost + nbAllMyTwigs));
		return initialCost + nbAllMyTwigs <= this.mySun;
	}
	
	/**
	 * Faire pousser un arbre de taille 1 en un arbre de taille 2 coûte 3 points de soleil + le nombre d'arbres de taille 2 que vous possédez déjà.
	 * @param twigsToGrow 
	 * @return
	 */
	private boolean computeCostToTwigGrow(final List<Tree> twigsToGrow) {
		if (twigsToGrow.isEmpty()) {
			return false;
		}
		final int initialCost = 3;
		final int nbAllMyMediumTrees = this.mediumTrees.size();
		System.err.println("computeCostToTwigGrow : " + (initialCost + nbAllMyMediumTrees));
		return initialCost + nbAllMyMediumTrees <= this.mySun;
	}
	
	/**
	 * Faire pousser un arbre de taille 2 en un arbre de taille 3 coûte 7 points de soleil + le nombre d'arbres de taille 3 que vous possédez déjà.
	 * @param mediumTreesToGrow 
	 * @return
	 */
	private boolean computeCostToMediumTreeGrow(final List<Tree> mediumTreesToGrow) {
		if (mediumTreesToGrow.isEmpty()) {
			return false;
		}
		final int initialCost = 7;
		final int nbAllMyBigTrees = this.bigTrees.size();
		System.err.println("computeCostToMediumTreeGrow : " + (initialCost + nbAllMyBigTrees));
		return initialCost + nbAllMyBigTrees <= this.mySun;
	}
	
	/**
	 * Faire pousser un arbre de taille 2 en un arbre de taille 3 coûte 7 points de soleil + le nombre d'arbres de taille 3 que vous possédez déjà.
	 * @param mediumTreesToGrow 
	 * @return
	 */
	private boolean computeCostToCompleteTree(final List<Tree> treesToComplete) {
		if (treesToComplete.isEmpty()) {
			return false;
		}
		final int initialCost = 4;
		System.err.println("computeCostToCompleteTree : " + initialCost);
		return initialCost <= this.mySun;
	}
	
	private Action toDoAction(final boolean goodCost, final Tree tree) {
		if (goodCost) {
			final Optional<Action> optionalAction = this.possibleActions.stream().filter(action -> {
				if (action.getTargetCellIdx() != null) {
					return tree.getCellIndex() == action.getTargetCellIdx();
				}
				
				return false;
			}).findFirst();
			System.err.println("toGrow optionalAction : " + optionalAction);
			
			if (optionalAction.isPresent()) {
				return Action.parse(optionalAction.get().toString());
			}
		}
		
		return null;
	}

	/**
	 * Add category for each trees that I have.
	 * @param seedsList
	 * @param twigsList
	 * @param mediumTreesList
	 * @param bigTreesList
	 * @param canGrow
	 */
	private void addMineTreeToCategory(final List<Tree> seedsList, final List<Tree> twigsList, final List<Tree> mediumTreesList, final List<Tree> bigTreesList) {
		final BiConsumer<List<Tree>, Tree> consumer = (listTrees, tree) -> {
			listTrees.add(tree);
		};
		this.trees.stream().filter(Tree::isMine).forEach(tree -> {
			switch (tree.getSize()) {
				case 0:
					consumer.accept(seedsList, tree);
					break;
				case 1:
					consumer.accept(twigsList, tree);
					break;
				case 2:
					consumer.accept(mediumTreesList, tree);
					break;
				case 3:
					if (bigTreesList != null) {
						consumer.accept(bigTreesList, tree);
					}
					break;
				default:
					break;
			}
		});
	}
	
//	private void customTree(final Tree tree, final Consumer<List<Tree>> action, final List<Tree> seedsList, final List<Tree> twigsList, final List<Tree> mediumTreesList, final List<Tree> bigTreesList) {
//		switch (tree.getSize()) {
//			case 0:
//				action.accept(seedsList);
//				break;
//			case 1:
//				action.accept(twigsList);
//				break;
//			case 2:
//				action.accept(mediumTreesList);
//				break;
//			case 3:
//				action.accept(bigTreesList);
//				break;
//			default:
//				break;
//		}
//	}

	private void handleNeighbours(final Cell cell, final List<Cell> neighbours) {
		for (int neigh : cell.getNeighbours()) {
			if (neigh != -1) {
				final Cell neighCell = this.board.get(neigh);
				if (neighCell.getRichess() == 0 && neighCell.isBusy()) {
					continue;
				}
				neighbours.add(neighCell);
			}
		}

		// Sort neighbours by richess
		neighbours.sort((data1, data2) -> data2.getRichess().compareTo(data1.getRichess()));
	}
}
