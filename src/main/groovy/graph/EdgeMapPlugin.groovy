package graph

class EdgeMapPlugin implements Plugin {
    @Override
    def apply(Graph graph) {
        graph.@edges = graph.@edges.inject([] as LinkedHashSet) { edges, edge ->
            edges.add(edge.withTraits(LinkedHashMap))
            edges
        }

        graph.metaClass.newEdge = { one, two ->
            return graph.&newEdge(one, two).withTraits(LinkedHashMap)
        }
    }
}
