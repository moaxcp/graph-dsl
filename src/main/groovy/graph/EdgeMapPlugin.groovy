package graph

class EdgeMapPlugin implements Plugin {
    @Override
    def apply(Graph graph) {
        graph.@edges.each { edge ->
            edge.delegateAs(Mapping)
        }

        graph.edgeFactory = new EdgeFactory() {
            def oldFactory = graph.edgeFactory
            @Override
            Edge newEdge(String one, String two) {
                def edge = oldFactory.newEdge(one, two)
                edge.delegateAs(Mapping)
                return edge
            }
        }
    }
}
