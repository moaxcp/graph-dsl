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
     * Creates edges where the vertex is edge.two and each id in ids is edge.one.
     * @param ids of vetices to connect to.
     */
    void connectsFrom(Object... ids) {
        graph.newVertexSpec([id:vertex.id, connectsFrom:ids]).apply()
    }

    void connectsFrom(Object id, Closure closure) {
        graph.newVertexSpec([id:vertex.id, connectsFrom:id]).apply()
        graph.newVertexSpec([id:id], closure).apply()
    }
}
