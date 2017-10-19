package graph.internal

class PropertyDelegator {
    Map delegate = [:]

    def propertyMissing(String name, value) {
        delegate[name] = value
    }

    def propertyMissing(String name) {
        if(delegate[name] instanceof Closure) {
            return ((Closure)delegate[name]).call()
        }
        if(delegate.containsKey(name)) {
            return delegate[name]
        } else {
            throw new MissingPropertyException("could not find property $name")
        }
    }
}
