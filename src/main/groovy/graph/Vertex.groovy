package graph

import graph.internal.PropertyDelegator
import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * A vertex in the graph. Every vertex should have a name. All vertices have a delegate which allows methods to be added
 * dynamically.
 */
class Vertex extends LinkedHashMap<String, Object> {
    boolean asBoolean() {
        key
    }

    @Override
    boolean equals(Object o) {
        if(!(o instanceof Vertex)) {
            return false
        }
        if(this.is(0)) {
            return true
        }
        Vertex rhs = (Vertex) o
        key == rhs.key
    }
}
