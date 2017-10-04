package graph.type.undirected

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.VertexSpec

/*
 * TODO refactoring for ConfigSpec.
 * GraphVertexSpec will be a single use object Once it is applied it should be discarded
 * 1. create GraphVertexSpec with graph, map
 *      this organizes the map into values for the Vertex
 *
 * 2. create Vertex with graph, map and closure
 *      organizes map and sets codeRunner
 *
 * 3. remove codeRunner as an option from the map
 *      If you want it you should use a closure in the ConfigSpec
 *
 * 4. setup map
 *      This creates the Vertex from the map
 *
 * 5. setup codeRunner
 *      Runs the closure against the vertex and graph
 *
 * 6. remove all other methods.
 *
 * This optimizes reuse in other types of graphs. Types can reuse the setup
 * methods as needed. This makes it easier to test as well.
 */
/**
 * Specification class that helps vertex methods in {@link Graph} objects. GraphVertexSpec is used to collect the details
 * of an update or create.
 */
class GraphVertexSpec extends VertexSpec {
    private Graph graph
    private Vertex vertex
    private String name
    private String rename
    private final Set<Class> traitsSet = [] as Set<Class>
    final Set<String> connectsToSet = [] as Set<String>
    private Closure runnerCodeClosure

    GraphVertexSpec(Graph graph, Map<String, ?> map) {
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

    GraphVertexSpec(Graph graph, Map<String, ?> map, Closure closure) {
        this(graph, map)
        runnerCodeClosure = closure
    }

    Graph getGraph() {
        graph
    }

    Vertex getVertex() {
        vertex
    }

    Closure getClosure() {
        runnerCodeClosure
    }

    void findVertex() {
        if(vertex) {
            throw new IllegalStateException("already ran spec")
        }
        if (!name) {
            throw new IllegalStateException('!name failed. Name must be groovy truth.')
        }
        vertex = graph.vertices[name] ?: graph.newVertex(name)
    }

    void applyRename() {
        if(graph.vertices[name] && rename) {
            graph.rename(name, rename)
        } else if(rename) {
            vertex.name = rename
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

    Vertex setup() {
        findVertex()
        applyRename()
        applyTraits()
        applyConnectsTo()
        vertex
    }
}
