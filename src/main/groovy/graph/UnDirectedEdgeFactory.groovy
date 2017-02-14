package graph

class UnDirectedEdgeFactory implements EdgeFactory {
    @Override
    Edge newEdge(String one, String two) {
        return new Edge(one: one, two: two)
    }
}
