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
}
