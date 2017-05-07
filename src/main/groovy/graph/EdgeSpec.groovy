package graph

/**
 * Specification class that helps edge methods in {@link Graph} objects create or update {@link Edge} objects. The
 * specification describes what {@link Edge} to create or update, If one and two should be renamed, and what traits to
 * apply to the {@link Edge}. runnerCode is a closure that will be run using an {@link EdgeSpecCodeRunner}.
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

    /**
     * New name to give one in the {@link Edge}.
     */
    String renameOne
    /**
     * New name to give two in the {@link Edge}.
     */
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

    /**
     * Gets the runnerCode from this EdgeSpec.
     * @return the runner code from this EdgeSpec.
     */
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
     * sets runnerCodeClosure. This will be run when this is applied to a graph.
     * @param runnerCodeClosure assigned to runnerCodeClosure
     */
    void runnerCode(@DelegatesTo(EdgeSpecCodeRunner) Closure runnerCodeClosure) {
        this.runnerCodeClosure = runnerCodeClosure
    }

    /**
     * Applies this {@link EdgeSpec} to the Edge and Graph. Members from this spec are applied in this order:
     * <p>
     * 1. creates or gets the {@link Edge} with one and two<br>
     * 2. creates or gets the vertices one and two point to<br>
     * 3. if renameOne is set, renames one and creates or gets the vertex<br>
     * 4. if renameTwo is set, renames two and creates or gets the vertex<br>
     * 5. adds any traits to the edge's delegate using {@link Edge#delegateAs(Class[])}<br>
     * 6. runs runnerCodeClosure against the graph and edge using a new {@link EdgeSpecCodeRunner}<br>
     * 7. returns the edge<br>
     * </p>
     * @throws IllegalArgumentException if one or two is empty or null.
     * @param graph to apply this EdgeSpec to
     * @param edge the resulting edge
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
     * overlays the provided EdgeSpec with this spec returning a new EdgeSpec.
     * @param spec to overlay with this
     * @return the resulting EdgeSpec
     */
    EdgeSpec overlay(EdgeSpec spec) {
        EdgeSpec next = new EdgeSpec()
        next.one = spec.one ?: one
        next.two = spec.two ?: two
        next.renameOne = spec.renameOne ?: renameOne
        next.renameTwo = spec.renameTwo ?: renameTwo
        next.traits((traitsSet + spec.traits) as Class[])
        if (this.runnerCodeClosure) {
            next.runnerCode this.runnerCodeClosure << spec.runnerCodeClosure
        } else {
            next.runnerCode spec.runnerCodeClosure
        }

        next
    }

    /**
     * Creates a new instance of an {@link EdgeSpec} using the provided Map. Valid values that can be in the Map are:
     * <p>
     * one - the first vertex name in the {@link Edge}<br>
     * two - the second vertex name in the {@link Edge}<br>
     * renameOne - vertex name to rename one to in the {@link Edge}<br>
     * renameTwo - vertex name to rename two to in the {@link Edge}<br>
     * traits - list of traits to be applied to the {@link Edge}<br>
     * runnerCode - closure to be applied to the {@link Graph} and {@link Edge} using a {@link EdgeSpecCodeRunner}
     * </p>
     * @param map containing list of valid values
     * @return the resulting EdgeSpec
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
