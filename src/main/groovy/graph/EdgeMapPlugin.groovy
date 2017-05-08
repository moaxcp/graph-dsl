package graph

/**
 * Applies the {@link Mapping} trait to all edges. All future edges will also apply the {@link Mapping} trait.
 */
class EdgeMapPlugin implements Plugin {

    /**
     * applies the {@link Mapping} trait to all edges in the graph. Creates a new {@link EdgeFactory} that will add
     * the {@link Mapping} trait to any new edges created.
     * @param graph this plugin is applied to.
     */
    @Override
    void apply(Graph graph) {
        graph.@edges.each { edge ->
            edge.delegateAs(Mapping)
        }

        graph.edgeFactory = new EdgeFactory() {
            EdgeFactory oldFactory = graph.edgeFactory
            @Override
            Edge newEdge(String one, String two) {
                Edge edge = oldFactory.newEdge(one, two)
                edge.delegateAs(Mapping)
            }
        }
    }
}
