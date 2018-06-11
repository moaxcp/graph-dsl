package graph

/**
 * Base implementation of a VertexSpec.
 *
 * Add new dsl properties for Vertex dsl to dslProperties. This will prevent them from being added as entries to the
 * Vertex.
 */
class VertexSpec {
    Graph graph
    Vertex vertex
    Object id
    Object changeId
    final Set<Object> connectsToSet = [] as Set<Object>
    final Set<Object> connectsFromSet = [] as Set<Object>
    List dslProperties
    Map entries
    Closure runnerCodeClosure

    protected VertexSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
        this.graph = graph

        dslProperties = ['id', 'changeId', 'connectsTo', 'connectsFrom']

        entries = map.findAll { !(it.key in dslProperties)}

        if(map.containsKey('id') && !map.id) {
            throw new IllegalArgumentException('Invalid id')
        }
        id = map.id

        if(map.containsKey('changeId') && !map.changeId) {
            throw new IllegalArgumentException('Invalid containsKey')
        }
        changeId = map.changeId

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

        if(map.connectsFrom && (map.connectsFrom instanceof Collection || map.connectsFrom.class.isArray())) {
            map.connectsFrom.each {
                if(!it) {
                    throw new IllegalArgumentException('Invalid connectsFrom item.')
                }
                connectsFromSet.add(it)
            }
        } else if(map.connectsFrom) {
            connectsFromSet.add(map.connectsFrom)
        }
        runnerCodeClosure = closure
    }

    protected void init() {
        if (vertex) {
            throw new IllegalStateException('vertex already created.')
        }
        if (!graph.vertices[id]) {
            id = changeId ?: id
            changeId = null
        }
        vertex = graph.vertices[id] ?: graph.newVertex(id:id)
    }

    protected void initEntries() {
        vertex.putAll(entries)
    }

    protected void checkConditions() {
        if (!id) {
            throw new IllegalStateException('id is not set.')
        }
        if (!graph) {
            throw new IllegalStateException('graph is not set.')
        }
        if (!vertex) {
            throw new IllegalStateException('vertex is not set.')
        }
        if (changeId && graph.vertices[changeId]) {
            throw new IllegalStateException('renamed vertex already exists')
        }
    }

    protected void applyChangeKey() {
        if (changeId) {
            graph.changeId(id, changeId)
        }
    }

    protected void applyConnectsTo() {
        connectsToSet.each {
            graph.edge vertex.id, it
        }
    }

    protected void applyConnectsFrom() {
        connectsFromSet.each {
            graph.edge it, vertex.id
        }
    }

    protected void applyClosure() {
        if (runnerCodeClosure) {
            VertexSpecCodeRunner runner = new VertexSpecCodeRunner(graph, vertex)
            runner.runCode(runnerCodeClosure)
        }
    }

    Vertex apply() {
        init()
        checkConditions()
        applyChangeKey()
        applyConnectsTo()
        applyConnectsFrom()
        initEntries()
        graph.addVertex(vertex)
        applyClosure()
        vertex
    }
}
