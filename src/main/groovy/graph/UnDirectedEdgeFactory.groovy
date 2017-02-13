package graph

/**
 * Created by john on 2/12/17.
 */
class UnDirectedEdgeFactory implements EdgeFactory {
    @Override
    Edge newEdge(String one, String two) {
        return new Edge(one: one, two: two)
    }
}
