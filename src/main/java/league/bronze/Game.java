package league.bronze;

import java.util.ArrayList;
import java.util.List;

class Game {
    int day;
    int nutrients;
    List<Cell> board;
    List<Action> possibleActions;
    List<Tree> trees;
    int mySun, opponentSun;
    int myScore, opponentScore;
    boolean opponentIsWaiting;

    public Game() {
        board = new ArrayList<>();
        possibleActions = new ArrayList<>();
        trees = new ArrayList<>();
    }

    Action getNextAction() {
        // TODO: write your algorithm here

        List<Action> treesToComplete = new ArrayList<>();

        this.possibleActions.forEach(action -> {
            switch (action.type) {
                case Action.WAIT:
                    break;
                case Action.SEED:
                    break;
                case Action.GROW:
                    break;
                case Action.COMPLETE:
                    treesToComplete.add(Action.parse(action.toString()));
                    break;
                default:
                    break;
            }
        });
        this.possibleActions.forEach(System.err::println);
        
        if (treesToComplete.isEmpty()) {
        	return Action.parse(Action.WAIT);
        }

        treesToComplete.sort((data1, data2) -> data1.targetCellIdx.compareTo(data2.targetCellIdx));

        System.err.println("TREEEE");
        treesToComplete.forEach(System.err::println);
        return treesToComplete.stream().filter(action -> 
        	this.trees.stream().filter(tree -> tree.cellIndex == action.targetCellIdx && tree.size == 3).findFirst().isPresent()
        ).findFirst().get();
    }

}
