package graph.type.weighted

import graph.Edge
import graph.type.undirected.GraphType

class WeightedGraphType extends GraphType {
    @Override
    void convert() {
        super.convert()

        Comparator comparator = { Edge a, Edge b ->
            if(a == b) {
                return 0
            }
            def weightA = 0
            try {
                weightA = a.weight ?: 0
            } catch(MissingPropertyException e) {

            }
            def weightB = 0
            try {
                weightB = b.weight ?: 0
            } catch(MissingPropertyException e) {

            }

            if(weightA.compareTo(weightB) < 0) {
                return -1
            } else if(weightA.compareTo(weightB) > 0) {
                return 1
            } else {
                int compareOne = a.one.compareTo(b.one)
                int compareTwo = a.two.compareTo(b.two)
                if(compareOne != 0) {
                    return compareOne
                } else if(compareTwo != 0) {
                    return compareTwo
                } else {
                    IllegalStateException('a and be are not equals but one and two are not comparable')
                }
            }
        }

        graph.replaceEdgesSet(new TreeSet<>(comparator))
    }
}
