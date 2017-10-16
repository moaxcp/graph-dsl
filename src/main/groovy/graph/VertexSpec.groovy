package graph

import graph.type.undirected.VertexSpecCodeRunner

/**
 * Specification class that helps vertex methods in {@link Graph} objects. GraphVertexSpec is used to collect the details
 * of an update or create.
 */
abstract class VertexSpec {
    Graph graph
    Vertex vertex
    private String name
    private String rename
    private final Set<String> connectsToSet = [] as Set<String>
    Closure runnerCodeClosure

    VertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        if (this.graph) {
            throw new IllegalArgumentException('Graph already set.')
        }
        this.graph = graph

        name = map.name
        rename = map.rename instanceof NameSpec ? map.rename.name : map.rename

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

    protected void init() {
        if(vertex) {
            throw new IllegalStateException('vertex already created.')
        }
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

    protected void applyRename() {
        if(rename) {
            graph.rename(name, rename)
        }
    }

    protected void applyConnectsTo() {
        connectsToSet.each {
            graph.edge vertex.name, it
        }
    }

    protected abstract void applyClosure()

    protected void addVertex(Vertex vertex) {
        graph.addVertex(vertex)
    }

    Vertex apply() {
        init()
        checkConditions()
        applyRename()
        applyConnectsTo()
        addVertex(vertex)
        applyClosure()
        vertex
    }
}
