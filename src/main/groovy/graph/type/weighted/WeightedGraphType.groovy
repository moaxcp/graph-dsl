package graph.type.weighted

import graph.Edge
import graph.type.undirected.GraphType

/**
 * A weighted graph type where edges are weighted. Graphs are traversed following edges with the lowest weight.
 */
class WeightedGraphType extends GraphType {
    @Override
    void convert() {
        super.convert()

        graph.replaceEdgesSet(new TreeSet<>(new WeightedEdgeComparator()))
    }

    @Override
    boolean isWeighted() {
        true
    }
}
