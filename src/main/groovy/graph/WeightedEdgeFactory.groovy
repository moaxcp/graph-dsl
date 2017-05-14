package graph

class WeightedEdgeFactory implements EdgeFactory {

    @Override
    Edge newEdge(String one, String two) {
        Edge edge = new Edge(one:one, two:two)
        edge.delegateAs(Weight)
        edge
    }
}
