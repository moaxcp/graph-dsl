package graph

import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * An edge between two vertices. Properties may be added to Edge by setting values. Assigning a property to a
 * {@link Closure} will make it lazy. When the property is read the value returned is the result of calling the closure.
 * If a missing method is called on Edge and a property is set with a closure the closure will be called and the resulting
 * value returned.
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

    /**
     * Adds a property to this Edge.
     * @param name  of property
     * @param value  of property
     * @return
     */
    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    /**
     * Returns the value of a property in this Edge. If the property is a {@link Closure} the result of calling the
     * closure is returned. If the property does not exist {@link MissingPropertyException} is thrown.
     * @param name  of property to return
     * @return value of the property
     */
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

    /**
     * If a property was set to a {@link Closure} and it is called on this Edge this method will call the closure
     * returning the results. The closure may be called with arguments.
     * @param name  of closure to call
     * @param args  to call closure with
     * @return
     */
    def methodMissing(String name, args) {
        if(delegate[name] instanceof Closure) {
            return ((Closure)delegate[name]).call(args)
        }
        throw new MissingMethodException(name, this.class, args)
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
