package graph

trait DirectedEdge {

    public boolean equals(Object o) {
        if (!(o instanceof DirectedEdge)) {
            return false
        }
        if (this.is(o)) {
            return true
        }
        DirectedEdge rhs = (DirectedEdge) o;
        return one == rhs.one && two == rhs.two
    }
}
