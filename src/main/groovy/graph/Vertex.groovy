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
    Object delegate = new Object()

    @PackageScope
    void setName(String name) {
        this.name = name
    }

    /**
     * Wraps the delegate in the given traits and assigns delegate to the result.
     * @param traits vararg of traits to wrap the delegate in.
     * @return this
     */
    Vertex delegateAs(Class<?>... traits) {
        delegate = delegate.withTraits(traits)
        this
    }

    /**
     * calls method on delegate.
     * @param name of method
     * @param args for method
     * @return the results returned from the delegate
     */
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        delegate.invokeMethod(name, args)
    }

    /**
     * returns property from delegate.
     * @param name of the property to return
     * @return the property of the delegate
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        delegate[name]
    }

    /**
     * sets the property on delegate.
     * @param name of the property to set
     * @param value to set the property to
     * @return the value
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        delegate[name] = value
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
