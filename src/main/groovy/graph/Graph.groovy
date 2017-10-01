package graph

import graph.plugin.Plugin
import graph.type.EdgeSpec
import graph.type.Type
import graph.type.VertexSpec
import graph.type.undirected.EdgeSpecCodeRunner

import graph.type.undirected.GraphType
import groovy.transform.PackageScope

/**
 * An implementation of a Graph. A {@link Vertex} is identified in this Graph using the vertex name property. A
 * {@link Edge} is identified by the names of the to vertices it connects.
 * <p>
 * {@link Vertex} and {@link Edge} objects are added using the vertex and edge methods. These methods will always create
 * objects if not present to reduce the code needed to express a graph and to ensure the integrity of the graph. There
 * are a few styles that may be used to express vertices and edges in this Graph. See the edge and vertex methods for
 * more details.
 * <p>
 * All graphs have a {@link type.Type}. All Vertex, Edge, VertexSpec, and EdgeSpec objects must be created by the Type.
 * This is to ensure the {@link Graph} always has the behavior specified by the {@link type.Type}.
 * <p>
 * The default behavior is that of an undirected graph. This is implemented by {@link GraphType}.
 * <p>
 * Plugins may be applied to this graph to change its behavior and the behavior of the vertices and edges. For more
 * information on plugins see {@link graph.plugin.Plugin}.
 */
class Graph implements GroovyInterceptable {
    private Map<String, ? extends Vertex> vertices = [:] as LinkedHashMap<String, ? extends Vertex>
    private final Set<Class> vertexTraitsSet = [] as LinkedHashSet<Class>
    private Set<? extends Edge> edges = [] as LinkedHashSet<? extends Edge>
    private final Set<Class> edgeTraitsSet = [] as LinkedHashSet<Class>
    private final Set<? extends Plugin> plugins = [] as LinkedHashSet<? extends Plugin>
    private Type type = new GraphType()


    /**
     * An enum defining traversal status. A value from this enum can be returned
     * from one of the closures passed to search methods changing the behavior of a traversal.
     */
    enum Traversal {
        /**
         * stops the current traversal. Useful in search when a vertex is found to end the traversal early.
         */
        STOP
    }

    /**
     * Defines the color for a vertex when traversing.
     */
    enum TraversalColor {
        /**
         * an undiscovered vertex
         */
        WHITE,
        /**
         * a discovered vertex that still needs work
         */
        GREY,
        /**
         * a vertex that the algorithm is done with
         */
         BLACK
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
     * returns the vertices as an unmodifiableMap. key is the vertex name and value is the vertex.
     * @return vertices as an unmodifiableMap
     */
    Map<String, ? extends Vertex> getVertices() {
        Collections.unmodifiableMap(vertices)
    }

