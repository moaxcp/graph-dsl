package graph.type.directed

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.undirected.UndirectedVertexSpec
import graph.type.undirected.VertexSpecCodeRunner
import groovy.transform.PackageScope

/**
 * Provides configuration for a directed graph vertex.
 */
class DirectedVertexSpec extends UndirectedVertexSpec {
    private final Set<String> connectsFromSet = [] as Set<String>

    /**
     * Creates a new DirectedVertexSpec from map.
     * @param map
     */
    @PackageScope
    DirectedVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph, map, closure)
        map.connectsFrom?.each {
            connectsFromSet.addAll((String) (it instanceof NameSpec ? it.name : it))
        }
    }

    protected void applyConnectsFrom() {
        connectsFromSet.each {
            graph.edge it, vertex.name
        }
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new DirectedVertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }
    }

    /**
     * Applies this specification to the graph. If runnerCode is set It will be run with a
     * {@link DirectedVertexSpecCodeRunner}.
     * @param graph
     * @return
     */
    Vertex apply() {
        init()
        checkConditions()
        applyRename()
        applyConnectsTo()
        applyConnectsFrom()
        addVertex(vertex)
        applyClosure()
        vertex
    }
}
