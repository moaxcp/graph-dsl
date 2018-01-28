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
class Graph implements GroovyInterceptable, VertexDsl, EdgeDsl, TraversalDsl {
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
     * @param c  closure to execute with graph as the delegate
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

    /**
     * Removes the {@link Vertex} from vertices with the matching key. If the Vertex has adjacentEdges it cannot be
     * deleted an IllegalStateException will be thrown.
     * @param key  key of {@link Vertex} to delete from this graph
     * @throws IllegalStateException if key vertex has adjacentEdges.
     * @see {@link Graph#adjacentEdges(Object)}
     */
    void delete(Object key) {
        if (adjacentEdges(key)) {
            throw new IllegalStateException(
                    "Cannot delete $key. There are edges that connect to it. Try deleting those first."
            )
        }
        vertices.remove(key)
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
     * @param  edge to add
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
     * @param closure  returns replaced entries.
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
     * @param one key of first vertex
     * @param two key of second vertex
     */
    void deleteEdge(Object one, Object two) {
        edges.remove(type.newEdge(one:one, two:two))
    }

    /**
     * Converts this Graph to the {@link Type} passed in.
     * @param typeClass  type to convert Graph into
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
     * @param typeName  name of type to convert graph
     */
    void type(String typeName) {
        Properties properties = new Properties()
        properties.load(getClass().getResourceAsStream("/META-INF/graph-types/${typeName}.properties"))
        type(this.class.classLoader.loadClass((String) properties.'implementation-class'))
    }

    /**
     * Applies a {@link Plugin} to this Graph.
     * @param pluginClass  plugin to apply
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
     * @param pluginName  name of plugin to apply
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
        vertices[vertex.key] = vertex
    }

    /**
     * Replaces the key of a {@link Vertex}. All edges connecting the {@link Vertex} are updated with the new key.
     * @param key  of original vertex
     * @param newKey  for updated vertex
     */
    void changeKey(Object key, Object newKey) {
        if (!key || !newKey) {
            throw new IllegalArgumentException('key or newKey is null or empty.')
        }
        Vertex vertex = vertex(key)
        vertices.remove(vertex.key)
        vertex.key = newKey
        vertices[(Object) vertex.key] = vertex
        adjacentEdges(key).each {
            if (it.one == key) {
                it.one = newKey
            }
            if (it.two == key) {
                it.two = newKey
            }
        }
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The closure contains configuration described in {@link #vertex(Object,Closure)}.
     * @param spec  specification for vertex
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
     * Returns the first unvisited vertex key in vertices.
     *
     * @param colors  map of vertex key and TraversalColor entries.
     * @return the first unvisited vertex key in vertices.
     */
    Object getUnvisitedVertexKey(Map colors) {
        vertices.find { k, v ->
            colors[(k)] != BLACK && colors[(k)] != GREY
        }?.key
    }

    /**
     * returns the first unvisited child key with a parent matching key.
     *
     * @param key  the key of the parent vertex to start searching from
     * @param colors  a map of vertex key and TraversalColor entries.
     * @return the first unvisited child key with a parent matching key.
     */
    Object getUnvisitedChildKey(Object key, Map<Object, TraversalColor> colors) {
        Edge edge = traverseEdges(key).findAll {
            it.one != it.two
        }.find {
            Object childKey = key == it.one ? it.two : it.one
            TraversalColor color = colors[childKey]
            color != GREY && color != BLACK
        }

        if (!edge) {
            return null
        }
        key == edge.one ? edge.two : edge.one
    }

    /**
     * Finds adjacent edges for vertex with key.
     * @param key
     * @return set of adjacent edges.
     */
    Set<? extends Edge> adjacentEdges(Object key) {
        edges.findAll { Edge edge ->
            key == edge.one || key == edge.two
        }
    }

    Set<? extends Edge> traverseEdges(Object key) {
        type.traverseEdges(key)
    }

    /**
     * Creates and returns a color map in the form of name:color where name is the vertex name and color is
     * TraversalColor.WHITE.
     * @return
     */
    Map makeColorMap() {
        vertices.collectEntries { key, vertex ->
            [(key):WHITE]
        }
    }

    /**
     * configures a depth first traversal with the given closure using {@link #depthFirstTraversalSpec(String,Closure)}.
     * Once the spec is configured {@link #traversal(Closure,TraversalSpec)} is called.
     * @param root  optional root to start traversal
     * @param specClosure  closure for depthFirstTraversalSpec method
     * @return result of the traversal
     */
    Traversal depthFirstTraversal(String root, @DelegatesTo(DepthFirstTraversalSpec) Closure specClosure) {
        DepthFirstTraversalSpec spec = depthFirstTraversalSpec(root, specClosure)
        traversal(this.&depthFirstTraversalConnected, spec)
    }

    Traversal depthFirstTraversal(@DelegatesTo(DepthFirstTraversalSpec) Closure specClosure) {
        DepthFirstTraversalSpec spec = depthFirstTraversalSpec(specClosure)
        traversal(this.&depthFirstTraversalConnected, spec)
    }

    /**
     * Creates a DepthFirstTraversalSpec from the provided closure. If root is set spec.root will be set before calling
     * the closure. Defaults will be configured with the setupSpec method after the closure is called.
     * @param root  optional root to start traversal
     * @param specClosure   A closure that has a new DepthFirstTraversalSpec as a delegate. Modify the
     * DepthFirstTraversalSpec in this closure to change the behavior of the depth first traversal.
     * @return resulting specification
     */
    private DepthFirstTraversalSpec depthFirstTraversalSpec(String root = null,
                                                            @DelegatesTo(DepthFirstTraversalSpec) Closure specClosure) {
        DepthFirstTraversalSpec spec = new DepthFirstTraversalSpec()
        spec.root = root
        specClosure.delegate = spec
        specClosure()
        setupSpec(spec)
        spec
    }

    /**
     * Configures defaults for a TraversalSpec. When colors and root are not set This method will generate defaults. If
     * colors is not defined in the spec it defaults to the result of {@link #makeColorMap()}. If root is not defined
     * in the spec it defaults to the result of calling {@link #getUnvisitedVertexKey(Map)} with spec.colors.
     * @param spec the {@link TraversalSpec} to configure with defaults.
     */
    private void setupSpec(TraversalSpec spec) {
        spec.colors = spec.colors ?: makeColorMap()
        spec.root = spec.root ?: getUnvisitedVertexKey(spec.colors)
    }

    /**
     * Creates a BreadthFirstTraversalSpec from the provided closure. If root is set spec.root will be set before
     * calling the closure. Defaults will be configured with the setupSpec method after the closure is called.
     * @param root  optional root to start traversal
     * @param specClosure   A closure that has a new BreadthFirstTraversalSpec as a delegate. Modify the
     * BreadthFirstTraversalSpec in this closure to change the behavior of the depth first traversal.
     * @return resulting specification
     */
    private BreadthFirstTraversalSpec breadthFirstTraversalSpec(String root = null,
                                                                @DelegatesTo(BreadthFirstTraversalSpec) Closure specClosure) {
        BreadthFirstTraversalSpec spec = new BreadthFirstTraversalSpec()
        spec.root = root
        specClosure.delegate = spec
        specClosure()
        setupSpec(spec)
        spec
    }

    /**
     * Performs a traversal with the given traversalConnected method and TraversalSpec on all
     * components of the graph. This method calls traversalConnected on spec.root
     * and continues to call traversalConnected until all vertices are colored black.
     * To stop the traversal early the spec can return Traversal.STOP in any of the
     * traversal closures.
     * @param traversalConnected  one of the traversalConnected methods in this graph
     * @param spec
     * @return null or a Traversal value
     */
    protected Traversal traversal(traversalConnected, TraversalSpec spec) {
        spec.roots = [] as Set
        while (spec.root) {
            Traversal traversal = traversalConnected(spec)
            if (traversal == Traversal.STOP) {
                return Traversal.STOP
            }
            spec.roots << spec.root
            spec.root = getUnvisitedVertexKey(spec.colors)
        }
        null
    }

    /**
     * Performs a depth first traversal on a connected component of the graph starting
     * at the vertex identified by root. The behavior of the traversal is determined by
     * spec.colors, spec.preorder, and spec.postorder.
     *
     * Traversal.STOP - It is possible to stop the traversal early by returning this value
     * in preorder and postorder.
     * @param spec the DepthFirstTraversalSpec
     * @return null or a Traversal value
     */
    private Traversal depthFirstTraversalConnected(DepthFirstTraversalSpec spec) {
        Object root = spec.root
        if (spec.preorder && spec.preorder(vertices[root]) == Traversal.STOP) {
            spec.colors[root] = GREY
            return Traversal.STOP
        }
        spec.colors[root] = GREY

        Set<Edge> adjacentEdges = traverseEdges(root)
        for (int index = 0; index < adjacentEdges.size(); index++) { //cannot stop and each() call on adjacentEdges
            Edge edge = adjacentEdges[index]
            String connectedName = root == edge.one ? edge.two : edge.one
            if (spec.classifyEdge && spec.classifyEdge(edge, root, connectedName,
                    spec.colors[connectedName]) == Traversal.STOP) {
                return Traversal.STOP
            }
            if (spec.colors[connectedName] == WHITE) {
                spec.root = connectedName
                if (Traversal.STOP == depthFirstTraversalConnected(spec)) {
                    return Traversal.STOP
                }
            }
        }

        if (spec.postorder && spec.postorder(vertices[root]) == Traversal.STOP) {
            spec.colors[root] = BLACK
            return Traversal.STOP
        }
        spec.colors[root] = BLACK
        null
    }

    /**
     * executes closure on each {@link Vertex} in breadth first order starting at the given root {@link Vertex}. See
     * {@link #breadthFirstTraversal} for details.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     */
    void eachBfs(String root = null, Closure closure) {
        breadthFirstTraversal {
            delegate.root = root
            visit { vertex ->
                closure(vertex)
                null
            }
        }
    }

    /**
     * Executes closure on each {@link Vertex} in breadth first order starting at root. If the closure returns true the
     * {@link Vertex} is returned.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return first {@link Vertex} where closure returns true
     */
    Vertex findBfs(String root = null, Closure closure) {
        Vertex result = null
        breadthFirstTraversal {
            delegate.root = root
            visit { vertex ->
                if (closure(vertex)) {
                    result = vertex
                    return Traversal.STOP
                }
            }
        }
        result
    }

    /**
     * Executes closure on each vertex in breadth first order starting at root. object is the initial value passed to
     * the closure. Each returned value from the closure is passed to the next call.
     * @param root  vertex to start breadth first traversal
     * @param object  initial value passed to the closure
     * @param closure  execute on each {@link Vertex}
     * @return object returned from the final call to closure.
     */
    Object injectBfs(String root = null, Object object, Closure closure) {
        Object result = object
        breadthFirstTraversal {
            delegate.root = root
            visit { vertex ->
                result = closure(result, vertex)
            }
        }
        result
    }

    /**
     * Runs closure on each vertex in breadth first order starting at root. The vertices where closure returns true are
     * returned.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return the vertices where closure returns true
     */
    List<? extends Vertex> findAllBfs(String root = null, Closure closure) {
        Closure inject = null
        if (root) {
            inject = this.&injectBfs.curry(root)
        } else {
            inject = this.&injectBfs
        }
        (List<? extends Vertex>) inject([]) { result, vertex ->
            if (closure(vertex)) {
                result << vertex
            }
            result
        }
    }

    /**
     * Runs closure on each vertex in breadth first order, starting at root, collecting returned values from the
     * closure.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return values returned from each execution of closure
     */
    List<?> collectBfs(String root = null, Closure closure) {
        Closure inject = null
        if (root) {
            inject = this.&injectBfs.curry(root)
        } else {
            inject = this.&injectBfs
        }
        (List<? extends Vertex>) inject([]) { result, vertex ->
            result << closure(vertex)
        }
    }

    /**
     * configures a breadth first traversal with the given closure using breadthFirstTraversalSpec(). Once the spec is
     * configured traversal(this.&breadthFirstTraversalConnected, spec) is called.
     * @param root  optional root to start traversal.
     * @param specClosure
     * @return
     */
    Traversal breadthFirstTraversal(String root = null, @DelegatesTo(BreadthFirstTraversalSpec) Closure specClosure) {
        BreadthFirstTraversalSpec spec = breadthFirstTraversalSpec(root, specClosure)
        traversal(this.&breadthFirstTraversalConnected, spec)
    }

    /**
     * Performs a breadth first traversal on a connected component of the graph starting
     * at the vertex identified by spec.root. The behavior of the traversal is determined by
     * spec.colors and spec.visit.
     * <p>
     * Traversal.STOP - It is possible to stop the traversal early by returning this value
     * in visit.
     * @param spec the BreadthFirstTraversalSpec
     * @return null or a Traversal value
     */
    @SuppressWarnings('NoDef')
    private Traversal breadthFirstTraversalConnected(BreadthFirstTraversalSpec spec) {
        if (!vertices[spec.root]) {
            throw new IllegalArgumentException("Could not find $spec.root in graph")
        }
        def traversal = spec.visit(vertices[spec.root])
        if (traversal == Traversal.STOP) {
            spec.colors[spec.root] = GREY
            return traversal
        }
        spec.colors[spec.root] = GREY
        Queue<String> queue = [] as Queue<String>
        queue << spec.root
        while (queue.size() != 0) {
            String current = queue.poll()
            Set<Edge> adjacentEdges = traverseEdges(current)
            for (int i = 0; i < adjacentEdges.size(); i++) {
                Edge edge = adjacentEdges[i]
                String connected = current == edge.one ? edge.two : edge.one
                if (spec.colors[connected] == WHITE) {
                    traversal = spec.visit(vertices[connected])
                    if (traversal == Traversal.STOP) {
                        spec.colors[connected] = GREY
                        return traversal
                    }
                    spec.colors[connected] = GREY
                    queue << connected
                }
            }
            spec.colors[current] = BLACK
        }
        null
    }

    /**
     * Classifies edges in a depthFirstTraversal returning the results.
     * @param action passed into EdgeClassification.addEdge
     * @return the resulting EdgeClassification
     */
    EdgeClassification classifyEdges(Closure action) {
        EdgeClassification ec = new EdgeClassification()
        depthFirstTraversal {
            classifyEdge { Edge edge, String from, String to, TraversalColor toColor ->
                ec.addEdge(this, edge, from, to, toColor, action)
            }
        }
        ec
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
        if(method != null) {
            return method.invoke(type, args)
        }

        def list = plugins.collect { plugin ->
            MetaMethod m = plugin.metaClass.getMetaMethod(name, args)
            if(m) {
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
