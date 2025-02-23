// Interface untuk visualisasi proses solving puzzle secara real-time

package solver;

import model.Board;

public interface SolverVisualizer {
    void updateVisualization(Board board);
}