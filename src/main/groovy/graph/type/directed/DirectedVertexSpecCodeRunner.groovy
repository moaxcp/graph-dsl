package graph.type.directed

import graph.ConfigSpec
import graph.Graph
import graph.NameSpec
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

    /**
     * Creates edges where the vertex is edge.two and each name in names is edge.one.
     * @param names of vetices to connect to.
     */
    void connectsFrom(NameSpec... names) {
        graph.newVertexSpec([key:vertex.key, connectsFrom:names*.name]).apply()
    }

    /**
     * Applies the specs to graph and adds edges using {@link #connectsFrom(String ...)}.
     * @param specs specs to apply to graph and connectFrom.
     */
    void connectsFrom(ConfigSpec... specs) {
        specs.each {
            graph.newVertexSpec(it.map, it.closure).apply()
        }
        connectsFrom(specs*.map.key as Object[])
    }
}
