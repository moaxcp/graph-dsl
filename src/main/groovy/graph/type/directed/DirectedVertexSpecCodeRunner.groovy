package graph.type.directed

import graph.ConfigSpec
import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.undirected.GraphVertexSpec
import graph.type.undirected.VertexSpecCodeRunner

/**
 * Delegate for {@link GraphVertexSpec#runnerCode}.
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
        GraphVertexSpec spec = graph.newVertexSpec([name:vertex.name, connectsFrom:names])
        spec.setup()
    }

    /**
     * Creates edges where the vertex is edge.two and each name in names is edge.one.
     * @param names of vetices to connect to.
     */
    void connectsFrom(NameSpec... names) {
        GraphVertexSpec spec = graph.newVertexSpec([name:vertex.name, connectsFrom:names*.name])
        spec.setup()
    }

    /**
     * Applies the specs to graph and adds edges using {@link #connectsFrom(String ...)}.
     * @param specs specs to setup to graph and connectFrom.
     */
    void connectsFrom(ConfigSpec... specs) {
        specs.each {
            GraphVertexSpec vspec = graph.newVertexSpec(it)
            vspec.setup()
            vspec.applyClosure()
        }
        connectsFrom(specs*.map.name as String[])
    }
}
