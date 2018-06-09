package graph

import graph.plugin.Plugin
import graph.type.Type
import graph.type.undirected.GraphType
import groovy.transform.PackageScope
import static TraversalColor.*

/**
 * An implementation of a Graph. A {@link Vertex} is identified in this Graph using the vertex name property. A
 * {@link Edge} is identified by the names of the to vertices it connects.
 * <p>
 * {@link Vertex} and {@link Edge} objects are added using the vertex and edge methods. These methods will always create
 * objects if not present to reduce the code needed to express a graph and to ensure the integrity of the graph. There
 * are a few styles that may be used to express vertices and edges in this Graph. See the edge and vertex methods for
 * more details.
 * <p>
 * All graphs have a {@link graph.type.Type}. All Vertex, Edge, VertexSpec, and EdgeSpec objects must be created by
 * the Type. This is to ensure the {@link Graph} always has the behavior specified by the {@link graph.type.Type}.
 * <p>
 * The default behavior is that of an undirected graph. This is implemented by {@link GraphType}.
 */
class Graph implements VertexDsl, EdgeDsl, TraversalDsl {
    private Map<Object, ? extends Vertex> vertices = [:] as LinkedHashMap<Object, ? extends Vertex>
    private Set<? extends Edge> edges = [] as LinkedHashSet<? extends Edge>
    private Type type
    private Set<? extends Plugin> plugins = [] as LinkedHashSet<? extends Plugin>

    Graph() {
        type = new GraphType()
        type.graph = this
    }

    /**
     * static entry point to the dsl.
     * @param c closure to execute with graph as the delegate
     * @return the resulting graph
     */
    static Graph graph(@DelegatesTo(Graph) Closure c) {
        Graph graph = new Graph()
        graph.with(c)
        graph
    }

    /**
     * returns the vertices as an unmodifiableMap.
     * @return vertices as an unmodifiableMap
     */
    Map<Object, ? extends Vertex> getVertices() {
        Collections.unmodifiableMap(vertices)
    }

    Vertex getVertex(Object id) {
        vertices[id]
    }

    /**
     * Removes the {@link Vertex} from vertices with the matching id. If the Vertex has adjacentEdges it cannot be
     * deleted an IllegalStateException will be thrown.
     * @param id id of {@link Vertex} to delete from this graph
     * @throws IllegalStateException if id vertex has adjacentEdges.
     * @see {@link Graph#adjacentEdges(Object)}
     */
    void delete(Object id) {
        if (adjacentEdges(id)) {
            throw new IllegalStateException(
                    "Cannot delete $id. There are edges that connect to it. Try deleting those first."
            )
        }
        vertices.remove(id)
    }

    /**
     * returns the edges as an unmodifiable set
     * @return edges as an unmodifiable set
     */
    Set<? extends Edge> getEdges() {
        Collections.unmodifiableSet(edges)
    }

    /**
     * Adds an edge object directly.
     * @param edge to add
     * @return true if add was successful.
     */
    @PackageScope
    boolean addEdge(Edge edge) {
        edges.add(edge)
    }

    /**
     * Replaces edges with results of running edges.collect(closure)
     * @param closure to run on each edge
     */
    void replaceEdges(Closure closure) {
        List replace = edges.collect(closure)
        edges.clear()
        edges.addAll(replace)
    }

    /**
     * Replaces set of edges used by Graph with the given set.
     * @param set to replace set of edges
     */
    void replaceEdgesSet(Set<? extends Edge> set) {
        if (!set.empty) {
            throw new IllegalArgumentException('set must be empty.')
        }
        set.addAll(edges)
        edges = set
    }

    /**
     * Replaces entries in vertices {@link Map}. The closure should return map entries for each entry passed in.
     * @param closure returns replaced entries.
     */
    void replaceVertices(Closure closure) {
        Map<String, ? extends Vertex> replace = vertices.collectEntries(closure) as Map<String, Vertex>
        vertices.clear()
        vertices.putAll(replace)
    }

    /**
     * Replaces vertices {@link Map}. The original entries are added to the new {@link Map} object.
     * @param map
     */
    void replaceVerticesMap(Map<String, ? extends Vertex> map) {
        if (!map.isEmpty()) {
            throw new IllegalArgumentException('map must be empty.')
        }
        map.putAll(vertices)
        vertices = map
    }

    /**
     * Removes an {@link Edge} from edges If a type changes the definition of an edge, for example to
     * {@link graph.type.directed.DirectedEdge}, this method will still work as expected. It will remove the edge where
     * edge.one == one and edge.two == two. Keep in mind, in the case of the base {@link Edge} object edge.one can also
     * equal two and edge.two can also equal one.
     * @param one id of first vertex
     * @param two id of second vertex
     */
    void deleteEdge(Object one, Object two) {
        edges.remove(type.newEdge(one: one, two: two))
    }

    /**
     * Converts this Graph to the {@link Type} passed in.
     * @param typeClass type to convert Graph into
     */
    void type(Class typeClass) {
        if (!Type.isAssignableFrom(typeClass)) {
            throw new IllegalArgumentException("$typeClass.name does not implement Type")
        }
        type = (Type) typeClass.newInstance()
        type.graph = this
        type.convert()
    }

