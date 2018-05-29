package graph.type.directed

import graph.ConfigSpec
import graph.Graph
import graph.Vertex
import graph.type.undirected.VertexSpecCodeRunner

/**
 * Delegate for {@link GraphVertexSpec#runnerCode}.
 */
class DirectedVertexSpecCodeRunner extends VertexSpecCodeRunner {

    DirectedVertexSpecCodeRunner(Graph graph, Vertex vertex) {
        super(graph, vertex)
    }

    /**
     * Creates edges where the vertex is edge.two and each key in keys is edge.one.
     * @param keys of vetices to connect to.
     */
    void connectsFrom(Object... keys) {
        graph.newVertexSpec([key:vertex.key, connectsFrom:keys]).apply()
    }

    void connectsFrom(Object key, Closure closure) {
        graph.newVertexSpec([key:vertex.key, connectsFrom:key]).apply()
        graph.newVertexSpec([key:key], closure).apply()
    }
}
