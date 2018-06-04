package graph.type.undirected

import graph.ConfigSpec
import graph.Graph
import graph.Vertex

/**
 * Delegate of the runnerCode closure in {@link GraphVertexSpec}. This provides methods and properties that can be used
 * in the closure. Method and property missing is delegated to the {@link Vertex}.
 */
class VertexSpecCodeRunner {
    private final Graph graph
    private final Vertex vertex

    VertexSpecCodeRunner(Graph graph, Vertex vertex) {
        this.graph = graph
        this.vertex = vertex
    }

    /**
     * Gets the graph the {@link Vertex} has been added to. This can be used inside the runnerCode to access the graph.
     * @return the graph the {@link Vertex} has been added to.
     */
    Graph getGraph() {
        graph
    }

    /**
     * Gets the {@link Vertex} that has been added. This can be used inside the runnerCode to access the vertex.
     * @return the vertex that has been added.
     */
    Vertex getVertex() {
        vertex
    }

    /**
     * Changes the key for the vertex to changeKey
     * @param newKey
     */
    void changeKey(Object newKey) {
        graph.newVertexSpec([key:vertex.key, changeKey:newKey]).apply()
    }

    /**
     * Creates edges where the vertex is edge.one and each key in keys is edge.two.
     * @param vertices to connect to.
     */
    void connectsTo(Object... keys) {
        graph.newVertexSpec([key:vertex.key, connectsTo:keys]).apply()
    }

    void connectsTo(Object key, Closure closure) {
        graph.newVertexSpec([key:vertex.key, connectsTo:key]).apply()
        graph.newVertexSpec([key:key], closure).apply()
    }

    /**
     * calls method on vertex.
     * @param name
     * @param args
     * @return
     */
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        vertex.invokeMethod(name, args)
    }

    /**
     * returns property from vertex.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        if (vertex[name]) {
            vertex[name]
        } else {
            throw new MissingPropertyException("Missing $name")
        }
    }

    /**
     * sets property on vertex.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        if (name == 'key') {
            graph.changeKey(vertex.key, value)
        }
        vertex[name] = value
    }

    /**
     * This object becomes the delegate of closure and then closure is called. The methods
     * in this object become available in the closure along with the properties vertex and graph.
     * If methods or properties are missing then vertex is checked. Otherwise standard closure delegation
     * takes place (owner, this).
     * @param closure
     * @return
     */
    void runCode(@DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
