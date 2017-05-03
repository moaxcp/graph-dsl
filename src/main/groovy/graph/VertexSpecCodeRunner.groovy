package graph

/**
 * Delegate of the runnerCode closure in {@link VertexSpec}. This provides methods and properties that can be used in
 * the closure. If method and property missing is delegated to the vertex.
 */
class VertexSpecCodeRunner {
    private Graph graph
    private Vertex vertex

    /**
     * @return the graph this vertex is being added to.
     */
    Graph getGraph() {
        graph
    }

    /**
     * @return the vertex created by {@link VertexSpec}.
     */
    Vertex getVertex() {
        vertex
    }

    /**
     * renames the vertex to newName
     * @param newName
     */
    void rename(String newName) {
        graph.rename(vertex.name, newName)
    }

    /**
     * Adds traits to the vertex.
     * @param traits
     */
    void traits(Class... traits) {
        vertex.delegateAs(traits as Class[])
    }

    /**
     * Creates edges where the vertex is edge.one and each name in names is edge.two.
     * @param names of vertices to connect to.
     */
    void edgesFirst(String... names) {
        names.each {
            graph.edge vertex.name, it
        }
    }

    /**
     * Creates edges where the vertex is edge.two and each name in names is edge.one.
     * @param names of vetices to connect to.
     */
    void edgesSecond(String... names) {
        names.each {
            graph.edge it, vertex.name
        }
    }

    /**
     * calls method on vertex.
     * @param name
     * @param args
     * @return
     */
    def methodMissing(String name, args) {
        vertex.invokeMethod(name, args)
    }

    /**
     * returns property from vertex.
     * @param name
     * @return
     */
    def propertyMissing(String name) {
        vertex[name]
    }

    /**
     * sets property on vertex.
     * @param name
     * @param value
     * @return
     */
    def propertyMissing(String name, value) {
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
    def runCode(@DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        closure.delegate = this
        closure()
    }
}
