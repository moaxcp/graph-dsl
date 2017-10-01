package graph.plugin

import graph.Graph
import graph.trait.Mapping

/**
 * Applies the {@link Mapping} trait to all vertices. All future vertices will also setup the {@link Mapping} trait.
 */
class VertexMapPlugin implements Plugin {

    /**
     * Applies the {@link Mapping} trait to all vertices in the graph. Creates a new {@link graph.VertexFactory} that
     * will add the {@link Mapping} trait to any new vertices created.
     * @param graph this plugin is applied to.
     */
    @Override
    void apply(Graph graph) {
        graph.vertexTraits(Mapping)
    }
}
