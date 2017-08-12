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
    DirectedVertexSpec(Map<String, ?> map) {
        super(map)

        map.connectsFrom?.each {
            connectsFromSet.addAll((String) (it instanceof NameSpec ? it.name : it))
        }
    }

    /**
     * Applies this specification to the graph. If runnerCode is set It will be run with a
     * {@link DirectedVertexSpecCodeRunner}.
     * @param graph
     * @return
     */
    Vertex apply(Graph graph) {
        if (!name) {
            throw new IllegalArgumentException('!name failed. Name must be groovy truth.')
        }
        Vertex vertex = graph.vertices[name] ?: graph.vertexFactory.newVertex(name)
        graph.addVertex(vertex)

        if (rename) {
            graph.rename(name, rename)
        }
        if (traits) {
            vertex.delegateAs(traits as Class[])
        }
        connectsTo.each {
            graph.edge vertex.name, it
        }
        connectsFromSet.each {
            graph.edge it, vertex.name
        }

        if (runnerCode) {
            VertexSpecCodeRunner runner = new DirectedVertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCode)
        }

        vertex
    }
}
