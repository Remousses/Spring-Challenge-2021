package league.bronze;

class Action {
    static final String WAIT = "WAIT";
    static final String SEED = "SEED";
    static final String GROW = "GROW";
    static final String COMPLETE = "COMPLETE";

    private String type;
    private Integer targetCellIdx;
    private Integer sourceCellIdx;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTargetCellIdx() {
		return targetCellIdx;
	}

	public void setTargetCellIdx(Integer targetCellIdx) {
		this.targetCellIdx = targetCellIdx;
	}

	public Integer getSourceCellIdx() {
		return sourceCellIdx;
	}

	public void setSourceCellIdx(Integer sourceCellIdx) {
		this.sourceCellIdx = sourceCellIdx;
	}
}
