package graph.type.directed

import graph.Edge

/**
 * Represents a directed edge in a graph. Since these should be sorted in a
 * Set the equals method is overridden to allow edges in both directions between
 * two vertices.
 */
@SuppressWarnings('EqualsAndHashCode') //equals still meets contract with hashCode (I think)
class DirectedEdge extends Edge {

    boolean equals(DirectedEdge edge) {
        from == edge.from && to == edge.to
    }

    /**
     * overridden to allow edges in both directions between two vertices.
     * @param o
     * @return true if two edges are equal.
     */
    @SuppressWarnings('Instanceof')
    boolean equals(Object o) {
        if (!(o instanceof DirectedEdge)) {
            return false
        }
        if (this.is(o)) {
            return true
        }
        DirectedEdge rhs = (DirectedEdge) o
        equals(rhs)
    }
}