    /**
     * Removes the {@link Vertex} from vertices with the matching name. If the Vertex has adjacentEdges it cannot be
     * deleted and IllegalStateException will be thrown.
     * @param name name of {@link Vertex} to delete from this graph
     * @throws IllegalStateException if named vertex has adjacentEdges.
     * @see {@link #adjacentEdges}
     */
    void delete(String name) {
        if (adjacentEdges(name)) {
            throw new IllegalStateException(
                    "Cannot delete $name. There are edges that connect to it. Try deleting those first."
            )
        }
        vertices.remove(name)
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

    void replaceVertices(Closure closure) {
        Map<String, ? extends Vertex> replace = vertices.collectEntries(closure) as Map<String, Vertex>
        vertices.clear()
        vertices.putAll(replace)
    }

    void replaceVerticesMap(Map<String, ? extends Vertex> map) {
        if(!map.isEmpty()) {
            throw new IllegalArgumentException('map must be empty.')
        }
        map.putAll(vertices)
        vertices = map
    }

    /**
     * Removes an {@link Edge} from edges where an edge created by the edgeFactory equals an edge in edges. Using the
     * edgeFactory ensures the edge removed matches the definition of an edge for this graph. If a plugin changes the
     * definition of an edge, for example to {@link graph.type.directed.DirectedEdge}, this method will still work as
     * expected. It will remove the edge where edge.one == one and edge.two == two. Keep in mind, in the case of the
     * base {@link Edge} object edge.one can also equal two and edge.two can also equal one.
     * @param one name of first vertex
     * @param two name of second vertex
     */
    void deleteEdge(String one, String two) {
        edges.remove(type.newEdge(one, two))
    }

    /**
     * returns plugins as an unmodifiable set
     * @return plugins as an unmodifiable set
     */
    Set<? extends Plugin> getPlugins() {
        Collections.unmodifiableSet(plugins)
    }

    /**
     * Creates and applies a {@link Plugin} to this graph.
     * @param pluginClass - the {@link Plugin} to create and setup to this graph.
     */
    void apply(Class pluginClass) {
        if (plugins.contains(pluginClass)) {
            throw new IllegalArgumentException("$pluginClass.name is already applied.")
        }
        if (!pluginClass.interfaces.contains(Plugin)) {
            throw new IllegalArgumentException("$pluginClass.name does not implement Plugin")
        }
        plugins << pluginClass
        Plugin plugin = pluginClass.newInstance()
        plugin.apply(this)
    }

    void apply(String pluginName) {
        Properties properties = new Properties()
        properties.load(getClass().getResourceAsStream("/META-INF/graph-plugins/${pluginName}.properties"))
        apply(Class.forName((String) properties.'implementation-class'))
    }

    void type(Class typeClass) {
        if(!Type.isAssignableFrom(typeClass)) {
            throw new IllegalArgumentException("$typeClass.name does not implement Type")
        }
        type = (Type) typeClass.newInstance()
        type.convert(this)
    }

    void type(String typeName) {
        Properties properties = new Properties()
        properties.load(getClass().getResourceAsStream("/META-INF/graph-types/${typeName}.properties"))
        type(Class.forName((String) properties.'implementation-class'))
    }

    Type getType() {
        return type
    }

    /**
     * Applies trait to all vertices and all future vertices.
     * @param traits to add to vertices and all future vertices
     */
    void vertexTraits(Class... traits) {
        vertices.each { name, vertex ->
            vertex.delegateAs(traits)
        }
        vertexTraitsSet.addAll(traits)
    }

    /**
     * Adds a vertex object directly. For internal use to create copies of a Graph.
     * @param vertex
     * @return true if add was successful.
     */
    @PackageScope
    boolean addVertex(Vertex vertex) {
        vertices[vertex.name] = vertex
    }

    /**
     * Finds the {@link Vertex} with the given name or creates a new one.
     * @param name  the name of the {@link Vertex} to find or create.
     * @return the resulting {@link Vertex}
     * @throws {@link IllegalArgumentException} When name is null or empty.
     */
    Vertex vertex(String name) {
        ConfigSpec spec = new ConfigSpec(map:[name:name])
        vertex(spec)
    }

    /**
     * Creates a {@link Vertex} in this graph using name in the {@link NameSpec}.
     * @param spec  The name of the Vertex.
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(NameSpec spec) {
        vertex(spec.name)
    }

    /**
     * Finds or creates all vertices returning them in a Set.
     * @param name  name of first {@link Vertex} to find or create.
     * @param names  of other vertices to find or create.
     * @return set of created vertices
     */
    Set<Vertex> vertex(String name, String... names) {
        Set<Vertex> set = new LinkedHashSet<>()
        set << vertex(name)
        names.collect {
            vertex(it)
        }.each {
            set << it
        }
        set
    }

    /**
     * Finds or creates all vertices returning them in a Set.
     * @param name  first vertex to create
     * @param names  vertices to create
     * @return set of created vertices
     */
    Set<Vertex> vertex(NameSpec name, NameSpec... names) {
        vertex(name.name, names*.name as String[])
    }

    /**
     * Renames a {@link Vertex}. All edges connecting the {@link Vertex} are updated with the new name.
     * @param name  of original vertex
     * @param newName  for updated vertex
     */
    void rename(String name, String newName) {
        if (!newName) {
            throw new IllegalArgumentException('newName is null or empty.')
        }
        Vertex vertex = vertex(name)
        vertices.remove(vertex.name)
        vertex.name = newName
        vertices[vertex.name] = vertex
        adjacentEdges(name).each {
            if (it.one == name) {
                it.one = newName
            }
            if (it.two == name) {
                it.two = newName
            }
        }
    }

    /**
     * Renames a {@link Vertex}. All edges connecting the {@link Vertex} are updated with the new name.
     * @param name  of original vertex
     * @param newName  for updated vertex
     */
    void rename(NameSpec name, NameSpec newName) {
        rename(name.name, newName.name)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map may contain configuration for the vertex. Default
     * configuration used can be:
     * <dl>
     *     <dt>name</dt>
     *     <dd>name of the vertex to create or update</dd>
     *     <dt>rename</dt>
     *     <dd>new name for the vertex</dd>
     *     <dt>connectsTo</dt>
     *     <dd>list of vertex names the vertex should connect to. Edges will be created with edge.one equal to the
     *     vertex name and edge.two equals to the 'connectTo' name.</dd>
     *     <dt>trait</dt>
     *     <dd>groovy trait to setup on the vertex delegate</dd>
     *     <dt>runnerCode</dt>
     *     <dd>closure to run after the vertex has been created. This can be used to configure the vertex with more
     *     complex operations. See {@link #vertex(String,Closure)} for a detailed description of methods availiable
     *     in the closure.</dd>
     * </dl>
     * Additional entries may be added by plugins applied to the graph.
     * @param map  configuration of {@link Vertex}
     * @return the resulting {@link Vertex}
     * @see #vertex(String name, Closure closure)
     */
    Vertex vertex(Map<String, ?> map) {
        ConfigSpec spec = new ConfigSpec(map:map)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. {@code closure} is used to further
     * customize the vertex and graph. In the closure getting and setting properties delegates to the vertex. Methods
     * also delegate to the vertex.
     * <p>
     * Variables accessible within the closure:
     * <dl>
     *     <dt>graph</td>
     *     <dd>{@link Graph} vertex was added to</dd>
     *     <dt>vertex</dt>
     *     <dd>{@link Vertex} added to graph</dd>
     * </dl>
     * <p>
     * By default there are several methods added in the closure.
     * <p>
     * <dl>
     *     <dt>{@code void rename(String newName)}</dt>
     *     <dd>renames the vertex</dd>
     *     <dt>{@code void rename(NameSpec newName)}</dt>
     *     <dd>renames the vertex using a NameSpec</dd>
     *     <dt>{@code void trait(Class... trait)}</dt>
     *     <dd>applies trait to the vertex</dd>
     *     <dt>{@code void connectsTo(String... names)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created.</dd>
     *     <dt>{@code void connectsTo(ConfigSpec... specs)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created. This method allows for
     *     arbitrarily nested configurations.</dd>
     * </dl>
     * <p>
     * Plugins may add variables and methods to the passed in closure.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(String name, Closure closure) {
        ConfigSpec spec = new ConfigSpec(map:[name:name], closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. The configuration given by the closure is
     * described in {@link #vertex(String,Closure)}.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(NameSpec name, Closure closure) {
        vertex(name.name, closure)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. The map contains configuration
     * described in {@link #vertex(Map)}.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(String name, Map<String, ?> map) {
        map.name = map.name ?: name
        ConfigSpec spec = new ConfigSpec(map:map)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. The map contains configuration
     * described in {@link #vertex(Map)}.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(NameSpec name, Map<String, ?> map) {
        vertex(name.name, map)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration
     * described in {@link #vertex(Map)}. The configuration given by the closure is described in
     * {@link #vertex(String,Closure)}.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return  The resulting {@link Vertex}.
     */
    Vertex vertex(Map<String, ?> map, Closure closure) {
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration
     * described in {@link #vertex(Map)}. The configuration given by the closure described in
     * {@link #vertex(String,Closure)}.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(String name, Map<String, ?> map, Closure closure) {
        map.name = map.name ?: name
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration
     * described in {@link #vertex(Map)}. The configuration given by the closure described in
     * {@link #vertex(String,Closure)}.
     * @param name  the name of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(NameSpec name, Map<String, ?> map, Closure closure) {
        vertex(name.name, map, closure)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The closure contains configuration described in {@link #vertex(String,Closure)}.
     * @param spec  specification for vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(ConfigSpec spec) {
        if(!vertices[(String) spec.map.name]) {
            if (spec.map.traits) {
                spec.map.traits.addAll(vertexTraitsSet)
            } else {
                spec.map.traits = new ArrayList(vertexTraitsSet)
            }
        }
        VertexSpec vspec = type.newVertexSpec(this, spec)
        Vertex vertex = vspec.setup()
        vertices.put(vertex.name, vertex)
        vspec.applyClosure()
        vertex
    }

    /**
     * Applies trait to all edges and all future edges.
     * @param traits to add to all edges and all future edges
     */
    void edgeTraits(Class... traits) {
        edges.each { edge ->
            edge.delegateAs(traits)
        }
        edgeTraitsSet.addAll(traits)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The
     * {@link Vertex} objects are identified by the params one and two. If the {@link Vertex} objects do not exist they
     * will be created.
     * @param one  the name of the first {@link Vertex}.
     * @param two  the name of the second {@link Vertex}.
     * @return the resulting {@link Edge}.
     */
    Edge edge(String one, String two) {
        ConfigSpec spec = new ConfigSpec(map:[one:one, two:two])
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. This
     * method calls {@link #edge(String,String,Map} with the names from the {@link NameSpec as params.
     * @param one  {@link NameSpec} for the first {@link Vertex}.
     * @param two  {@link NameSpec} for the second {@link Vertex}.
     * @return the resulting {@link Edge}.
     */
    Edge edge(NameSpec one, NameSpec two) {
        edge(one.name, two.name)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}.The map may
     * contain configuration for the edge. Default configuration options are:
     * <dl>
     *     <dt>one</dt>
     *     <dd>name of the first {@link Vertex}</dd>
     *     <dt>two</dt>
     *     <dd>name of the second {@link Vertex}</dd>
     *     <dt>renameOne</dt>
     *     <dd>If the edge already exists rename edge.one to renameOne. Otherwise edge.one is set to renameOne instead
     *     of map.one.</dd>
     *     <dt>renameTwo</dt>
     *     <dd>If the edge already exists rename edge.two to renameTwo. Otherwise edge.two is set to renameTwo instead
     *     of map.one.</dd>
     *     <dt>traits</dt>
     *     <dd>Set of traits applied to the delegate of the edge.</dd>
     *     <dt>runnerCode</dt>
     *     <dd>Closure to run after the edge has been created. This can be used to configure the vertex with more
     *     complex operations. See {@link #edge(String,String,Closure) for a detailed description of methods available
     *     in the closure.</dd>
     * </dl>
     * Additional entries may be added by plugins applied to the graph.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     * @see #edge(String one, String two, Closure closure)
     */
    Edge edge(Map<String, ?> map) {
        ConfigSpec spec = new ConfigSpec(map:map)
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters.
     * @param one  the name of the first {@link Vertex}.
     * @param two  the name of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     */
    Edge edge(String one, String two, Map<String, ?> map) {
        map.one = map.one ?: one
        map.two = map.two ?: two
        ConfigSpec spec = new ConfigSpec(map:map)
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters.
     * @param one  {@link NameSpec} for the first {@link Vertex}.
     * @param two  {@link NameSpec} for the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     */
    Edge edge(NameSpec one, NameSpec two, Map<String, ?> map) {
        edge(one.name, two.name, map)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. {@code Closure}
     * is used to further customize the edge and graph. In the closure getting and setting properties delegates to the
     * edge. Methods also delegate to the edge.
     * <p>
     * Variables accessible within the closure:
     * <dl>
     *     <dt>{@code graph}</dt>
     *     <dd>{@link Graph} edge was added to</dd>
     *     <dt>{@code edge}</dt>
     *     <dd>{@link Edge} added to graph</dd>
     * </dl>
     * Attempting to set one or two will throw an exception. Use the {@code renameOne} and {@code renameTwo} methods.
     * <p>
     * By default there are several methods available in the closure.
     * <p>
     * <dl>
     *     <dt>{@code void renameOne(String renameOne)}</dt>
     *     <dd>Renames edge.one</dd>
     *     <dt>{@code void renameOne(NameSpec renameOne)}</dt>
     *     <dd>Renames edge.one</dd>
     *     <dt>{@code void renameTwo(String renameTwo)}</dt>
     *     <dd>Renames edge.two</dd>
     *     <dt>{@code void renameTwo(NameSpec renameTwo)}</dt>
     *     <dd>Renames edge.two</dd>
     *     <dt>{@code void traits(Class... traits)}</dt>
     *     <dd>Applies a trait the edge's delegate</dd>
     * </dl>
     * Plugins may add variables and methods to the passed in closure.
     * @param one  the name of the first {@link Vertex}.
     * @param two  the name of the second {@link Vertex}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     */
    Edge edge(String one, String two, @DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        ConfigSpec spec = new ConfigSpec(map:[one:one, two:two], closure:closure)
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The
     * configuration given by the closure is described in {@link #edge(String, String, Closure)}.
     * @param one  {@link NameSpec} for the first {@link Vertex}.
     * @param two  {@link NameSpec} for the second {@link Vertex}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     */
    Edge edge(NameSpec one, NameSpec two, @DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        edge(one.name, two.name, closure)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}. If one or two are entries in map those values will be used
     * instead of the parameters.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     */
    Edge edge(Map<String, ?> map, @DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}.
     * @param one  the name of the first {@link Vertex}.
     * @param two  the name of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     */
    Edge edge(String one, String two, Map<String, ?> map, @DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        map.one = map.one ?: one
        map.two = map.two ?: two
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        configEdge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}.
     * @param one  {@link NameSpec} for the first {@link Vertex}.
     * @param two  {@link NameSpec} for the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     */
    Edge edge(NameSpec one, NameSpec two, Map<String, ?> map, @DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        edge(one.name, two.name, map, closure)
    }

    /**
     * Applies spec to this graph.
     * @param spec the specification for an {@link Edge}
     * @return the resulting {@link Edge}.
     */
    @PackageScope
    Edge configEdge(ConfigSpec spec) {
        if(!edges.find { it == type.newEdge((String) spec.map.one, (String) spec.map.two) }) {
            if (spec.map.traits) {
                spec.map.traits.addAll(edgeTraitsSet)
            } else {
                spec.map.traits = new ArrayList(edgeTraitsSet)
            }
        }
        EdgeSpec espec = type.newEdgeSpec(this, spec)
        Edge edge = espec.setup()
        edges.add(edge)
        espec.applyClosure()
        edge
    }

    /**
     * Returns the first unvisited vertex name in vertices.
     *
     * @param colors a map of vertex name entries with the value of the TraversalColor
     * @return the first unvisited vertex name in the vertices.
     */
    String getUnvisitedVertexName(Map colors) {
        vertices.find { k, v ->
            colors[(k)] != TraversalColor.BLACK && colors[k] != TraversalColor.GREY
        }?.key
    }

    /**
     * returns the name of first unvisited child vertex with a parent matching parentName.
     *
     * @param parentName  the name of the parent vertex to start searching from
     * @param colors  a map of vertex name entries with the value of the TraversalColor
     * @return the name of the first unvisited child vertex
     */
    String getUnvisitedChildName(String parentName, Map<String, TraversalColor> colors) {
        Edge edge = traverseEdges(parentName).findAll {
            it.one != it.two
        }.find {
            String childName = parentName == it.one ? it.two : it.one
            TraversalColor color = colors[childName]
            color != TraversalColor.GREY && color != TraversalColor.BLACK
        }

        if (!edge) {
            return null
        }
        parentName == edge.one ? edge.two : edge.one
    }

    /**
     * returns the name of first unvisited child vertex with a parent matching parentName.
     * @param spec  the name of the parent vertex to start searching from
     * @param colors  a map of vertex name entries with the value of the TraversalColor
     * @return the name of the first unvisited child vertex
     */
    String getUnvisitedChildName(NameSpec spec, Map<String, TraversalColor> colors) {
        getUnvisitedChildName(spec.name, colors)
    }

    /**
     * returns the name of first unvisited child vertex with a parent matching parentName.
     * @param spec  configuration containing a name for the parent and TraversalColors for each vertex
     * @return the name of the first unvisited child vertex
     */
    String getUnvisitedChildName(ConfigSpec spec) {
        getUnvisitedChildName((String) spec.map.name, spec.map)
    }

    /**
     * Finds adjacent edges for vertex with name.
     * @param name
     * @return set of adjacent edges.
     */
    Set<? extends Edge> adjacentEdges(String name) {
        edges.findAll {
            name == it.one || name == it.two
        }
    }

    /**
     * Finds adjacent edges for vertex with name.
     * @param name
     * @return set of adjacent edges.
     */
    Set<? extends Edge> adjacentEdges(NameSpec name) {
        adjacentEdges(name.name)
    }

    /**
     * Returns edges from vertex with name that should be traversed.
     * @param name
     * @return
     */
    Set<? extends Edge> traverseEdges(String name) {
        adjacentEdges(name)
    }

    /**
     * Returns edges from vertex with name that should be traversed.
     * @param name
     * @return
     */
    Set<? extends Edge> traverseEdges(NameSpec name) {
        traverseEdges(name.name)
    }

    /**
     * Creates and returns a color map in the form of name:color where name is the vertex name and color is
     * TraversalColor.WHITE.
     * @return
     */
    Map makeColorMap() {
        vertices.collectEntries { name, vertex ->
            [(name):TraversalColor.WHITE]
        }
    }

    /**
     * configures a depth first traversal with the given closure using {@link #depthFirstTraversalSpec(String,Closure)}.
     * Once the spec is configured {@link #traversal(Closure,TraversalSpec)} is called.
     * @param root  optional root to start traversal
     * @param specClosure  closure for depthFirstTraversalSpec method
     * @return result of the traversal
     */
    Traversal depthFirstTraversal(String root = null, @DelegatesTo(DepthFirstTraversalSpec) Closure specClosure) {
        DepthFirstTraversalSpec spec = depthFirstTraversalSpec(root, specClosure)
        traversal(this.&depthFirstTraversalConnected, spec)
    }

    /**
     * configures a depth first traversal with the given closure using {@link #depthFirstTraversalSpec(String,Closure)}.
     * Once the spec is configured {@link #traversal(Closure,TraversalSpec)} is called.
     * @param root  optional root to start traversal
     * @param specClosure  closure for depthFirstTraversalSpec method
     * @return result of the traversal
     */
    Traversal depthFirstTraversal(NameSpec root, @DelegatesTo(DepthFirstTraversalSpec) Closure specClosure) {
        depthFirstTraversal(root.name, specClosure)
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
     * in the spec it defaults to the result of calling {@link #getUnvisitedVertexName(Map)} with spec.colors.
     * @param spec the {@link TraversalSpec} to configure with defaults.
     */
    private void setupSpec(TraversalSpec spec) {
        spec.colors = spec.colors ?: makeColorMap()
        spec.root = spec.root ?: getUnvisitedVertexName(spec.colors)
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
     * @param traversalConnected - one of the traversalConnected methods in this graph
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
            spec.root = getUnvisitedVertexName(spec.colors)
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
        def root = spec.root
        if (spec.preorder && spec.preorder(vertices[root]) == Traversal.STOP) {
            spec.colors[root] = TraversalColor.GREY
            return Traversal.STOP
        }
        spec.colors[root] = TraversalColor.GREY

        Set<Edge> adjacentEdges = traverseEdges(root)
        for (int index = 0; index < adjacentEdges.size(); index++) { //cannot stop and each() call on adjacentEdges
            Edge edge = adjacentEdges[index]
            String connectedName = root == edge.one ? edge.two : edge.one
            if (spec.classifyEdge && spec.classifyEdge(edge, root, connectedName,
                    spec.colors[connectedName]) == Traversal.STOP) {
                return Traversal.STOP
            }
            if (spec.colors[connectedName] == TraversalColor.WHITE) {
                spec.root = connectedName
                if (Traversal.STOP == depthFirstTraversalConnected(spec)) {
                    return Traversal.STOP
                }
            }
        }

        if (spec.postorder && spec.postorder(vertices[root]) == Traversal.STOP) {
            spec.colors[root] = TraversalColor.BLACK
            return Traversal.STOP
        }
        spec.colors[root] = TraversalColor.BLACK
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
     * executes closure on each {@link Vertex} in breadth first order. See {@link #breadthFirstTraversal} for details.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     */
    void eachBfs(NameSpec root, Closure closure) {
        eachBfs(root.name, closure)
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
     * Executes closure on each {@link Vertex} in breadth first order starting at root. If the closure returns true the
     * {@link Vertex} is returned.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return first {@link Vertex} where closure returns true
     */
    Vertex findBfs(NameSpec root, Closure closure) {
        findBfs(root.name, closure)
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
     * Executes closure on each vertex in breadth first order starting at root. object is the initial value passed to
     * the closure. Each returned value from the closure is passed to the next call.
     * @param root  vertex to start breadth first traversal
     * @param object  initial value passed to the closure
     * @param closure  execute on each {@link Vertex}
     * @return object returned from the final call to closure.
     */
    Object injectBfs(NameSpec root, Object object, Closure closure) {
        injectBfs(root.name, object, closure)
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
        if(root) {
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
     * Runs closure on each vertex in breadth first order starting at root. The vertices where closure returns true are
     * returned.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return the vertices where closure returns true
     */
    List<? extends Vertex> findAllBfs(NameSpec root, Closure closure) {
        findAllBfs(root.name, closure)
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
        if(root) {
            inject = this.&injectBfs.curry(root)
        } else {
            inject = this.&injectBfs
        }
        (List<? extends Vertex>) inject([]) { result, vertex ->
            result << closure(vertex)
        }
    }


    /**
     * Runs closure on each vertex in breadth first order, starting at root, collecting returned values from the
     * closure.
     * @param root  vertex to start breadth first traversal
     * @param closure  execute on each {@link Vertex}
     * @return values returned from each execution of closure
     */
    List<?> collectBfs(NameSpec root, Closure closure) {
        collectBfs(root.name, closure)
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

    Traversal breadthFirstTraversal(NameSpec root, @DelegatesTo(BreadthFirstTraversalSpec) Closure specClosure) {
        breadthFirstTraversal(root.name, specClosure)
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
    private Traversal breadthFirstTraversalConnected(BreadthFirstTraversalSpec spec) {
        if (!vertices[spec.root]) {
            throw new IllegalArgumentException("Could not find $spec.root in graph")
        }
        def traversal = spec.visit(vertices[spec.root])
        if (traversal == Traversal.STOP) {
            spec.colors[spec.root] = TraversalColor.GREY
            return traversal
        }
        spec.colors[spec.root] = TraversalColor.GREY
        Queue<String> queue = [] as Queue<String>
        queue << spec.root
        while (queue.size() != 0) {
            String current = queue.poll()
            Set<Edge> adjacentEdges = traverseEdges(current)
            for (int i = 0; i < adjacentEdges.size(); i++) {
                Edge edge = adjacentEdges[i]
                String connected = current == edge.one ? edge.two : edge.one
                if (spec.colors[connected] == TraversalColor.WHITE) {
                    traversal = spec.visit(vertices[connected])
                    if (traversal == Traversal.STOP) {
                        spec.colors[connected] = TraversalColor.GREY
                        return traversal
                    }
                    spec.colors[connected] = TraversalColor.GREY
                    queue << connected
                }
            }
            spec.colors[current] = TraversalColor.BLACK
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
     * Creates a {@link graph.type.VertexSpec}
     * @param name
     * @return a {@link VertexSpec} with name set to the property name.
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        new NameSpec(name:name)
    }

    /**
     * Creates a {@link VertexSpec}. The result is similar to calling {@link VertexSpec#newVertexSpec(Map)}
     * <pre>
     *     VertexSpec.newVertexSpec([name:name + args[0])
     * </pre>
     * @param name
     * @param args
     * @return a {@link VertexSpec}
     */
    @SuppressWarnings('Instanceof')
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        MetaMethod method = type.metaClass.getMetaMethod(name, args)
        if(method != null && (method.declaringClass.theClass == Type || method.declaringClass.theClass.interfaces.contains(Type))) {
            return method.invoke(type, args)
        }

        if (name == 'vertex') {
            throw new IllegalArgumentException("Confusing name 'vertex' for spec.")
        }
        if (args.size() == 0) {
            return new ConfigSpec(map:[name:name])
        }

        if (args.size() == 1 && args[0] instanceof Map) {
            args[0].name = args[0].name ?: name
            return new ConfigSpec(map:(Map) args[0])
        }

        if (args.size() == 1 && args[0] instanceof Closure) {
            return new ConfigSpec(map:[name:name], closure:(Closure) args[0])
        }

        if (args.size() == 2 && args[0] instanceof Map && args[1] instanceof Closure) {
            args[0].name = args[0].name ?: name
            return new ConfigSpec(map:(Map) args[0], closure:(Closure) args[1])
        }

        throw new MissingMethodException(name, Graph, args)
    }
}
