package developx.book.parallel.synchronizeds.barrier;

public record Board() {
    public void commitNewValues() {

    }

    public Board getSubBoard(int count, int i) {
        return null;
    }

    public boolean hasConverged() {
        return false;
    }

    public int getMaxX() {
        return 0;
    }

    public int getMaxY() {
        return 0;
    }

    public void setNewValue(int x, int y, Object o) {
    }

    public void waitForConvergence() {

    }
}
