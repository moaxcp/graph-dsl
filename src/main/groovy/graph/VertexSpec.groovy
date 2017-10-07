package graph

import graph.type.undirected.VertexSpecCodeRunner

/**
 * Specification class that helps vertex methods in {@link Graph} objects. GraphVertexSpec is used to collect the details
 * of an update or create.
 */
class VertexSpec {
    private Graph graph
    private Vertex vertex
    private String name
    private String rename
    private final Set<Class> traitsSet = [] as Set<Class>
    final Set<String> connectsToSet = [] as Set<String>
    private Closure runnerCodeClosure

    VertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        if (this.graph) {
            throw new IllegalArgumentException('Graph already set.')
        }
        this.graph = graph

        name = map.name
        rename = map.rename instanceof NameSpec ? map.rename.name : map.rename

        map.traits?.each{
            traitsSet.add((Class) it)
        }

        map.connectsTo?.each {
            connectsToSet.add((String) (it instanceof NameSpec ? it.name : it))
        }
        runnerCodeClosure = closure
    }

    protected Graph getGraph() {
        return graph
    }

    protected Vertex getVertex() {
        return vertex
    }

    protected Closure getRunnerCodeClosure() {
        return runnerCodeClosure
    }

    void init() {
        if(!graph.vertices[name]) {
            name = rename ?: name
            rename = null
        }
        vertex = graph.vertices[name] ?: graph.newVertex(name)
    }

    protected void checkConditions() {
        if (!name) {
            throw new IllegalStateException('!name failed. Name must be groovy truth.')
        }
        if(!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if(!vertex) {
            throw new IllegalStateException("already ran spec")
        }
        if(rename) {
            if(graph.vertices[rename]) {
                throw new IllegalStateException('renamed vertex already exists')
            }
        }
    }

    void applyRename() {
        if(rename) {
            graph.rename(name, rename)
        }
    }

    void applyTraits() {
        if (traitsSet) {
            vertex.delegateAs(traitsSet as Class[])
        }
    }

    void applyConnectsTo() {
        connectsToSet.each {
            graph.edge vertex.name, it
        }
    }

    void applyClosure() {
        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }
    }

    protected void addVertex(Vertex vertex) {
        graph.addVertex(vertex)
    }

    Vertex apply() {
        init()
        checkConditions()
        applyRename()
        applyTraits()
        applyConnectsTo()
        addVertex(vertex)
        applyClosure()
        vertex
    }
}
