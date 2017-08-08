package graph

/**
 * Delegate of the runnerCode closure in {@link VertexSpec}. This provides methods and properties that can be used in
 * the closure. Method and property missing is delegated to the {@link Vertex}.
 */
class VertexSpecCodeRunner {
    Graph graph
    Vertex vertex

    /**
     * renames the vertex to newName
     * @param newName
     */
    void rename(String newName) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, rename:newName)
        spec.apply(graph)
    }

    /**
     * renames the vertex to newName
     * @param newName
     */
    void rename(NameSpec newName) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, rename:newName.name)
        spec.apply(graph)
    }

    /**
     * Adds traits to the vertex.
     * @param traits
     */
    void traits(Class... traits) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, traits:traits)
        spec.apply(graph)
    }

    /**
     * Creates edges where the vertex is edge.one and each name in names is edge.two.
     * @param names of vertices to connect to.
     */
    void connectsTo(String... names) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, connectsTo:names)
        spec.apply(graph)
    }

    /**
     * Creates edges where the vertex is edge.one and each name in names is edge.two.
     * @param names of vertices to connect to.
     */
    void connectsTo(NameSpec... names) {
        VertexSpec spec = graph.vertexSpecFactory.newVertexSpec(name:vertex.name, connectsTo:names*.name)
        spec.apply(graph)
    }

    /**
     * Applies the specs to graph and adds edges using {@link #connectsTo(String...)}.
     * @param specs  specs to apply to graph and connectTo.
     */
    void connectsTo(ConfigSpec... specs) {
        specs.each {
            graph.vertexSpecFactory.newVertexSpec(it).apply(graph)
        }
        connectsTo(specs*.map.name as String[])
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
        vertex[name]
    }

    /**
     * sets property on vertex.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        if (name == 'name') {
            throw new MissingPropertyException('Cannot set name in dsl. Consider using the rename method.')
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
