package graph

import groovy.transform.PackageScope

/**
 * Specification class that helps vertex methods in {@link Graph} objects. It can be the delegate of closures used in
 * vertex methods of {@link Graph}.
 */
@PackageScope
class VertexSpec {
    /**
     * The name of the {@link Vertex} to create or update.
     */
    String name
    private Set<Class> traits = [] as Set<Class>
    private Set<String> connectsTo = [] as Set<String>
    private Closure config

    /**
     * @return the set of traits that should be applied to the {@link Vertex}
     */
    Set<Class> getTraits() {
        traits
    }

    /**
     * {@link Graph} will use this set to create edges between the {@link Vertex} and other vertices.
     * @return The names of vertices the {@link Vertex} should connect to.
     */
    Set<String> getConnectsTo() {
        connectsTo
    }

    /**
     * @return The config applied to the {@link Vertex} using its leftShift operator.
     */
    Closure getConfig() {
        config
    }

    /**
     * Adds to the set of traits to be applied to the {@link Vertex} {@link #getTraits()} is used to get the {@link Set}.
     * Traits will be applied to the {@link Vertex} before the {@link Vertex} is configured. {@link #config} can be
     * used to configure these traits.
     * @param classes traits to apply
     */
    void traits(Class... traits) {
        this.traits.addAll(traits)
    }

    /**
     * Adds to the names the {@link Vertex} should connect to.
     * @param names
     */
    void connectsTo(String... names) {
        connectsTo.addAll(names)
    }

    /**
     * Either appends to or sets the config member. This config is applied to the {@link Vertex} after traits are applied.
     * @param config added to the config member variable
     */
    void config(@DelegatesTo(Vertex) Closure config) {
        if(this.config) {
            this.config << config
        } else {
            this.config = config
        }
    }

    /**
     * Apply this specification to the given {@link Graph} and {@link Vertex}. First traits are applied. Then edges are
     * created from connectsTo. Finally the config is applied to the {@link Vertex} using the leftShift operator.
     * @param graph
     * @param vertex
     */
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

    /**
     * Creates a new instance of a VertexSpec using the provided closure. A new {@link VertexSpec} will be the delegate
     * while the {@link Graph} will be the owner and this.
     * @param graph
     * @param closure
     * @return the resulting VertexSpec
     */
    static VertexSpec newInstance(Graph graph, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = new VertexSpec()

        Closure code = closure.rehydrate(spec, graph, graph)
        code()

        spec
    }

    /**
     * creates a new instance of a VertexSpec using the provided Map. Valid values that can be in the Map are:
     * <p>
     * traits - list of traits to be applied to the {@link Vertex}<br>
     * connectsTo - list of vertices to connect the {@link Vertex} to.<br>
     * config - closure to be applied to the {@link Vertex} after traits and edges are created.
     * <p>
     * All other values are ignored.
     * @param map
     * @return
     */
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
