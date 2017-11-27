package graph


/**
 * A vertex in the graph. Every vertex should have a name. All vertices have a delegate which allows methods to be added
 * dynamically.
 */
class Vertex {
    @Delegate
    Map map = [:]

    Object getKey() {
        get('key')
    }

    Object setKey(Object value) {
        put('key', value)
    }

    boolean asBoolean() {
        key
    }

    boolean equals(Vertex vertex) {
        key == vertex.key
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
