package developx.book.parallel.puzzle;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

@Immutable
@RequiredArgsConstructor
public class Node <P, M>{
    final P pos;
    final M move;
    final Node<P, M> prev;

    List<M> asMoveList(){
        LinkedList<M> solution = new LinkedList<>();
        for (Node<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }
        return solution;
    }
}
