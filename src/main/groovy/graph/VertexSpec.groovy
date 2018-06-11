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

    /**
     * Gets the graph the {@link Vertex} has been added to. This can be used inside the runnerCode to access the graph.
     * @return the graph the {@link Vertex} has been added to.
     */
    Graph getGraph() {
        graph
    }

    /**
     * Gets the {@link Vertex} that has been added. This can be used inside the runnerCode to access the vertex.
     * @return the vertex that has been added.
     */
    Vertex getVertex() {
        vertex
    }

    void setId(Object id) {
        changeId(id)
        this.id = id
    }

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

    /**
     * Changes the id for the vertex to changeId
     * @param newId
     */
    void changeId(Object newId) {
        graph.newVertexSpec([id:vertex.id, changeId:newId]).apply()
    }

    /**
     * Creates edges where the vertex is edge.one and each id in ids is edge.two.
     * @param vertices to connect to.
     */
    void connectsTo(Object... ids) {
        graph.newVertexSpec([id:vertex.id, connectsTo:ids]).apply()
    }

    void connectsTo(Object id, Closure closure) {
        graph.newVertexSpec([id:vertex.id, connectsTo:id]).apply()
        graph.newVertexSpec([id:id], closure).apply()
    }

    /**
     * Creates edges where the vertex is edge.two and each id in ids is edge.one.
     * @param ids of vetices to connect to.
     */
    void connectsFrom(Object... ids) {
        graph.newVertexSpec([id:vertex.id, connectsFrom:ids]).apply()
    }

    void connectsFrom(Object id, Closure closure) {
        graph.newVertexSpec([id:vertex.id, connectsFrom:id]).apply()
        graph.newVertexSpec([id:id], closure).apply()
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
            runnerCodeClosure.delegate = this
            runnerCodeClosure.resolveStrategy = Closure.DELEGATE_FIRST
            runnerCodeClosure()
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

    /**
     * calls method on vertex.
     * @param name
     * @param args
     * @return
     */
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        vertex.invokeMethod(name, args)
    }

    /**
     * returns property from vertex.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        if (vertex[name]) {
            vertex[name]
        } else {
            throw new MissingPropertyException("Missing $name")
        }
    }

    /**
     * sets property on vertex.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        vertex[name] = value
    }
}
