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

    def runCode(@DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        closure.delegate = this
        closure()
    }
}
