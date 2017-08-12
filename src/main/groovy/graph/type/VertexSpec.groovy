package graph.type

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.undirected.VertexSpecCodeRunner


/*
 * TODO refactoring for ConfigSpec.
 * VertexSpec will be a single use object Once it is applied it should be discarded
 * 1. create VertexSpec with map
 *      this organizes the map into values for the Vertex
 *
 * 2. create Vertex with map and closure
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

    /**
     * The name of the {@link graph.Vertex} to create or update.
     */
    String name

    /**
     * The new name to give a {@link graph.Vertex}
     */
    String rename

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
    VertexSpec(Map<String, ?> map) {
        name = map.name
        rename = map.rename instanceof NameSpec ? map.rename.name : map.rename

        if (map.traits) {
            traits(map.traits as Class[])
        }

        map.connectsTo?.each {
            connectsTo((String) (it instanceof NameSpec ? it.name : it))
        }

        if (map.runnerCode) {
            runnerCode(map.runnerCode)
        }
    }

    /**
     * The set of trait that should be applied to the {@link graph.Vertex}.
     * @return
     */
    Set<Class> getTraits() {
        traitsSet
    }

    /**
     * The set of edges to create between the {@link graph.Vertex} and other vertices. The {@link graph.Vertex} will be
     * edge.one.
     * @return The names of vertices the {@link graph.Vertex} should connect to.
     */
    Set<String> getConnectsTo() {
        Collections.unmodifiableSet(connectsToSet)
    }

    /**
     * The runnerCode. This will be run against a VertexSpecCodeRunner
     * @return
     */
    Closure getRunnerCode() {
        this.runnerCodeClosure
    }

    /**
     * Adds to the set of trait to be applied to the {@link graph.Vertex}.
     * @param traits - added to the set
     */
    void traits(Class... traits) {
        this.traitsSet.addAll(traits)
    }

    /**
     * Adds to the names the {@link graph.Vertex} should connect to. In the resulting edge the vertex named by this spec
     * will be edge.one.
     * @param names
     */
    void connectsTo(String... names) {
        connectsToSet.addAll(names)
    }

    /**
     * Sets the runnerCode closure.
     * @param runnerCode
     */
    void runnerCode(Closure runnerCode) {
        this.runnerCodeClosure = runnerCode
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
    Vertex apply(Graph graph) {
        if (!name) {
            throw new IllegalArgumentException('!name failed. Name must be groovy truth.')
        }
        Vertex vertex = graph.vertices[name] ?: graph.vertexFactory.newVertex(name)
        graph.addVertex(vertex)

        if (rename) {
            graph.rename(name, rename)
        }
        if (traitsSet) {
            vertex.delegateAs(traitsSet as Class[])
        }
        connectsToSet.each {
            graph.edge vertex.name, it
        }

        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }

        vertex
    }
}
