package graph.type.weighted

import graph.Edge

@SuppressWarnings('NoDef')
@SuppressWarnings('EmptyCatchBlock')
class WeightedEdgeComparator implements Comparator<Edge> {

    @Override
    int compare(Edge a, Edge b) {
        if (a == b) {
            return 0
        }
        def weightA = a.weight ?: 0
        def weightB = b.weight ?: 0

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
}
