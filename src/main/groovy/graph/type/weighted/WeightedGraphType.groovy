package graph.type.weighted

import graph.Edge
import graph.type.undirected.GraphType

/**
 * A weighted graph type where edges are weighted. Graphs are traversed following edges with the lowest weight.
 */
@SuppressWarnings('NoDef')
@SuppressWarnings('EmptyCatchBlock')
class WeightedGraphType extends GraphType {
    @Override
    void convert() {
        super.convert()

        Comparator comparator = { Edge a, Edge b ->
            if (a == b) {
                return 0
            }
            def weightA = 0
            try {
                weightA = a.weight ?: 0
            } catch (MissingPropertyException e) {
                //defaults to 0
            }
            def weightB = 0
            try {
                weightB = b.weight ?: 0
            } catch (MissingPropertyException e) {
                //defaults to 0
            }

            if (weightA < weightB) {
                return -1
            } else if (weightA > weightB) {
                return 1
            }
            int compareOne = a.one <=> b.one
            int compareTwo = a.two <=> b.two
            if (compareOne != 0) {
                return compareOne
            } else if (compareTwo != 0) {
                return compareTwo
            }
            throw new IllegalStateException('a and be are not equals but one and two are not comparable')
        }

        graph.replaceEdgesSet(new TreeSet<>(comparator))
    }
}
