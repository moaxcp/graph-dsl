package graph

import groovy.transform.ToString

/**
 * Represents a directed edge in a grpah. Since these should be soted in a
 * Set the equals method is overridden to allow edges in both directions between
 * two vertices.
 */
@SuppressWarnings('EqualsAndHashCode') //equals still meets contract with hashCode (I think)
@ToString(includeSuper=true, includeNames=true)
class DirectedEdge extends Edge {

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
        one == rhs.one && two == rhs.two
    }
}
