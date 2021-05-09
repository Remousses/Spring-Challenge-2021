package league.bronze;

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
