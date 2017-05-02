package graph

class VertexSpecCodeRunner {
    private Graph graph
    private Vertex vertex

    public VertexSpecCodeRunner(Graph graph, Vertex vertex) {
        this.graph = graph
        this.vertex = vertex
    }

    Graph getGraph() {
        graph
    }

    Vertex getVertex() {
        vertex
    }

    public rename(String newName) {
        graph.rename(vertex.name, newName)
    }

    public traits(Class... traits) {
        vertex.delegateAs(traits as Class[])
    }

    public edgesFirst(String... names) {
        names.each {
            graph.edge vertex.name, it
        }
    }

    public edgesSecond(String... names) {
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
