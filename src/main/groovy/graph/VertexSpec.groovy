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
        Collections.unmodifiableSet(edgesFirst)
    }

    /**
     * The set of edges to create between the {@link Vertex} and other vertices. The {@link Vertex} will be edge.two.
     * @return The names of vertices the {@link Vertex} should connect to.
     */
    Set<String> getEdgesSecond() {
        Collections.unmodifiableSet(edgesSecond)
    }

    /**
     * The runnerCode. This will be run against a VertexSpecCodeRunner
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
     * Sets the runnerCode closure.
     * @param runnerCode
     */
    void runnerCode(Closure runnerCode) {
        this.runnerCode = runnerCode
    }

    /**
     * Applies this {@link VertexSpec} to the {@link Vertex} and {@link Graph}. Members from this spec are applied in this order:
     * <p>
     * 1. renames vertex to rename if set<br>
     * 2. applies traits to the vertex<br>
     * 3. creates edges between the vertex and edgesFirst where the vertex is edge.one<br>
     * 4. creates edges between the vertex and edgesFirst where the vertex is edge.one<br>
     * @param graph
     */
    Vertex apply(Graph graph) {
        if(!name) {
            throw new IllegalArgumentException("!name failed. Name must be groovy truth.")
        }
        Vertex vertex = graph.vertices[name] ?: graph.vertexFactory.newVertex(name)
        graph.addVertex(vertex)

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

        if(runnerCode) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph:graph, vertex:vertex)
            runner.runCode(runnerCode)
        }

        vertex
    }

    /**
     * creates a new instance of a VertexSpec using the provided Map. Valid values that can be in the Map are:
     * <p>
     * name - the name of the vertex to create or update<br>
     * rename - what to rename the vertex<br>
     * traits - list of traits to be applied to the {@link Vertex}<br>
     * edgesFirst - list of vertices to connect the {@link Vertex} to. The vertex is edge.one<br>
     * edgesSecond - list of vertices to connect the {@link Vertex} to. The vertex is edge.two<br>
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
