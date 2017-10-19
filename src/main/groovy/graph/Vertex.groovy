package graph

import graph.internal.PropertyDelegator
import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * A vertex in the graph. Every vertex should have a name. All vertices have a delegate which allows methods to be added
 * dynamically.
 */
@ToString(includeNames=true)
@EqualsAndHashCode
class Vertex extends PropertyDelegator {
    String name

    @PackageScope
    void setName(String name) {
        this.name = name
    }

    Object getAt(String name) {
        if(name == 'name') {
            return this.name
        }
        delegate[name]
    }
}
