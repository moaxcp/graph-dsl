package graph.type

import graph.Edge
import graph.EdgeSpec
import graph.Graph
import graph.NameSpec

/**
 * Base implementation of an EdgeSpec. Type packages can inherit this class to implement default methods in EdgeSpec.
 */
abstract class AbstractEdgeSpec extends EdgeSpec {
    Edge edge
    boolean edgePresentInGraph

    Object one
    Object two
    Object changeOne
    Object changeTwo
    Closure runnerCodeClosure

    protected AbstractEdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph)

        one = map.one
        two = map.two
        changeOne = map.changeOne instanceof NameSpec ? map.changeOne.name : map.changeOne
        changeTwo = map.changeTwo instanceof NameSpec ? map.changeTwo.name : map.changeTwo

        runnerCodeClosure = closure
    }

    protected init() {
        if (edge) {
            throw new IllegalStateException('Edge already created.')
        }
        Edge created = graph.newEdge(one, two)
        Edge existing = graph.edges.find { it == created }
        edgePresentInGraph = existing != null

        if (!edgePresentInGraph) {
            one = changeOne ?: one
            two = changeTwo ?: two
            changeOne = null
            changeTwo = null
            created.one = one
            created.two = two
            existing = graph.edges.find { it == created }
            edgePresentInGraph = existing != null
        }

        edge = existing ?: created
    }

    protected void checkConditions() {
        if (!one) {
            throw new IllegalStateException('!one failed. one must be groovy truth.')
        }
        if (!two) {
            throw new IllegalStateException('!two failed. two must be groovy truth.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!edge) {
            throw new IllegalStateException('edge is not set created.')
        }
        if (changeOne || changeTwo) {
            Edge renamed = graph.newEdge(changeOne ?: one, changeTwo ?: two)
            if (graph.edges.find { it == renamed }) {
                throw new IllegalStateException('renamed edge already exists.')
            }
        }
    }

    protected void setupGraph() {
        if (edgePresentInGraph && (changeOne || changeTwo)) {
            //need to delete and re-add edge to reset hashcode in LinkedHashSet.
            graph.deleteEdge(edge.one, edge.two)
        }

        graph.vertex(one)
        graph.vertex(two)
    }

    protected void initRenameOne() {
        if (changeOne) {
            graph.vertex(changeOne)
            edge.one = changeOne
        }
    }

    protected void initRenameTwo() {
        if (changeTwo) {
            graph.vertex(changeTwo)
            edge.two = changeTwo
        }
    }

    protected abstract void applyClosure()

    Edge apply() {
        init()
        checkConditions()
        setupGraph()
        initRenameOne()
        initRenameTwo()
        addEdge(edge)
        applyClosure()
        edge
    }
}
