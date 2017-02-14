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

    def propertyMissing(String name) {
        delegate[name]
    }

    def propertyMissing(String name, value) {
        delegate[name] = value
    }
}