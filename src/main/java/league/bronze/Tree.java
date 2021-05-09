package league.bronze;

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
