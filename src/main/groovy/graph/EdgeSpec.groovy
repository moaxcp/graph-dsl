package graph

/**
 * Base implementation of an EdgeSpec.
 *
 * Add new dsl properties for the Edge dsl to dslProperties. This will prevent them from being added to the Edge.
 */
class EdgeSpec {
    private Graph graph
    private Edge edge
    private boolean edgePresentInGraph
    Object from
    Object to
    private Object changeFrom
    private Object changeTo
    private List dslProperties
    private Map entries
    private Closure runnerCodeClosure
    private boolean applied

    /**
     * Gets the graph the {@link Edge} has been added to. This can be used inside the runnerCode to access the graph.
     * @return the graph the {@link Edge} has been added to.
     */
    Graph getGraph() {
        graph
    }

    //todo edge returned should not be able to set one or two. A trait can be applied to prevent this.
    // This is an issue on all edge methods.
    /**
     * Gets the {@link Edge} that has been added. This can be used inside the runnerCode to access the edge.
     * @return the edge that has been added.
     */
    Edge getEdge() {
        edge
    }

    void setFrom(Object from) {
        changeFrom(from)
        this.from = from
    }

    void setTo(Object to) {
        changeTo(to)
        this.to = to
    }

    EdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        if(!graph) {
            throw new IllegalArgumentException('graph must be set.')
        }
        if(!map) {
            throw new IllegalArgumentException('map must be set.')
        }
        this.graph = graph

        dslProperties = ['from', 'to', 'changeFrom', 'changeTo']

        entries = map.findAll { !(it.key in dslProperties) }

        from = map.from
        to = map.to
        changeFrom = map.changeFrom
        changeTo = map.changeTo

        runnerCodeClosure = closure

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
        if (!from) {
            throw new IllegalStateException('!from failed. from must be groovy truth.')
        }
        if (!to) {
            throw new IllegalStateException('!to failed. to must be groovy truth.')
        }
        if (changeFrom || changeTo) {
            Edge renamed = graph.newEdge(from:changeFrom ?: from, to:changeTo ?: to)
            if (graph.edges.find { it == renamed }) {
                throw new IllegalStateException('renamed edge already exists.')
            }
        }
    }

    /**
     * Changes edge.from to changeFrom. This allows edge.from to point to a different {@link graph.Vertex}
     * @param changeFrom the new id for edge.from
     */
    void changeFrom(Object changeFrom) {
        graph.newEdgeSpec([from:edge.from, to:edge.to, changeFrom:changeFrom]).apply()
    }

    /**
     * Changes edge.to to changeTo. This allows edge.two to point to a different {@link graph.Vertex}.
     * @param changeTo  the new id for edge.to
     */
    void changeTo(Object changeTo) {
        graph.newEdgeSpec([from:edge.from, to:edge.to, changeTo:changeTo]).apply()
    }

    private void setupGraph() {
        if (edgePresentInGraph && (changeFrom || changeTo)) {
            //need to delete and re-add edge to reset hashcode in LinkedHashSet.
            graph.deleteEdge(edge.from, edge.to)
        }

        graph.vertex(from)
        graph.vertex(to)
    }

    private void initRenameOne() {
        if (changeFrom) {
            graph.vertex(changeFrom)
            edge.from = changeFrom
        }
    }

    private void initRenameTwo() {
        if (changeTo) {
            graph.vertex(changeTo)
            edge.to = changeTo
        }
    }

    private void initEntries() {
        edge.putAll(entries)
    }

    private void applyClosure() {
        if (!runnerCodeClosure) {
            return
        }
        runnerCodeClosure.delegate = this
        runnerCodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
        runnerCodeClosure()
    }

    Edge apply() {
        if(applied) {
            throw new IllegalStateException('spec has already been applied.')
        }
        applied = true
        setupGraph()
        initRenameOne()
        initRenameTwo()
        initEntries()
        graph.addEdge(edge)
        applyClosure()
        edge
    }

    /**
     * Calls the missing method on the {@link Edge}.
     * @param name
     * @param args
     * @return
     */
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        edge.invokeMethod(name, args)
    }

    /**
     * Gets the property from the {@link Edge}.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        if (edge[name]) {
            edge[name]
        } else {
            throw new MissingPropertyException("Missing $name")
        }
    }

    /**
     * Sets the property on the {@link Edge}.
     * @throws MissingPropertyException when attempting to set one or two.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        edge[name] = value
    }
}
