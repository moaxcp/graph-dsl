package graph

trait DirectedEdge {

    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false
        }
        if (this.is(o)) {
            return true
        }
        Edge rhs = (Edge) o;
        return one == rhs.one && two == rhs.two
    }
}
