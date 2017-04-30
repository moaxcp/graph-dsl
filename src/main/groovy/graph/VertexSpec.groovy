package graph

import groovy.transform.PackageScope

/**
 * Specification class that helps vertex methods in {@link Graph} objects. VertexSpec is used to collect the details
 * of an update or create.
 */
@PackageScope
class VertexSpec {

    /**
     * The name of the {@link Vertex} to create or update.
     */
    String name

    /**
     * The new name to give a {@link Vertex}
     */
    String rename

    private Set<Class> traits = [] as Set<Class>
    private Set<String> edgesFirst = [] as Set<String>
    private Set<String> edgesSecond = [] as Set<String>
    private Closure runnerCode

    /**
     * The set of traits that should be applied to the {@link Vertex}.
     * @return
     */
    Set<Class> getTraits() {
        traits
    }

    /**
     * The set of edges to create between the {@link Vertex} and other vertices. The {@link Vertex} will be edge.one.
     * @return The names of vertices the {@link Vertex} should connect to.
     */
    Set<String> getEdgesFirst() {
        edgesFirst
    }

    /**
     * The set of edges to create between the {@link Vertex} and other vertices. The {@link Vertex} will be edge.two.
     * @return The names of vertices the {@link Vertex} should connect to.
     */
    Set<String> getEdgesSecond() {
        edgesSecond
    }

    /**
     * The runnerCode applied to the {@link Vertex} using its leftShift operator.
     * @return
     */
    Closure getRunnerCode() {
        this.runnerCode
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
     * Sets the runnerCode closure which will be run on the {@link Vertex}.
     * @param runnerCode added to the runnerCode member variable
     */
    void runnerCode(@DelegatesTo(Vertex) Closure runnerCode) {
        this.runnerCode = runnerCode
    }

    /**
     * Applies this {@link VertexSpec} to the {@link Vertex} and {@link Graph}. Members from this spec are applied in this order:
     * <p>
     * 1. renames vertex to rename if set
     * 2. applies traits to the vertex
     * 3. connects the vertex vertices listed in edgesFirst set
     * @param graph
     */
    void apply(Graph graph, Vertex vertex) {
        if(rename) {
            graph.rename(name, rename)
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
    }

    /**
     * Creates a new instance of a VertexSpec using the provided closure. A new {@link VertexSpec} will be the delegate
     * while the {@link Graph} will be the owner and this.
     * @param graph
     * @param closure
     * @return the resulting VertexSpec
     */
    static VertexSpec newInstance(@DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = new VertexSpec()

        Closure code = closure.rehydrate(spec, closure.owner, closure.getThisObject())
        code()

        spec
    }

    /**
     * creates a new instance of a VertexSpec using the provided Map. Valid values that can be in the Map are:
     * <p>
     * name - the name of the vertex to create or update<br>
     * rename - what to rename the vertex<br>
     * traits - list of traits to be applied to the {@link Vertex}<br>
     * getEdgesFirst - list of vertices to connect the {@link Vertex} to.<br>
     * runnerCode - closure to be applied to the {@link Vertex} after traits and edges are created.
     * <p>
     * All other values are ignored.
     * @param map
     * @return
     */
    static VertexSpec newInstance(Map<String, ?> map) {
        VertexSpec spec = new VertexSpec(name:map.name, rename:map.rename)
        if (map.traits) {
            spec.traits(map.traits as Class[])
        }
        if (map.edgesFirst) {
            spec.edgesFirst(map.edgesFirst as String[])
        }
        if(map.edgesSecond) {
            spec.edgesSecond(map.edgesSecond as String[])
        }
        if(map.runnerCode) {
            spec.runnerCode(map.runnerCode)
        }

        spec
    }

    /**
     * Creates a new {@link VertexSpec} from members in this {@link VertexSpec} and the spec param. Members in the spec
     * param override the members in this. The runnerCode closure is appended if set.
     * @param spec
     * @return A new spec
     */
    VertexSpec overlay(VertexSpec spec) {
        VertexSpec next = new VertexSpec()
        if(spec.name) {
            next.name = spec.name
        } else {
            next.name = name
        }

        next.rename = spec.rename

        next.traits ((traits + spec.traits) as Class[])
        next.edgesFirst ((edgesFirst + spec.edgesFirst) as String[])
        next.edgesSecond ((edgesSecond + spec.edgesSecond) as String[])

        if(this.runnerCode) {
            next.runnerCode this.runnerCode << spec.runnerCode
        } else {
            next.runnerCode spec.runnerCode
        }
        next
    }
}
