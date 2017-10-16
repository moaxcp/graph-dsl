package graph.type.undirected

import graph.EdgeSpec
import graph.Graph
import groovy.transform.PackageScope

class UndirectedEdgeSpec extends EdgeSpec {
    @PackageScope
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
