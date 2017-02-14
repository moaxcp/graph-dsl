package graph

class Edge {
    String one
    String two
    def delegate = new Object()

    def delegateAs(Class<?>... traits) {
        delegate = delegate.withTraits(traits)
        this
    }

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

    def methodMissing(String name, def args) {
        delegate.invokeMethod(name, args)
    }

    def propertyMissing(String name) {
        delegate[name]
    }

    def propertyMissing(String name, value) {
        delegate[name] = value
    }
}