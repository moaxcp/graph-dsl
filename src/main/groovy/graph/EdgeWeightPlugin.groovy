package graph

class EdgeWeightPlugin implements Plugin {

    @Override
    void apply(Graph graph) {
        graph.edges.each { edge ->
            edge.delegateAs(Weight)
        }

        graph.edgeFactory = new WeightedEdgeFactory()

        graph.metaClass.traverseEdges = this.&traverseEdges.curry(graph.&traverseEdges)
    }

    static Set<? extends Edge> traverseEdges(Closure originalTraverseEdges, String name) {
        Set<Edge> edges = originalTraverseEdges(name)
        TreeSet newEdges = new TreeSet(new OrderBy({ it.weight }))
        newEdges.addAll(edges)
        newEdges
    }
}
