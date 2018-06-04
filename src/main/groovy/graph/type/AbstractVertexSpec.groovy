package graph.type

import graph.Graph
import graph.Vertex
import graph.VertexSpec

/**
 * Base implementation of a VertexSpec. Type packages can inherit this class to implement default methods in
 * VertexSpec.
 *
 * Add new dsl properties for Vertex dsl to dslProperties. This will prevent them from being added as entries to the
 * Vertex.
 */
abstract class AbstractVertexSpec extends VertexSpec {
    Vertex vertex
    Object key
    Object changeKey
    final Set<Object> connectsToSet = [] as Set<Object>
    List dslProperties
    Map entries
    Closure runnerCodeClosure

    protected AbstractVertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        super(graph)

        dslProperties = ['key', 'changeKey', 'connectsTo']

        entries = map.findAll { !(it.key in dslProperties)}

        if(map.containsKey('key') && !map.key) {
            throw new IllegalArgumentException('Invalid key')
        }
        key = map.key

        if(map.containsKey('changeKey') && !map.changeKey) {
            throw new IllegalArgumentException('Invalid containsKey')
        }
        changeKey = map.changeKey

        if(map.connectsTo && (map.connectsTo instanceof Collection || map.connectsTo.class.isArray())) {
            map.connectsTo.each {
                if(!it) {
                    throw new IllegalArgumentException('Invalid connectsTo item.')
                }
                connectsToSet.add(it)
            }
        } else if(map.connectsTo) {
            connectsToSet.add(map.connectsTo)
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
        vertex = graph.vertices[key] ?: graph.newVertex(key:key)
    }

    protected void initEntries() {
        vertex.putAll(entries)
    }

    protected void checkConditions() {
        if (!key) {
            throw new IllegalStateException('key is not set.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!vertex) {
            throw new IllegalStateException('vertex is not set.')
        }
        if (changeKey) {
            if (graph.vertices[changeKey]) {
                throw new IllegalStateException('renamed vertex already exists')
            }
        }
    }

    protected void applyChangeKey() {
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
        applyChangeKey()
        applyConnectsTo()
        initEntries()
        addVertex(vertex)
        applyClosure()
        vertex
    }
}
