package graph

import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * An edge between two vertices. This class uses a delegate when methods
 * and properties are missing. Traits should be applied to the delegate
 * using delegateAs().
 */
@ToString(includeNames=true, excludes='delegate')
class Edge {
    String one
    String two
    Object delegate = new Object()

    @PackageScope
    void setOne(String one) {
        this.one = one
    }

    @PackageScope
    void setTwo(String two) {
        this.two = two
    }

    /**
     * applies trait to the delegate.
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
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        delegate.invokeMethod(name, args)
    }

    /**
     * returns property from delegate.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        delegate[name]
    }

    /**
     * sets property on delegate.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    /**
     * Runs the closure on this {@link Edge}.
     * @param closure
     * @return this {@link Edge}
     */
    Edge leftShift(Closure closure) {
        Closure code = closure.rehydrate(this, this, this)
        code()
        this
    }
}
