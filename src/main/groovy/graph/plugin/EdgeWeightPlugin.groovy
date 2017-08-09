package graph.plugin

import graph.Graph
import graph.Weight

/**
 * Plugin to add {@link Weight} support to the edges of a {@link Graph}.
 */
class EdgeWeightPlugin implements Plugin {

    /**
     * {@link graph.Weight} is added to graph as an edgeTrait. The internal {@link Set} of edges is replaced with a
     * {@link TreeSet} ordered by weight. This results in all traversal methods following edges by weight rather than
     * insertion order.
     * @param graph to apply plugin
     */
    @Override
    void apply(Graph graph) {
        graph.edgeTraits(Weight)

        Comparator comparator = { a, b ->
            a.weight <=> b.weight
        }

        graph.replaceEdgesSet(new TreeSet<>(comparator))
    }
}
