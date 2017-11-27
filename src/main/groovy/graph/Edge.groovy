package graph


/**
 * An edge between two vertices. Properties may be added to Edge by setting values. Assigning a property to a
 * {@link Closure} will make it lazy. When the property is read the value returned is the result of calling the closure.
 * If a missing method is called on Edge and a property is set with a closure the closure will be called and the
 * resulting value returned.
 */
class Edge {
    @Delegate
    Map map = [:]

    Object getOne() {
        get('one')
    }

    Object setOne(Object value) {
        put('one', value)
    }

    Object getTwo() {
        get('two')
    }

    Object setTwo(Object value) {
        put('two', value)
    }

    boolean asBoolean() {
        one && two
    }

    boolean equals(Edge edge) {
        (one == edge.one || one == edge.two) && (two == edge.two || two == edge.one)
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
        equals(rhs)
    }

    /**
     * returns one.hashCode() + two.hashCode()
     * @return
     */
    @Override
    int hashCode() {
        one.hashCode() + two.hashCode()
    }
}
