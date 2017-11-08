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
    Object key
    Object changeKey
    final Set<Object> connectsToSet = [] as Set<Object>
    Closure runnerCodeClosure

    protected AbstractVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph)

        key = map.key
        changeKey = map.changeKey instanceof NameSpec ? map.changeKey.name : map.changeKey

        map.connectsTo?.each {
            connectsToSet.add(it instanceof NameSpec ? it.name : it)
        }
        runnerCodeClosure = closure
    }

    protected void init() {
        if (vertex) {
            throw new IllegalStateException('vertex already created.')
        }
        if (!graph.vertices[key]) {
            key = changeKey ?: key
            changeKey = null
        }
        vertex = graph.vertices[key] ?: graph.newVertex(key)
    }

    protected void checkConditions() {
        if (!key) {
            throw new IllegalStateException('!key failed. Name must be groovy truth.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!vertex) {
            throw new IllegalStateException('already ran spec')
        }
        if (changeKey) {
            if (graph.vertices[changeKey]) {
                throw new IllegalStateException('renamed vertex already exists')
            }
        }
    }

    protected void applyRename() {
        if (changeKey) {
            graph.changeKey(key, changeKey)
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
