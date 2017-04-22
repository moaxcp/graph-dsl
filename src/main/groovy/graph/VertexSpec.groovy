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
    private Set<String> edgesFirst = [] as Set<String>
    private Set<String> edgesSecond = [] as Set<String>
    private Closure config

    /**
     * The set of traits that should be applied to the {@link Vertex}.
     * @return
     */
    Set<Class> getTraits() {
        traits
    }

    /**
     * The set of edges to create between the {@link Vertex} and other vertices.
     * @return The names of vertices the {@link Vertex} should connect to.
     */
    Set<String> edgesFirst() {
        edgesFirst
    }

    /**
     * The config applied to the {@link Vertex} using its leftShift operator.
     * @return
     */
    Closure getConfig() {
        config
    }

    /**
     * Adds to the set of traits to be applied to the {@link Vertex}.
     * @param traits - added to the set
     */
    void traits(Class... traits) {
        this.traits.addAll(traits)
    }

    /**
     * Adds to the names the {@link Vertex} should connect to. In the resulting edge the vertex named by this spec
     * will be edge.one.
     * @param names
     */
    void edgesFirst(String... names) {
        edgesFirst.addAll(names)
    }

    /**
     * Adds to the names the {@link Vertex} should connect to. In the resulting edge the vertex named by this spec
     * will be edge.two.
     * @param names
     */
    void edgesSecond(String... names) {
        edgesSecond.addAll(names)
    }

    /**
     * Sets the config closure which will be run on the {@link Vertex}.
     * @param config added to the config member variable
     */
    void config(@DelegatesTo(Vertex) Closure config) {
        this.config = config
    }

    /**
     * Applies this {@link VertexSpec} to the {@link Vertex} and {@link Graph}. Members from this spec are applied in this order:
     * <p>
     * 1. renames vertex to name if set
     * 2. applies traits to the vertex
     * 3. connects the vertex vertices listed in edgesFirst set
     * 4. configures the vertex using vertex << config
     * @param graph
     * @param vertex
     */
    void applyToGraphAndVertex(Graph graph, Vertex vertex) {
        if(name) {
            graph.rename(vertex.name, name)
        }
        if (traits) {
            vertex.delegateAs(traits as Class[])
        }
        edgesFirst.each {
            graph.edge vertex.name, it
        }
        edgesSecond.each {
            graph.edge it, vertex.name
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
     * edgesFirst - list of vertices to connect the {@link Vertex} to.<br>
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
        if (map.edgesFirst) {
            spec.edgesFirst(map.edgesFirst as String[])
        }
        if(map.edgesSecond) {
            spec.edgesSecond(map.edgesSecond as String[])
        }
        if(map.config) {
            spec.config(map.config)
        }

        spec
    }
}
