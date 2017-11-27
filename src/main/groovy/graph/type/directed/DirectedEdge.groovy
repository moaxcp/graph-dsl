package graph.type.directed

import graph.Edge
import groovy.transform.ToString

/**
 * Represents a directed edge in a graph. Since these should be sorted in a
 * Set the equals method is overridden to allow edges in both directions between
 * two vertices.
 */
@SuppressWarnings('EqualsAndHashCode') //equals still meets contract with hashCode (I think)
class DirectedEdge extends Edge {

    boolean equals(DirectedEdge edge) {
        one == edge.one && two == edge.two
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
