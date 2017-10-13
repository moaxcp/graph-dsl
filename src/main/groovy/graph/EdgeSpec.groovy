package graph

import graph.type.undirected.EdgeSpecCodeRunner

class EdgeSpec {
    private Graph graph
    private Edge edge
    private boolean edgePresentInGraph

    private String one
    private String two
    private String renameOne
    private String renameTwo
    private final Set<Class> traitsSet = [] as Set<Class>
    private Closure runnerCodeClosure

    EdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        if (this.graph) {
            throw new IllegalArgumentException('Graph already set.')
        }
        this.graph = graph

        one = map.one
        two = map.two
        renameOne = map.renameOne instanceof NameSpec ? map.renameOne.name : map.renameOne
        renameTwo = map.renameTwo instanceof NameSpec ? map.renameTwo.name : map.renameTwo

        map.traits?.each {
            traitsSet.add((Class) it)
        }

        runnerCodeClosure = closure
    }

    protected init() {
        if(edge) {
            throw new IllegalStateException('Edge already created.')
        }
        Edge created = graph.newEdge(one, two)
        Edge existing = graph.edges.find { it == created }
        edgePresentInGraph = existing != null

        if(!edgePresentInGraph) {
            one = renameOne ?: one
            two = renameTwo ?: two
            renameOne = null
            renameTwo = null
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
        if (renameOne || renameTwo) {
            Edge renamed = graph.newEdge(renameOne ?: one, renameTwo ?: two)
            if (graph.edges.find { it == renamed }) {
                throw new IllegalStateException('renamed edge already exists.')
            }
        }
    }

    protected void setupGraph() {
        if (edgePresentInGraph && (renameOne || renameTwo)) {
            //need to delete and re-add edge to reset hashcode in LinkedHashSet.
            graph.deleteEdge(edge.one, edge.two)
        }

        graph.vertex(one)
        graph.vertex(two)
    }

    protected void initRenameOne() {
        if (renameOne) {
            graph.vertex(renameOne)
            edge.one = renameOne
        }
    }

    protected void initRenameTwo() {
        if (renameTwo) {
            graph.vertex(renameTwo)
            edge.two = renameTwo
        }
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            EdgeSpecCodeRunner runner = new EdgeSpecCodeRunner(graph, edge)
            runner.runCode(runnerCodeClosure)
        }
    }

    protected void addEdge() {
        graph.addEdge(edge)
    }

    Edge apply() {
        init()
        checkConditions()
        setupGraph()
        initRenameOne()
        initRenameTwo()
        addEdge()
        applyClosure()
        edge
    }
}
