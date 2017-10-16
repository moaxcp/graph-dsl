package graph.type.undirected

import graph.Graph
import graph.VertexSpec
import groovy.transform.PackageScope

class UndirectedVertexSpec extends VertexSpec {
    @PackageScope
    UndirectedVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph, map, closure)
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }
    }
}
