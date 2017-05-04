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
    private Set<Class> traits = [] as Set<Class>
    private Closure config

    /**
     * The set of traits that will be applied to the {@link Edge}.
     * @return
     */
    Set<Class> getTraits() {
        traits
    }

    /**
     * The config applied to the {@link Edge} using its {@link Edge#leftShift(Edge)} operator.
     * @return
     */
    Closure getConfig() {
        config
    }

    /**
     * Adds to the set of traits that will be applied to the {@link Edge}.
     * @param traits - added to the set
     */
    void traits(Class... traits) {
        this.traits.addAll(traits)
    }

    /**
     * Sets the config closure which will be run on the {@link Edge}.
     * @param config
     */
    void config(@DelegatesTo(Edge) Closure config) {
        this.config = config
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
    void applyToGraphAndEdge(Graph graph, Edge edge) {
        if (one) {
            graph.vertex(one)
            edge.one = one
        }
        if (two) {
            graph.vertex(two)
            edge.two = two
        }
        if (traits) {
            edge.delegateAs(this.traits as Class[])
        }
        if (config) {
            edge << config
        }
    }

    /**
     * Creates a new instance of an EdgeSpec using the provided closure. Anew @link EdgeSpec} is the delegate
     * while the {@link Graph} is the owner and this.
     * @param graph
     * @param closure
     * @return
     */
    static EdgeSpec newInstance(Graph graph, @DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec spec = new EdgeSpec()

        Closure code = closure.rehydrate(spec, graph, graph)
        code()

        spec
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
        EdgeSpec spec = new EdgeSpec(one: map.one, two: map.two)
        if (map.traits) {
            spec.traits(map.traits as Class[])
        }
        if (map.config) {
            spec.config(map.config)
        }

        spec
    }
}
