package graph.type.directed

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.type.VertexSpec
import graph.type.undirected.VertexSpecCodeRunner

class DirectedVertexSpec extends VertexSpec {
    private final Set<String> connectsFromSet = [] as Set<String>

    DirectedVertexSpec() {

    }

    DirectedVertexSpec(Map<String, ?> map) {
        super(map)

        map.connectsFrom?.each {
            connectsFrom(it instanceof NameSpec ? it.name : it)
        }
    }

    /**
     * The set of edges to create between the {@link graph.Vertex} and other vertices. The {@link graph.Vertex} will be edge.two.
     * @return The names of vertices the {@link graph.Vertex} should connect to.
     */
    Set<String> getConnectsFrom() {
        Collections.unmodifiableSet(connectsFromSet)
    }

    /**
     * Adds to the names the {@link graph.Vertex} should connect to. In the resulting edge the vertex named by this spec
     * will be edge.two.
     * @param names
     */
    void connectsFrom(String... names) {
        connectsFromSet.addAll(names)
    }

    Vertex apply(Graph graph) {
        if (!name) {
            throw new IllegalArgumentException('!name failed. Name must be groovy truth.')
        }
        Vertex vertex = graph.vertices[name] ?: graph.vertexFactory.newVertex(name)
        graph.addVertex(vertex)

        if (rename) {
            graph.rename(name, rename)
        }
        if (traits) {
            vertex.delegateAs(traits as Class[])
        }
        connectsTo.each {
            graph.edge vertex.name, it
        }
        connectsFromSet.each {
            graph.edge it, vertex.name
        }

        if (runnerCode) {
            VertexSpecCodeRunner runner = new DirectedVertexSpecCodeRunner(graph: graph, vertex: vertex)
            runner.runCode(runnerCode)
        }

        vertex
    }

    VertexSpec overlay(DirectedVertexSpec spec) {
        VertexSpec next = new DirectedVertexSpec()
        next.name = spec.name ?: name
        next.rename = spec.rename ?: rename

        next.traits((traits + spec.traits) as Class[])
        next.connectsTo((connectsTo + spec.connectsTo) as String[])
        next.connectsFrom((connectsFromSet + spec.connectsFrom) as String[])

        if (this.runnerCode) {
            next.runnerCode this.runnerCode << spec.runnerCode
        } else {
            next.runnerCode spec.runnerCode
        }
        next
    }
}
