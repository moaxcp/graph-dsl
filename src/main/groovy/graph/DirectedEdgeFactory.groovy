package graph

class DirectedEdgeFactory implements EdgeFactory {
    @Override
    Edge newEdge(String one, String two) {
        return new DirectedEdge(one: one, two: two)
    }
}
