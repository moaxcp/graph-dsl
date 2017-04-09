package graph

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode(excludes = ['delegate'])
class Vertex {
    String name
    def delegate = new Object()

    def delegateAs(Class<?>... traits) {
        delegate = delegate.withTraits(traits)
        this
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
     * sets the property on delegate.
     * @param name
     * @param value
     * @return
     */
    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    /**
     * Runs the closure on this {@link Vertex}.
     * @param closure
     * @return this {@link Vertex}
     */
    Vertex leftShift(Closure closure) {
        Closure code = closure.rehydrate(this, this, this)
        code()
        this
    }
}