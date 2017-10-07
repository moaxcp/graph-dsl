package graph.type.undirected

import graph.ConfigSpec
import graph.Graph
import graph.NameSpec
import graph.Vertex

/**
 * Delegate of the runnerCode closure in {@link GraphVertexSpec}. This provides methods and properties that can be used in
 * the closure. Method and property missing is delegated to the {@link Vertex}.
 */
class VertexSpecCodeRunner {
    private Graph graph
    private Vertex vertex

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
        return vertex
    }

    /**
     * renames the vertex to newName
     * @param newName
     */
    void rename(String newName) {
        graph.newVertexSpec([name:vertex.name, rename:newName]).apply()
    }

    /**
     * renames the vertex to newName
     * @param newName
     */
    void rename(NameSpec newName) {
        graph.newVertexSpec([name:vertex.name, rename:newName.name]).apply()
    }

    /**
     * Adds trait to the vertex.
     * @param traits
     */
    void traits(Class... traits) {
        graph.newVertexSpec([name:vertex.name, traits:traits]).apply()
    }

    /**
     * Creates edges where the vertex is edge.one and each name in names is edge.two.
     * @param names of vertices to connect to.
     */
    void connectsTo(String... names) {
        graph.newVertexSpec([name:vertex.name, connectsTo:names]).apply()
    }

    /**
     * Creates edges where the vertex is edge.one and each name in names is edge.two.
     * @param names of vertices to connect to.
     */
    void connectsTo(NameSpec... names) {
        graph.newVertexSpec([name:vertex.name, connectsTo:names*.name]).apply()
    }

    /**
     * Applies the specs to graph and adds edges using {@link #connectsTo(String...)}.
     * @param specs  specs to apply to graph and connectTo.
     */
    void connectsTo(ConfigSpec... specs) {
        specs.each {
            graph.newVertexSpec(it).apply()
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
