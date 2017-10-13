package graph

import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * An edge between two vertices. Edge is also a Map.
 */
@ToString(includeNames=true, excludes='delegate')
class Edge {
    String one
    String two
    Map delegate = [:]

    @PackageScope
    void setOne(String one) {
        this.one = one
    }

    @PackageScope
    void setTwo(String two) {
        this.two = two
    }

    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    def propertyMissing(String name) {
        if(delegate[name] instanceof Closure) {
            return ((Closure)delegate[name]).call()
        }
        if(delegate[name]) {
            return delegate[name]
        } else {
            throw new MissingPropertyException("could not find property $name")
        }
    }

    def methodMissing(String name, args) {
        if(delegate[name] instanceof Closure) {
            return ((Closure)delegate[name]).call(args)
        }
        throw new MissingMethodException(name, this.class, args)
    }

    Object getAt(String name) {
        if(name == 'one') {
            return one
        }
        if(name == 'two') {
            return two
        }
        delegate[name]
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
