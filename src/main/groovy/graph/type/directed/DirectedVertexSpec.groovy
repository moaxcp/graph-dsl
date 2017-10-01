package graph.type.directed

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.VertexSpec
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
    DirectedVertexSpec(Graph graph, Map<String, ?> map) {
        super(graph, map)
        init(map)
    }

    private void init(Map<String, ?> map) {
        map.connectsFrom?.each {
            connectsFromSet.addAll((String) (it instanceof NameSpec ? it.name : it))
        }
    }

    DirectedVertexSpec(Graph graph, Map<String, ?> map, Closure closure) {
        super(graph, map, closure)
        init(map)
    }

    void applyClosure() {
        if (closure) {
            VertexSpecCodeRunner runner = new DirectedVertexSpecCodeRunner(graph, vertex)
            runner.runCode(closure)
        }
    }

    /**
     * Applies this specification to the graph. If runnerCode is set It will be run with a
     * {@link DirectedVertexSpecCodeRunner}.
     * @param graph
     * @return
     */
    Vertex setup() {

        applyVertex()
        applyRename()
        applyTraits()
        applyConnectsTo()
        connectsFromSet.each {
            graph.edge it, vertex.name
        }

        applyClosure()
        vertex
    }
}
