package graph.type.weighted

import graph.Edge

@SuppressWarnings('NoDef')
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
        int compareFrom = a.from <=> b.from
        int compareTo = a.to <=> b.to
        if (compareFrom != 0) {
            return compareFrom
        } else if (compareTo != 0) {
            return compareTo
        }
        throw new IllegalStateException('a and be are not equals but from and to are not comparable')
    }
}
