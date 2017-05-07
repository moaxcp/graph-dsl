package graph

/**
 * Specification class that helps edge methods in {@link Graph} objects. It can be the delegate of closures used in
 * edge methods of {@link Graph}.
 */
class EdgeSpec {
    /**
     * Name of the first {@link Vertex} in the {@link Edge}.
     */
    String one
    /**
     * Name of the second {@link Vertex} in the {@link Edge}.
     */
    String two

    String renameOne
    String renameTwo

    private Set<Class> traitsSet = [] as Set<Class>
    private Closure runnerCodeClosure

    /**
     * The set of traits that will be applied to the {@link Edge}.
     * @return
     */
    Set<Class> getTraits() {
        traitsSet
    }

    Closure getRunnerCode() {
        runnerCodeClosure
    }

    /**
     * Adds to the set of traits that will be applied to the {@link Edge}.
     * @param traits - added to the set
     */
    void traits(Class... traits) {
        this.traitsSet.addAll(traits)
    }

    /**
     * @param config
     */
    void runnerCode(@DelegatesTo(EdgeSpecCodeRunner) Closure runnerCodeClosure) {
        this.runnerCodeClosure = runnerCodeClosure
    }

    /**
     * Applies this {@link EdgeSpec} to the Edge and Graph. Members from this spec are applied in this order:
     * <p>
     * 1. creates or gets the vertex and sets edge.one to one
     * 2. creates or gets the vertex and sets edge.two to two
     * 3. applies traits to edge using delegateAs
     * 4. configures the edge using edge << config
     * @param graph
     * @param edge
     */
    Edge apply(Graph graph) {
        if(!one) {
            throw new IllegalArgumentException("!one failed. one must be groovy truth.")
        }
        if(!two) {
            throw new IllegalArgumentException("!two failed. two must be groovy truth.")
        }
        Edge e = graph.edgeFactory.newEdge(one, two)
        Edge edge = graph.edges.find { it == e } ?: e
        graph.addEdge(edge)
        graph.vertex(one)
        graph.vertex(two)
        edge.one = one
        edge.two = two

        if (renameOne) {
            graph.vertex(renameOne)
            edge.one = renameOne
        }
        if (renameTwo) {
            graph.vertex(renameTwo)
            edge.two = renameTwo
        }
        if (traitsSet) {
            edge.delegateAs(this.traitsSet as Class[])
        }

        if (runnerCodeClosure) {
            EdgeSpecCodeRunner runner = new EdgeSpecCodeRunner(graph:graph, edge:edge)
            runner.runCode(runnerCodeClosure)
        }

        edge
    }

    /**
     * Creates a new instance of an {@link EdgeSpec} using the provided Map. Valid values that can be in the Map are:
     * <p>
     * traits - list of traits to be applied to the {@link Edge}<br>
     * config - closure to be applied to the {@link Edge}
     * <p>
     * @param map
     * @return
     */
    static EdgeSpec newInstance(Map<String, ?> map) {
        EdgeSpec spec = new EdgeSpec(one:map.one, two:map.two, renameOne:map.renameOne, renameTwo:map.renameTwo)
        if (map.traits) {
            spec.traits(map.traits as Class[])
        }
        if (map.runnerCode) {
            spec.runnerCode(map.runnerCode)
        }

        spec
    }
}
