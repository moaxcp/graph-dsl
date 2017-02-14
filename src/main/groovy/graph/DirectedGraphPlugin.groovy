package graph

class DirectedGraphPlugin implements Plugin {

    def apply(Graph graph) {
        graph.@edges = graph.@edges.collect { edge ->
            new DirectedEdge(one: edge.one, two: edge.two)
        } as LinkedHashSet

        graph.edgeFactory = new DirectedEdgeFactory()

        graph.metaClass.outEdges = { name ->
            graph.@edges.findAll {
                name == it.one
            }
        }

        graph.metaClass.adjacentEdges = { name ->
            outEdges name
        }
    }
}
