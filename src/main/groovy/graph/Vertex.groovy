package graph


/**
 * A vertex in the graph. Every vertex should have a name. All vertices have a delegate which allows methods to be added
 * dynamically.
 */
class Vertex {
    @Delegate
    Map map = [:]

    Object getId() {
        get('id')
    }

    Object setId(Object id) {
        put('id', id)
    }

    boolean asBoolean() {
        id
    }

    boolean equals(Vertex vertex) {
        id == vertex.id
    }

    @Override
    boolean equals(Object o) {
        if(!(o instanceof Vertex)) {
            return false
        }
        if(this.is(o)) {
            return true
        }
        Vertex rhs = (Vertex) o
        id == rhs.id
    }
}
