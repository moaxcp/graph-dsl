package graph.type.undirected

import graph.Graph
import graph.type.AbstractEdgeSpec

/**
 * Implements an EdgeSpec for an undirected graph.
 */
class UndirectedEdgeSpec extends AbstractEdgeSpec {

    UndirectedEdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph, map, closure)
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            EdgeSpecCodeRunner runner = new EdgeSpecCodeRunner(graph, edge)
            runner.runCode(runnerCodeClosure)
        }
    }
}
