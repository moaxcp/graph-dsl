package graph.type

import graph.Edge
import graph.Graph
import graph.NameSpec
import graph.type.undirected.EdgeSpecCodeRunner

/**
 * Specification class that helps edge methods in {@link Graph} objects create or update {@link Edge} objects. The
 * specification describes what {@link Edge} to create or update, If one and two should be renamed, and what trait to
 * setup to the {@link Edge}. runnerCode is a closure that will be run using an {@link EdgeSpecCodeRunner}.
 */
class EdgeSpec {

    private Graph graph
    private Edge edge
    private String one
    private String two
    private String renameOne
    private String renameTwo
    private final Set<Class> traitsSet = [] as Set<Class>
    private Closure runnerCodeClosure

    EdgeSpec(Graph graph, Map<String, ?> map) {
        this.graph = graph
        one = map.one
        two = map.two
        renameOne = map.renameOne instanceof NameSpec ? map.renameOne.name : map.renameOne
        renameTwo = map.renameTwo instanceof NameSpec ? map.renameTwo.name : map.renameTwo

        map.traits?.each {
            traitsSet.add((Class) it)
        }
    }

    EdgeSpec(Graph graph, Map<String, ?> map, Closure closure) {
        this(graph, map)
        runnerCodeClosure = closure
    }

    Graph getGraph() {
        graph
    }

    Edge getEdge() {
        edge
    }

    void initEdgeAndSetupVertices() {
        if (!one) {
            throw new IllegalArgumentException('!one failed. one must be groovy truth.')
        }
        if (!two) {
            throw new IllegalArgumentException('!two failed. two must be groovy truth.')
        }
        Edge e = graph.newEdge(one, two)
        edge = graph.edges.find { it == e }

        if (edge) {
            if (renameOne || renameTwo) {
                //need to delete and re-add edge to reset hashcode in LinkedHash.
                graph.deleteEdge(edge.one, edge.two)
            }
        } else {
            edge = e
        }
        graph.vertex(one)
        graph.vertex(two)
    }

    void initRenameOne() {
        if (renameOne) {
            graph.vertex(renameOne)
            edge.one = renameOne
        }
    }

    void initRenameTwo() {
        if (renameTwo) {
            graph.vertex(renameTwo)
            edge.two = renameTwo
        }
    }

    void applyTraits() {
        if (traitsSet) {
            edge.delegateAs(this.traitsSet as Class[])
        }
    }

    void applyClosure() {
        if (runnerCodeClosure) {
            EdgeSpecCodeRunner runner = new EdgeSpecCodeRunner(graph, edge)
            runner.runCode(runnerCodeClosure)
        }
    }

    Edge setup() {
        initEdgeAndSetupVertices()
        initRenameOne()
        initRenameTwo()
        applyTraits()
        edge
    }
}