    /**
     * Searches for a properties file on the classpath using "/META-INF/graph-types/${typeName}.properties". A
     * property 'implementation-class' is used to find the {@link Type} class. The class is used to convert this graph.
     * @param typeName name of type to convert graph
     */
    void type(String typeName) {
        Properties properties = new Properties()
        properties.load(getClass().getResourceAsStream("/META-INF/graph-types/${typeName}.properties"))
        type(this.class.classLoader.loadClass((String) properties.'implementation-class'))
    }

    /**
     * Applies a {@link Plugin} to this Graph.
     * @param pluginClass plugin to apply
     */
    void plugin(Class pluginClass) {
        if (!Plugin.isAssignableFrom(pluginClass)) {
            throw new IllegalArgumentException("$pluginClass.name does not implement Plugin")
        }
        Plugin plugin = (Plugin) pluginClass.newInstance()
        plugin.graph = this
        plugins.add plugin
    }

    /**
     * Searches for a properties file on the classpath using "/META-INF/graph-plugins/${pluginName}.properties". A
     * property 'implementation-class' is used to find the {@link Plugin} class. The plugin class is applied to this
     * graph.
     * @param pluginName name of plugin to apply
     */
    void plugin(String pluginName) {
        Properties properties = new Properties()
        properties.load(getClass().getResourceAsStream("/META-INF/graph-plugins/${pluginName}.properties"))
        plugin(this.class.classLoader.loadClass((String) properties.'implementation-class'))
    }

    /**
     * Returns the type of this Graph.
     * @return the type of this graph
     */
    Type getType() {
        type
    }

    /**
     * Adds a vertex object directly. For internal use to create copies of a Graph.
     * @param vertex
     * @return true if add was successful.
     */
    @PackageScope
    boolean addVertex(Vertex vertex) {
        vertices[vertex.id] = vertex
    }

    /**
     * Replaces the id of a {@link Vertex}. All edges connecting the {@link Vertex} are updated with the new id.
     * @param id of original vertex
     * @param newId for updated vertex
     */
    void changeId(Object id, Object newId) {
        if (!id || !newId) {
            throw new IllegalArgumentException('id or newId is null or empty.')
        }
        Vertex vertex = vertex(id)
        vertices.remove(vertex.id)
        vertex.id = newId
        vertices[(Object) vertex.id] = vertex
        adjacentEdges(id).each {
            if (it.one == id) {
                it.one = newId
            }
            if (it.two == id) {
                it.two = newId
            }
        }
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The closure contains configuration described in {@link #vertex(Object, Closure)}.
     * @param spec specification for vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(ConfigSpec spec) {
        type.newVertexSpec(spec.map, spec.closure).apply()
    }

    /**
     * Applies spec to this graph.
     * @param spec the specification for an {@link Edge}
     * @return the resulting {@link Edge}.
     */
    Edge edge(ConfigSpec spec) {
        type.newEdgeSpec(spec.map, spec.closure).apply()
    }

    /**
     * Returns the first unvisited vertex id in vertices.
     *
     * @param colors map of vertex id and TraversalColor entries.
     * @return the first unvisited vertex id in vertices.
     */
    Object getUnvisitedVertexId(Map colors) {
        vertices.find { k, v ->
            colors[(k)] != BLACK && colors[(k)] != GREY
        }?.key
    }

    /**
     * returns the first unvisited child id with a parent matching id.
     *
     * @param id the id of the parent vertex to start searching from
     * @param colors a map of vertex id and TraversalColor entries.
     * @return the first unvisited child id with a parent matching id.
     */
    Object getUnvisitedChildId(Object id, Map<Object, TraversalColor> colors) {
        Edge edge = traverseEdges(id).findAll {
            it.one != it.two
        }.find {
            Object childKey = id == it.one ? it.two : it.one
            TraversalColor color = colors[childKey]
            color != GREY && color != BLACK
        }

        if (!edge) {
            return null
        }
        id == edge.one ? edge.two : edge.one
    }

    /**
     * Finds adjacent edges for vertex with id.
     * @param id
     * @return set of adjacent edges.
     */
    Set<? extends Edge> adjacentEdges(Object id) {
        edges.findAll { Edge edge ->
            id == edge.one || id == edge.two
        }
    }

    /**
     * If the missing method is in the assigned {@link Type} the method will be called on type. Otherwise a
     * {@link ConfigSpec} is created and returned.
     * @param name
     * @param args
     * @return result of calling method on Type or a ConfigSpec
     */
    @SuppressWarnings('Instanceof')
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        MetaMethod method = type.metaClass.getMetaMethod(name, args)
        if (method != null) {
            return method.invoke(type, args)
        }

        def list = plugins.collect { plugin ->
            MetaMethod m = plugin.metaClass.getMetaMethod(name, args)
            if (m) {
                return [plugin, m]
            }
            null
        }.find { list ->
            list
        }

        if (list != null) {
            return list[1].invoke(list[0], args)
        }
        throw new MissingMethodException(name, Graph, args)
    }
}
