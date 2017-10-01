package graph.type.undirected

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.Vertex
import graph.type.EdgeSpec
import graph.type.Type
import graph.type.VertexSpec

/**
 * Implements an undirected graph. There can only be one {@link Edge} between any two vertices. When traversing a graph
 * an {@link Edge} is adjacent to a {@link Vertex} if it's one or two property equals the name of the {@link Vertex}.
 */
class GraphType implements Type {
    @Override
    Edge newEdge(String one, String two, Object delegate = null) {
        if(delegate) {
            new Edge(one:one, two:two, delegate:delegate)
        } else {
            new Edge(one: one, two: two)
        }
    }

    @Override
    Vertex newVertex(String name, Object delegate = null) {
        if(delegate) {
            new Vertex(name:name, delegate:delegate)
        } else {
            new Vertex(name: name)
        }
    }

    @Override
    EdgeSpec newEdgeSpec(Graph graph, Map<String, ?> map) {
        new EdgeSpec(graph, map)
    }

    @Override
    EdgeSpec newEdgeSpec(Graph graph, ConfigSpec spec) {
        new EdgeSpec(graph, spec.map, spec.closure)
    }

    @Override
    VertexSpec newVertexSpec(Graph graph, Map<String, ?> map) {
        new VertexSpec(graph, map)
    }

    @Override
    VertexSpec newVertexSpec(Graph graph, ConfigSpec spec) {
        new VertexSpec(graph, spec.map, spec.closure)
    }

    @Override
    boolean canConvert(Graph graph) {
        if(graph.edges.size() == 0) {
            return true
        }
        Set<Edge> edges = new HashSet<>()
        return !graph.edges.find { Edge current ->
            Edge edge = new Edge(one:current.one, two:current.two)
            return !edges.add(edge)
        }
    }

    @Override
    void convert(Graph graph) {
        if(!canConvert(graph)) {
            throw new IllegalArgumentException("Cannot convert to ${getClass().getSimpleName()}")
        }
        graph.replaceEdges { Edge edge ->
            newEdge(edge.one, edge.two, edge.delegate)
        }

        graph.replaceEdgesSet(new LinkedHashSet<? extends Edge>())

        graph.replaceVertices { String name, Vertex vertex ->
            [vertex.name, newVertex(vertex.name, vertex.delegate)]
        }

        graph.replaceVerticesMap(new LinkedHashMap<String, ? extends Vertex>())
    }
}
