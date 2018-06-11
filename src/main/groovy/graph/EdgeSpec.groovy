package graph

/**
 * Base implementation of an EdgeSpec.
 *
 * Add new dsl properties for the Edge dsl to dslProperties. This will prevent them from being added to the Edge.
 */
class EdgeSpec {
    Graph graph
    Edge edge
    boolean edgePresentInGraph

    Object from
    Object to
    Object changeFrom
    Object changeTo
    List dslProperties
    Map entries
    Closure runnerCodeClosure

    protected EdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        this.graph = graph

        dslProperties = ['from', 'to', 'changeFrom', 'changeTo']

        entries = map.findAll { !(it.key in dslProperties) }

        from = map.from
        to = map.to
        changeFrom = map.changeFrom
        changeTo = map.changeTo

        runnerCodeClosure = closure
    }

    protected init() {
        if (edge) {
            throw new IllegalStateException('Edge already created.')
        }
        Edge created = graph.newEdge(from:from, to:to)
        Edge existing = graph.edges.find { it == created }
        edgePresentInGraph = existing != null

        if (!edgePresentInGraph) {
            from = changeFrom ?: from
            to = changeTo ?: to
            changeFrom = null
            changeTo = null
            created.from = from
            created.to = to
            existing = graph.edges.find { it == created }
            edgePresentInGraph = existing != null
        }

        edge = existing ?: created
    }

    protected void checkConditions() {
        if (!from) {
            throw new IllegalStateException('!from failed. from must be groovy truth.')
        }
        if (!to) {
            throw new IllegalStateException('!to failed. to must be groovy truth.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!edge) {
            throw new IllegalStateException('edge is not set.')
        }
        if (changeFrom || changeTo) {
            Edge renamed = graph.newEdge(from:changeFrom ?: from, to:changeTo ?: to)
            if (graph.edges.find { it == renamed }) {
                throw new IllegalStateException('renamed edge already exists.')
            }
        }
    }

    protected void setupGraph() {
        if (edgePresentInGraph && (changeFrom || changeTo)) {
            //need to delete and re-add edge to reset hashcode in LinkedHashSet.
            graph.deleteEdge(edge.from, edge.to)
        }

        graph.vertex(from)
        graph.vertex(to)
    }

    protected void initRenameOne() {
        if (changeFrom) {
            graph.vertex(changeFrom)
            edge.from = changeFrom
        }
    }

    protected void initRenameTwo() {
        if (changeTo) {
            graph.vertex(changeTo)
            edge.to = changeTo
        }
    }

    protected void initEntries() {
        edge.putAll(entries)
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            EdgeSpecCodeRunner runner = new EdgeSpecCodeRunner(graph, edge)
            runner.runCode(runnerCodeClosure)
        }
    }

    Edge apply() {
        init()
        checkConditions()
        setupGraph()
        initRenameOne()
        initRenameTwo()
        initEntries()
        graph.addEdge(edge)
        applyClosure()
        edge
    }
}
