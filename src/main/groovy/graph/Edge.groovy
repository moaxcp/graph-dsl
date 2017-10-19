package graph

import graph.internal.PropertyDelegator
import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * An edge between two vertices. Properties may be added to Edge by setting values. Assigning a property to a
 * {@link Closure} will make it lazy. When the property is read the value returned is the result of calling the closure.
 * If a missing method is called on Edge and a property is set with a closure the closure will be called and the resulting
 * value returned.
 */
@ToString(includeNames=true)
class Edge extends PropertyDelegator {
    String one
    String two

    @PackageScope
    void setOne(String one) {
        this.one = one
    }

    @PackageScope
    void setTwo(String two) {
        this.two = two
    }

    /**
     * Returns the property with the given name.
     * @param name
     * @return
     */
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
}
