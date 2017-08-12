package graph.type

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.undirected.VertexSpecCodeRunner


/*
 * TODO refactoring for ConfigSpec.
 * VertexSpec will be a single use object Once it is applied it should be discarded
 * 1. create VertexSpec with graph, map
 *      this organizes the map into values for the Vertex
 *
 * 2. create Vertex with graph, map and closure
 *      organizes map and sets codeRunner
 *
 * 3. remove codeRunner as an option from the map
 *      If you want it you should use a closure in the ConfigSpec
 *
 * 4. apply map
 *      This creates the Vertex from the map
 *
 * 5. apply codeRunner
 *      Runs the closure against the vertex and graph
 *
 * 6. remove all other methods.
 *
 * This optimizes reuse in other types of graphs. Types can reuse the apply
 * methods as needed. This makes it easier to test as well.
 */
/**
 * Specification class that helps vertex methods in {@link Graph} objects. VertexSpec is used to collect the details
 * of an update or create.
 */
class VertexSpec {
    private Graph graph
    private Vertex vertex
    private String name
    private String rename
    private final Set<Class> traitsSet = [] as Set<Class>
    private final Set<String> connectsToSet = [] as Set<String>
    private Closure runnerCodeClosure

    /**
     * creates a new instance of a VertexSpec using the provided Map. Valid values that can be in the Map are:
     * <p>
     * name - the name of the vertex to create or update<br>
     * rename - what to rename the vertex<br>
     * trait - list of trait to be applied to the {@link graph.Vertex}<br>
     * connectsTo - list of vertices to connect the {@link graph.Vertex} to. The vertex is edge.one<br>
     * connectsFrom - list of vertices to connect the {@link graph.Vertex} to. The vertex is edge.two<br>
     * runnerCode - closure to be applied to the {@link graph.Vertex} after trait and edges are created.
     * <p>
     * All other values are ignored.
     * @param map
     * @return
     */
    VertexSpec(Graph graph, Map<String, ?> map) {
        this.graph = graph
        name = map.name
        rename = map.rename instanceof NameSpec ? map.rename.name : map.rename

        map.traits?.each{
            traitsSet.add((Class) it)
        }

        map.connectsTo?.each {
            connectsToSet.add((String) (it instanceof NameSpec ? it.name : it))
        }
    }

    VertexSpec(Graph graph, Map<String, ?> map, Closure closure) {
        this(graph, map)
        runnerCodeClosure = closure
    }

    Graph getGraph() {
        graph
    }

    Vertex getVertex() {
        vertex
    }

    Set<String> getConnectsToSet() {
        connectsToSet
    }

    Closure getClosure() {
        runnerCodeClosure
    }

    void applyVertex() {
        if(vertex) {
            throw new IllegalStateException("already ran spec")
        }
        if (!name) {
            throw new IllegalStateException('!name failed. Name must be groovy truth.')
        }
        vertex = graph.vertices[name] ?: graph.vertexFactory.newVertex(name)
        graph.addVertex(vertex)
    }

    void applyRename() {
        if (rename) {
            graph.rename(name, rename)
        }
    }

    void applyTraits() {
        if (traitsSet) {
            vertex.delegateAs(traitsSet as Class[])
        }
    }

    void applyConnectsTo() {
        connectsToSet.each {
            graph.edge vertex.name, it
        }
    }

    void applyClosure() {
        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }
    }

    /**
     * Applies this {@link VertexSpec} to the {@link graph.Vertex} and {@link graph.Graph}. Members from this spec are
     * applied in this order:
     * <p>
     * 1. renames vertex to rename if set<br>
     * 2. applies trait to the vertex<br>
     * 3. creates edges between the vertex and connectsTo where the vertex is edge.one<br>
     * 4. creates edges between the vertex and connectsFrom where the vertex is edge.two<br>
     * @param graph
     */
    Vertex apply() {
        applyVertex()
        applyRename()
        applyTraits()
        applyConnectsTo()
        applyClosure()
        vertex
    }
}
