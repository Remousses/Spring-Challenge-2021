import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

class Action {
    static final String WAIT = "WAIT";
    static final String SEED = "SEED";
    static final String GROW = "GROW";
    static final String COMPLETE = "COMPLETE";

    static Action parse(String action) {
        String[] parts = action.split(" ");
        switch (parts[0]) {
        case WAIT:
            return new Action(WAIT);
        case SEED:
            return new Action(SEED, Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
        case GROW:
        case COMPLETE:
        default:
            return new Action(parts[0], Integer.valueOf(parts[1]));
        }
    }

    String type;
    Integer targetCellIdx;
    Integer sourceCellIdx;

    public Action(String type, Integer sourceCellIdx, Integer targetCellIdx) {
        this.type = type;
        this.targetCellIdx = targetCellIdx;
        this.sourceCellIdx = sourceCellIdx;
    }

    public Action(String type, Integer targetCellIdx) {
        this(type, null, targetCellIdx);
    }

    public Action(String type) {
        this(type, null, null);
    }
    
    @Override
    public String toString() {
        if (type == WAIT) {
            return Action.WAIT;
        }
        if (type == SEED) {
            return String.format("%s %d %d", SEED, sourceCellIdx, targetCellIdx);
        }
        return String.format("%s %d", type, targetCellIdx);
    }
}

class Cell {
    int index;
    int richess;
    int[] neighbours;

    public Cell(int index, int richess, int[] neighbours) {
        this.index = index;
        this.richess = richess;
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return new StringBuilder("Cell { ").append("index : ").append(this.index)
            .append(", richess : ").append(this.richess)
            .append(", neighbours : ").append(Arrays.stream(this.neighbours).mapToObj(String::valueOf).collect(Collectors.joining("/")))
            .append(" }").toString();
    }
}

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

class Player {
    int oldTreeNumber = 0;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Game game = new Game();
        System.out.println("yolo");

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
            game.board.add(cell);
        }

        game.board.forEach(System.err::println);

        while (true) {
            game.day = in.nextInt();
            game.nutrients = in.nextInt();
            game.mySun = in.nextInt();
            game.myScore = in.nextInt();
            game.opponentSun = in.nextInt();
            game.opponentScore = in.nextInt();
            game.opponentIsWaiting = in.nextInt() != 0;

            game.trees.clear();
            int numberOfTrees = in.nextInt();
            for (int i = 0; i < numberOfTrees; i++) {
                int cellIndex = in.nextInt();
                int size = in.nextInt();
                boolean isMine = in.nextInt() != 0;
                boolean isDormant = in.nextInt() != 0;
                Tree tree = new Tree(cellIndex, size, isMine, isDormant);
                game.trees.add(tree);
            }

            game.trees.forEach(System.err::println);

            game.possibleActions.clear();
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
}
class Tree {
    int cellIndex;
    int size;
    boolean isMine;
    boolean isDormant;

    public Tree(int cellIndex, int size, boolean isMine, boolean isDormant) {
        this.cellIndex = cellIndex;
        this.size = size;
        this.isMine = isMine;
        this.isDormant = isDormant;
    }

    @Override
    public String toString() {
        return new StringBuilder("Tree { ").append("cellIndex : ").append(this.cellIndex)
            .append(", size : ").append(this.size)
            .append(", isMine : ").append(this.isMine)
            .append(", isDormant : ").append(this.isDormant).append(" }").toString();
    }
}
