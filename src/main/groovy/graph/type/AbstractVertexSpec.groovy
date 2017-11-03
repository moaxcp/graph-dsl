package graph.type

import graph.Graph
import graph.NameSpec
import graph.Vertex
import graph.VertexSpec

/**
 * Base implementation of a VertexSpec. Type packages can inherit this class to implement default methods in
 * VertexSpec.
 */
abstract class AbstractVertexSpec extends VertexSpec {
    Vertex vertex
    String name
    String rename
    final Set<String> connectsToSet = [] as Set<String>
    Closure runnerCodeClosure

    protected AbstractVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph)

        name = map.name
        rename = map.rename instanceof NameSpec ? map.rename.name : map.rename

        map.connectsTo?.each {
            connectsToSet.add((String) (it instanceof NameSpec ? it.name : it))
        }
        runnerCodeClosure = closure
    }

    protected void init() {
        if (vertex) {
            throw new IllegalStateException('vertex already created.')
        }
        if (!graph.vertices[name]) {
            name = rename ?: name
            rename = null
        }
        vertex = graph.vertices[name] ?: graph.newVertex(name)
    }

    protected void checkConditions() {
        if (!name) {
            throw new IllegalStateException('!name failed. Name must be groovy truth.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!vertex) {
            throw new IllegalStateException('already ran spec')
        }
        if (rename) {
            if (graph.vertices[rename]) {
                throw new IllegalStateException('renamed vertex already exists')
            }
        }
    }

    protected void applyRename() {
        if (rename) {
            graph.rename(name, rename)
        }
    }

    protected void applyConnectsTo() {
        connectsToSet.each {
            graph.edge vertex.key, it
        }
    }

    protected abstract void applyClosure()

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
