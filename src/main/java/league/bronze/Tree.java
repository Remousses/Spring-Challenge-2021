package league.bronze;

class Tree {
    private Integer cellIndex;
    private int size;
    private boolean isMine;
    private boolean isDormant;

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
    
    public Integer getCellIndex() {
		return cellIndex;
	}

	public void setCellIndex(Integer cellIndex) {
		this.cellIndex = cellIndex;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isDormant() {
		return isDormant;
	}

	public void setDormant(boolean isDormant) {
		this.isDormant = isDormant;
	}
}
