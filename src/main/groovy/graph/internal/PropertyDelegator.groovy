package graph.internal

/**
 * Delegates missing properties to a map.
 */
class PropertyDelegator {
    Map delegate = [:]

    /**
     * Sets the property name to value.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    /**
     * Returns the missing property if set. If the property is set to a {@link Closure} the value returned
     * by calling the closure will be returned.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        if (delegate[name] instanceof Closure) {
            return ((Closure)delegate[name]).call()
        }
        if (delegate.containsKey(name)) {
            return delegate[name]
        }
        throw new MissingPropertyException("could not find property $name")
    }
}
