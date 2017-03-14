package graph

/**
 * An edge between two vertices. This class uses a delegate when methods
 * and properties are missing. Traits should be applied to the delegate
 * using delegateAs().
 */
@SuppressWarnings('NoDef')
class Edge {
    String one
    String two
    def delegate = new Object()

    /**
     * applies traits to the delegate.
     * @param traits
     * @return
     */
    Edge delegateAs(Class<?>... traits) {
        delegate = delegate.withTraits(traits)
        this
    }

    /**
     * two edges are equal when they connect to the same vertices
     * regardless of ordering of one and two.
     * @param o
     * @return
     */
    @Override
    @SuppressWarnings('Instanceof')
    boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false
        }
        if (this.is(o)) {
            return true
        }
        Edge rhs = (Edge) o
        (one == rhs.one || one == rhs.two) && (two == rhs.two || two == rhs.one)
    }

    /**
     * returns one.hashCode() + two.hashCode()
     * @return
     */
    @Override
    int hashCode() {
        one.hashCode() + two.hashCode()
    }

    /**
     * calls method on delegate.
     * @param name
     * @param args
     * @return
     */
    def methodMissing(String name, args) {
        delegate.invokeMethod(name, args)
    }

    /**
     * returns property from delegate.
     * @param name
     * @return
     */
    def propertyMissing(String name) {
        delegate[name]
    }

    /**
     * sets property on delegate.
     * @param name
     * @param value
     * @return
     */
    def propertyMissing(String name, value) {
        delegate[name] = value
    }
}
