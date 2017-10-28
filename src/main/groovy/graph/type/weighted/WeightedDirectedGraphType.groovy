package graph.type.weighted

import graph.type.directed.DirectedGraphType

class WeightedDirectedGraphType extends DirectedGraphType {
    @Override
    void convert() {
        super.convert()

        graph.replaceEdgesSet(new TreeSet<>(new WeightedEdgeComparator()))
    }
}
