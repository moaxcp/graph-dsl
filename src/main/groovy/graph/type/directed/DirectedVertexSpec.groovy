package graph.type.directed

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.VertexSpec
import graph.type.undirected.VertexSpecCodeRunner

/**
 * Provides configuration for a directed graph vertex.
 */
class DirectedVertexSpec extends VertexSpec {
    private final Set<String> connectsFromSet = [] as Set<String>

    /**
     * Creates a new DirectedVertexSpec from map.
     * @param map
     */
    DirectedVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph, map, closure)
        map.connectsFrom?.each {
            connectsFromSet.addAll((String) (it instanceof NameSpec ? it.name : it))
        }
    }

    void applyConnectsFrom() {
        connectsFromSet.each {
            graph.edge it, vertex.name
        }
    }

    void applyClosure() {
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
        applyTraits()
        applyConnectsTo()
        applyConnectsFrom()
        addVertex(vertex)
        applyClosure()
        vertex
    }
}
