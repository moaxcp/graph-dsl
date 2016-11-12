package graph

class Edge {
    String one
    String two

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false
        }
        if (this.is(o)) {
            return true
        }
        Edge rhs = (Edge) o;
        return (one == rhs.one || one == rhs.two) && (two == rhs.two || two == rhs.one)
    }

    @Override
    public int hashCode() {
        one.hashCode() + two.hashCode()
    }
}