package league.bronze;

import java.util.Arrays;
import java.util.stream.Collectors;

class Cell {
    private int index;
    private Integer richess;
    private int[] neighbours;
    private boolean isBusy;

    public Cell(int index, int richess, int[] neighbours) {
        this.index = index;
        this.richess = richess;
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return new StringBuilder("Cell { ").append("index : ").append(this.index)
                .append(", richess : ").append(this.richess)
                .append(", isBusy : ").append(this.isBusy)
            .append(", neighbours : ").append(Arrays.stream(this.neighbours).mapToObj(String::valueOf).collect(Collectors.joining("/")))
            .append(" }").toString();
    }

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Integer getRichess() {
		return richess;
	}

	public void setRichess(Integer richess) {
		this.richess = richess;
	}

	public int[] getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(int[] neighbours) {
		this.neighbours = neighbours;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
}
