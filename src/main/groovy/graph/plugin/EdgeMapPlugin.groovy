package graph.plugin

import graph.Graph
import graph.trait.Mapping

/**
 * Applies the {@link Mapping} trait to all edges. All future edges will also setup the {@link Mapping} trait.
 */
class EdgeMapPlugin implements Plugin {

    /**
     * applies the {@link Mapping} trait to all edges in the graph. Creates a new {@link graph.EdgeFactory} that will
     * add the {@link Mapping} trait to any new edges created.
     * @param graph this plugin is applied to.
     */
    @Override
    void apply(Graph graph) {
        graph.edgeTraits(Mapping)
    }
}
