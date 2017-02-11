package graph

class DirectedGraphPlugin implements Plugin {

    def apply(Graph graph) {
        graph.@edges = graph.@edges.inject([] as LinkedHashSet) { edges, edge ->
            edges.add(edge as DirectedEdge)
            edges
        }

        graph.metaClass.newEdge = { one, two ->
            return graph.&newEdge(one, two) as DirectedEdge
        }

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
