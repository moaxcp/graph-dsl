package graph.type.undirected

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.EdgeSpec
import graph.Vertex
import graph.VertexSpec
import graph.type.Type

/**
 * Implements an undirected graph. There can only be one {@link Edge} between any two vertices. When traversing a graph
 * an {@link Edge} is adjacent to a {@link Vertex} if it's one or two property equals the name of the {@link Vertex}.
 */
class GraphType implements Type {

    Graph graph

    @Override
    Edge newEdge(String one, String two, Object delegate = null) {
        if (delegate) {
            new Edge(one:one, two:two, delegate:delegate)
        } else {
            new Edge(one:one, two:two)
        }
    }

    @Override
    Vertex newVertex(String name, Object delegate = null) {
        if (delegate) {
            new Vertex(key:name, delegate:delegate)
        } else {
            new Vertex(key:name)
        }
    }

    @Override
    EdgeSpec newEdgeSpec(Map<String, ?> map) {
        new UndirectedEdgeSpec(graph, map)
    }

    @Override
    EdgeSpec newEdgeSpec(ConfigSpec spec) {
        new UndirectedEdgeSpec(graph, spec.map, spec.closure)
    }

    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new UndirectedVertexSpec(graph, map)
    }

    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        new UndirectedVertexSpec(graph, spec.map, spec.closure)
    }

    @Override
    boolean canConvert() {
        if (graph.edges.size() == 0) {
            return true
        }
        Set<Edge> edges = [] as Set
        !graph.edges.find { Edge current ->
            Edge edge = new Edge(one:current.one, two:current.two)
            !edges.add(edge)
        }
    }

    @Override
    void convert() {
        if (!canConvert()) {
            throw new IllegalArgumentException("Cannot convert to ${getClass().simpleName}")
        }
        graph.replaceEdges { Edge edge ->
            newEdge(edge.one, edge.two, edge.delegate)
        }

        graph.replaceEdgesSet(new LinkedHashSet<? extends Edge>())

        graph.replaceVertices { String name, Vertex vertex ->
            [vertex.key, newVertex(vertex.key, vertex.delegate)]
        }

        graph.replaceVerticesMap([:])
    }

    /**
     * Returns edges from vertex with name that should be traversed.
     * @param name
     * @return
     */
    Set<? extends Edge> traverseEdges(String name) {
        graph.adjacentEdges(name)
    }
}
