package graph

class VertexSpec {
    String name
    private Set<Class> traits = [] as Set<Class>
    private Set<String> connectsTo = [] as Set<String>
    private Closure config

    Set<Class> getTraits() {
        traits
    }

    Set<String> getConnectsTo() {
        connectsTo
    }

    String getName() {
        name
    }

    Closure getConfig() {
        config
    }

    void traits(Class... classes) {
        traits.addAll(classes)
    }

    void connectsTo(String... names) {
        connectsTo.addAll(names)
    }

    void config(@DelegatesTo(Vertex) Closure config) {
        if(this.config) {
            this.config << config
        } else {
            this.config = config
        }
    }

    void applyToGraphAndVertex(Graph graph, Vertex vertex) {
        if (traits) {
            vertex.delegateAs(traits as Class[])
        }
        connectsTo.each {
            graph.edge vertex.name, it
        }
        if(config) {
            vertex << config
        }
    }

    static VertexSpec newInstance(Graph graph, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = new VertexSpec()

        Closure code = closure.rehydrate(spec, graph, graph)
        code()

        spec
    }

    static VertexSpec newInstance(Map<String, ?> map) {
        VertexSpec spec = new VertexSpec(name:map.name)
        if (map.traits) {
            spec.traits(map.traits as Class[])
        }
        if (map.connectsTo) {
            spec.connectsTo(map.connectsTo as String[])
        }
        if(map.config) {
            spec.config(map.config)
        }

        spec
    }
}
