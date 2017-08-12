package graph.type.directed

import graph.ConfigSpec
import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.VertexSpec
import graph.type.undirected.VertexSpecCodeRunner

/**
 * Delegate for {@link VertexSpec#runnerCode}.
 */
class DirectedVertexSpecCodeRunner extends VertexSpecCodeRunner {

    DirectedVertexSpecCodeRunner(Graph graph, Vertex vertex) {
        super(graph, vertex)
    }

    /**
     * Creates edges where the vertex is edge.two and each name in names is edge.one.
     * @param names of vetices to connect to.
     */
    void connectsFrom(String... names) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, connectsFrom:names)
        spec.apply(graph)
    }

    /**
     * Creates edges where the vertex is edge.two and each name in names is edge.one.
     * @param names of vetices to connect to.
     */
    void connectsFrom(NameSpec... names) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, connectsFrom:names*.name)
        spec.apply(graph)
    }

    /**
     * Applies the specs to graph and adds edges using {@link #connectsFrom(String ...)}.
     * @param specs specs to apply to graph and connectFrom.
     */
    void connectsFrom(ConfigSpec... specs) {
        specs.each {
            graph.vertexSpecFactory.newVertexSpec(it).apply(graph)
        }
        connectsFrom(specs*.map.name as String[])
    }
}
