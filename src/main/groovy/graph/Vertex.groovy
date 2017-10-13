package graph

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * A vertex in the graph. Every vertex should have a name. All vertices have a delegate which allows methods to be added
 * dynamically.
 */
@ToString(includeNames=true, excludes='delegate')
@EqualsAndHashCode(excludes = ['delegate'])
class Vertex {
    String name
    Map delegate = [:]

    @PackageScope
    void setName(String name) {
        this.name = name
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

    /**
     * Runs the closure on this {@link Vertex}.
     * @param closure to run
     * @return this {@link Vertex}
     */
    Vertex leftShift(Closure closure) {
        Closure code = closure.rehydrate(this, this, this)
        code()
        this
    }
}
