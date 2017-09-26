package graph.type.undirected

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.Vertex
import graph.type.EdgeSpec
import graph.type.Type
import graph.type.VertexSpec

class UndirectedGraphType implements Type {
    @Override
    Edge newEdge(String one, String two) {
        new Edge(one:one, two:two)
    }

    @Override
    Vertex newVertex(String name) {
        new Vertex(name:name)
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
    void convert(Graph graph) {
        graph.replaceEdges { edge ->
            new Edge(one:edge.one, two:edge.two, delegate:edge.delegate)
        }
    }
}
