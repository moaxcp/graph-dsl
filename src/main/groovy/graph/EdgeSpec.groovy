package graph

class EdgeSpec {
    String one
    String two
    private Set<Class> traits = [] as Set<Class>
    private Closure config

    Set<Class> getTraits() {
        traits
    }

    Closure getConfig() {
        config
    }

    void traits(Class... traits) {
        this.traits.addAll(traits)
    }

    void config(@DelegatesTo(Edge) Closure config) {
        this.config = config
    }

    void applyToGraphAndEdge(Graph graph, Edge edge) {
        if(one) {
            graph.vertex(one)
            edge.one = one
        }
        if(two) {
            graph.vertex(two)
            edge.two = two
        }
        if(traits) {
            edge.delegateAs(this.traits as Class[])
        }
        if(config) {
            edge << config
        }
    }

    static EdgeSpec newInstance(Graph graph, @DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec spec = new EdgeSpec()

        Closure code = closure.rehydrate(spec, graph, graph)
        code()

        spec
    }

    static EdgeSpec newInstance(Map<String, ?> map) {
        EdgeSpec spec = new EdgeSpec(one:map.one, two:map.two)
        if(map.traits) {
            spec.traits(map.traits as Class[])
        }
        if(map.config) {
            spec.config(map.config)
        }

        spec
    }
}
