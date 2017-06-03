package graph

/**
 * Applies the {@link Mapping} trait to all vertices. All future vertices will also apply the {@link Mapping} trait.
 */
class VertexMapPlugin implements Plugin {

    /**
     * Applies the {@link Mapping} trait to all vertices in the graph. Creates a new {@link VertexFactory} that will add
     * the {@link Mapping} trait to any new vertices created.
     * @param graph this plugin is applied to.
     */
    @Override
    void apply(Graph graph) {
        graph.vertexTraits(Mapping)
    }
}
