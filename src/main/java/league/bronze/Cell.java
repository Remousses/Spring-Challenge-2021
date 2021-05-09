package league.bronze;

import java.util.Arrays;
import java.util.stream.Collectors;

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
