package graph.type.undirected

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
    Edge newEdge(Map<String, ?> map) {
        new Edge(map)
    }

    @Override
    Vertex newVertex(Map<String, ?> map) {
        new Vertex(map)
    }

    @Override
    EdgeSpec newEdgeSpec(Map<String, ?> map, Closure closure = null) {
        new UndirectedEdgeSpec(graph, map, closure)
    }

    @Override
    VertexSpec newVertexSpec(Map<String, ?> map, Closure closure = null) {
        new UndirectedVertexSpec(graph, map, closure)
    }

    @Override
    boolean canConvert() {
        if (graph.edges.size() == 0) {
            return true
        }
        Set<Edge> edges = [] as Set
        !graph.edges.find { Edge current ->
            Edge edge = new Edge(current)
            !edges.add(edge)
        }
    }

    @Override
    void convert() {
        if (!canConvert()) {
            throw new IllegalArgumentException("Cannot convert to ${getClass().simpleName}")
        }
        graph.replaceEdges { Edge edge ->
            newEdge(edge)
        }

        graph.replaceEdgesSet(new LinkedHashSet<? extends Edge>())

        graph.replaceVertices { String name, Vertex vertex ->
            [vertex.id, newVertex(vertex)]
        }

        graph.replaceVerticesMap([:])
    }

    @Override
    boolean isMultiGraph() {
        false
    }

    @Override
    boolean isDirected() {
        false
    }

    @Override
    boolean isWeighted() {
        false
    }

    /**
     * Returns edges from vertex that should be traversed.
     * @param id
     * @return
     */
    Set<? extends Edge> traverseEdges(Object id) {
        graph.adjacentEdges(id)
    }
}
