package graph

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
 * The default behavior is that of an undirected graph. There can only be one {@link Edge} between any two vertices.
 * When traversing a graph an {@link Edge} is adjacent to a {@link Vertex} if it's one or two property equals the name
 * of the {@link Vertex}.
 * <p>
 * Plugins may be applied to this graph to change its behavior and the behavior of the vertices and edges within this
 * graph. For more information on plugins see {@link Plugin}.
 */
class Graph {
    private Map<String, ? extends Vertex> vertices = [:] as LinkedHashMap<String, ? extends Vertex>
    private Set<? extends Edge> edges = [] as LinkedHashSet<? extends Edge>
    private Set<? extends Plugin> plugins = [] as LinkedHashSet<? extends Plugin>
    private EdgeFactory edgeFactory = new UnDirectedEdgeFactory()
    private VertexFactory vertexFactory = new DefaultVertexFactory()

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
     * static entry point for creating a graph within a groovy script.
     * @param c
     * @return
     */
    static Graph graph(Closure c) {
        def graph = new Graph()
        graph.with(c)
        graph
    }

    /**
     * returns the vertices as an unmodifiableMap
     * @return
     */
    Map<String, ? extends Vertex> getVertices() {
        Collections.unmodifiableMap(vertices)
    }

    /**
     * returns the edges as an unmodifiable set
     * @return
     */
    Set<? extends Edge> getEdges() {
        Collections.unmodifiableSet(edges)
    }

    /**
     * returns plugins as an unmodifiable set
     * @return
     */
    Set<? extends Plugin> getPlugins() {
        Collections.unmodifiableSet(plugins)
    }

    /**
     * Creates and applies a {@link Plugin} to this graph.
     * @param pluginClass - the {@link Plugin} to create and apply to this graph.
     * @return
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
     * @param name - the name of the {@link Vertex} to find or create.
     * @return the resulting {@link Vertex}
     * @throws {@link IllegalArgumentException} When name is null or empty.
     */
    Vertex vertex(String name) {
        if(!name) {
            throw new IllegalArgumentException("!name failed. Name must be groovy truth.")
        }
        Vertex vertex = vertices[name] ?: vertexFactory.newVertex(name)
        vertices[name] = vertex
        vertex
    }

    /**
     * Finds or creates all vertices returning them in a Set.
     * @param names
     * @return
     */
    Set<Vertex> vertex(String... names) {
        names.collect { name ->
            vertex(name)
        } as Set<Vertex>
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. See {@link VertexSpec@applyToGraphAndVertex(Graph,Vertex)} for details
     * on how this graph and the {@link Vertex} are modified.
     * @param closure - delegates to {@link VertexSpec}
     * @return the resulting {@link Vertex}
     */
    Vertex vertex(@DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = VertexSpec.newInstance(this, closure)
        Vertex vertex = vertex(spec.name)
        spec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map must contain configuration described in
     * {@link VertexSpec#newInstance(Map)}.
     * @param map
     * @return the resulting {@link Vertex}
     */
    Vertex vertex(Map<String, ?> map) {
        VertexSpec spec = VertexSpec.newInstance(map)
        Vertex vertex = vertex(spec.name)
        spec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. The configuration given by the closure is delegated to a
     * {@link VertexSpec}. See {@link VertexSpec} for details on how it modifies this graph and the {@link Vertex}.
     * @param name
     * @param closure
     * @return
     */
    Vertex vertex(String name, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = VertexSpec.newInstance(this, closure)
        Vertex vertex = vertex(name)
        spec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Renames a {@link Vertex}. All edges connecting the {@link Vertex} are updated with the new name.
     * @param name
     * @param newName
     */
    void rename(String name, String newName) {
        if(!newName) {
            throw new IllegalArgumentException("newName is null or empty.")
        }
        adjacentEdges(name).each {
            if(it.one == name) {
                it.one = newName
            }
            if(it.two == name) {
                it.two = newName
            }
            vertex(name).name = newName
        }
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given name. The map must contain configuration described in
     * {@link VertexSpec#newInstance(Map)}.
     * @param name
     * @param map
     * @return
     */
    Vertex vertex(String name, Map<String, ?> map) {
        VertexSpec spec = VertexSpec.newInstance(map)
        Vertex vertex = vertex(name)
        spec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. This method creates two {@link VertexSpec} objects from the
     * map and closure. The map is applied before the closure.
     * @param map -
     * @param closure -
     * @return the resulting vertex
     */
    Vertex vertex(Map<String, ?> map, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec mapSpec = VertexSpec.newInstance(map)
        VertexSpec closureSpec = VertexSpec.newInstance(this, closure)
        Vertex vertex = vertex(mapSpec.name)
        mapSpec.applyToGraphAndVertex(this, vertex)
        closureSpec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. This method creates two {@link VertexSpec} objects from the
     * map and closure. The map is applied before the closure.
     * @param name
     * @param map
     * @param closure
     * @return
     */
    Vertex vertex(String name, Map<String, ?> map, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec mapSpec = VertexSpec.newInstance(map)
        VertexSpec closureSpec = VertexSpec.newInstance(this, closure)
        Vertex vertex = vertex(name)
        mapSpec.applyToGraphAndVertex(this, vertex)
        closureSpec.applyToGraphAndVertex(this, vertex)
        vertex
    }

    /**
     * Adds an edge object directly.
     * @param edge
     * @return true if add was successful.
     */
    @PackageScope
    boolean addEdge(Edge edge) {
        edges << edge
    }

    /**
     * Creates or finds an edge between two vertices. The vertices are checked using {#vertex(String... names)}. If the
     * edge already exists it is returned.
     * @param one - the name of the first {@link Vertex}
     * @param two - the name of the second {@link Vertex}
     * @return the resulting edge
     */
    Edge edge(String one, String two) {
        vertex(one, two)
        Edge e = edgeFactory.newEdge(one, two)
        Edge edge = edges.find  { it == e } ?: e
        edges << edge
        edge
    }

    /**
     * Creates or updates a {@link Edge} in this graph. See {@link EdgeSpec#applyToGraphAndEdge(Graph,Edge)} for details
     * on how this graph and the {@link Edge} are modified.
     * @param closure - delegates to {@link EdgeSpec}
     * @return the resulting {@link Edge}
     */
    Edge edge(@DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec spec = EdgeSpec.newInstance(this, closure)
        Edge edge = edge(spec.one, spec.two)
        spec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Creates or updates a {@link Edge} in this graph. The map must contain configuration described in
     * {@link EdgeSpec#newInstance(Map)}.
     * @param map
     * @return the resulting {@link Edge}
     */
    Edge edge(Map<String, ?> map) {
        EdgeSpec spec = EdgeSpec.newInstance(map)
        Edge edge = edge(spec.one, spec.two)
        spec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Creates or updtes a {@link Edge} in this graph with the given one and two. The configuration given by the closure is delegated to an
     * {@link EdgeSpec}. See {@link EdgeSpec#applyToGraphAndEdge(Graph,Edge)} for details on how it modifies this graph and the {@link Edge}.
     * @param one
     * @param two
     * @param closure
     * @return
     */
    Edge edge(String one, String two, @DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec spec = EdgeSpec.newInstance(this, closure)
        Edge edge = edge(one, two)
        spec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Creates or updates a {@link Edge} in this graph with the given one and two. The map must contain configuration described in
     * {@link EdgeSpec#newInstance(Map)}.
     * @param one
     * @param two
     * @param map
     * @return
     */
    Edge edge(String one, String two, Map<String, ?> map) {
        EdgeSpec spec = EdgeSpec.newInstance(map)
        Edge edge = edge(one, two)
        spec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Creates or updates a {@link Edge} in this graph. This methods creates two {@link EdgeSpec} objects from the
     * map and closure. The map is applied before the clsoure using {@link EdgeSpec#applyToGraphAndEdge(Graph,Edge)}.
     * @param map
     * @param closure
     * @return
     */
    Edge edge(Map<String, ?> map, @DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec mapSpec = EdgeSpec.newInstance(map)
        EdgeSpec closureSpec = EdgeSpec.newInstance(this, closure)
        Edge edge = edge(mapSpec.one, mapSpec.two)
        mapSpec.applyToGraphAndEdge(this, edge)
        closureSpec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Creates or updates a {@link Edge} in this graph with the given one and two. This methods creates two {@link EdgeSpec} objects from the
     * map and closure. The map is applied before the clsoure using {@link EdgeSpec#applyToGraphAndEdge(Graph,Edge)}.
     * @param one
     * @param two
     * @param map
     * @param closure
     * @return
     */
    Edge edge(String one, String two, Map<String, ?> map, @DelegatesTo(EdgeSpec) Closure closure) {
        EdgeSpec mapSpec = EdgeSpec.newInstance(map)
        EdgeSpec closureSpec = EdgeSpec.newInstance(this, closure)
        Edge edge = edge(one, two)
        mapSpec.applyToGraphAndEdge(this, edge)
        closureSpec.applyToGraphAndEdge(this, edge)
        edge
    }

    /**
     * Returns the first unvisited vertex name in vertices.
     *
     * @param colors a map of vertex name entries with the value of the TraversalColor
     * @return the first unvisited vertex name in the vertices.
     */
    def getUnvisitedVertexName(colors) {
        vertices.find { k, v ->
            colors[(k)] != TraversalColor.BLACK && colors[k] != TraversalColor.GREY
        }?.key
    }

    /**
     * returns the name of first unvisited child vertex with a parent matching parentName.
     *
     * @param colors a map of vertex name entries with the value of the TraversalColor
     * @param parentName the name of the parent vertex to start searching from
     * @return the name of the first unvisited child vertex
     */
    def getUnvisitedChildName(colors, parentName) {
        def edge = traverseEdges(parentName).findAll {
            it.one != it.two
        }.find {
            def childName = parentName == it.one ? it.two : it.one
            def color = colors[childName]
            return !(color == TraversalColor.GREY || color == TraversalColor.BLACK)
        }

        if (!edge) {
            return null
        }
        parentName == edge.one ? edge.two : edge.one
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
     * Returns edges from vertex with name that should be traversed.
     * @param name
     * @return
     */
    Set<? extends Edge> traverseEdges(String name) {
        adjacentEdges(name)
    }

    /**
     * creates and returns a color map in the form of
     * name : color. name is the vertex
     * name and TraversalColor.WHITE is the
     * color.
     * @return
     */
    def makeColorMap() {
        vertices.collectEntries { name, vertex ->
            [(name): TraversalColor.WHITE]
        }
    }

    /**
     * configures a depth first traversal with the given closure using
     * depthFirstTraversalSpec().
     *
     * Once the spec is configured traversal(graph.&depthFirstTraversalConnected, spec) is called.
     *
     * @param specClosure
     * @return
     */
    Traversal depthFirstTraversal(Closure specClosure) {
        def spec = depthFirstTraversalSpec(specClosure)
        traversal(this.&depthFirstTraversalConnected, spec)
    }

    /**
     * creates a DepthFirstTraversalSpec from the provided closure.
     *
     * defaults will be configured with the setupSpec method.
     *
     * @param specClosure is a closure that has a new DepthFirstTraversalSpec
     * as a delegate. Modify the DepthFirstTraversalSpec in this closure to
     * change the behavior of the depth first traversal.
     * @return
     */
    DepthFirstTraversalSpec depthFirstTraversalSpec(Closure specClosure) {
        def spec = new DepthFirstTraversalSpec()
        specClosure.delegate = spec
        specClosure()
        setupSpec(spec)
        spec
    }

    /**
     * Configures defaults for a TraversalSpec. When colors and root are not set
     * This method will generate defaults.
     *
     * if colors is not defined in the spec it defaults to the result of
     * makeColorMap()
     *
     * if root is not defined in the spec it defaults to the result of
     * getUnvisitedVertexName(spec.colors)
     * @param spec the traversal spec to configure with defaults.
     */
    void setupSpec(TraversalSpec spec) {
        if (!spec.colors) {
            spec.colors = makeColorMap()
        }
        if (!spec.root) {
            spec.root = getUnvisitedVertexName(spec.colors)
        }
    }

    /**
     * Creates a BreadthFirstTraversalSpec with the given closure.
     *
     * defaults will be configured with the setupSpec method.
     *
     * @param specClosure is a closure that has a new BreadthFirstTraversalSpec
     * as a delegate. Modify the BreadthFirstTraversalSpec in this closure to
     * change the behavior of the breadth first traversal.
     * @return
     */
    BreadthFirstTraversalSpec breadthFirstTraversalSpec(Closure specClosure) {
        def spec = new BreadthFirstTraversalSpec()
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
    Traversal traversal(traversalConnected, spec) {
        String name = spec.root
        while (name) {
            def traversal = traversalConnected(name, spec)
            if (traversal == Traversal.STOP) {
                return Traversal.STOP
            }
            name = getUnvisitedVertexName(spec.colors)
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
     * @param root the root of the vertex to start at
     * @param spec the DepthFirstTraversalSpec
     * @return null or a Traversal value
     */
    Traversal depthFirstTraversalConnected(String root, DepthFirstTraversalSpec spec) {
        if (spec.preorder && spec.preorder(vertices[root]) == Traversal.STOP) {
            spec.colors[root] = TraversalColor.GREY
            return Traversal.STOP
        }
        spec.colors[root] = TraversalColor.GREY

        Set<Edge> adjacentEdges = traverseEdges(root)
        for (int index = 0; index < adjacentEdges.size(); index++) { //cannot stop and each() call on adjacentEdges
            Edge edge = adjacentEdges[index]
            String connectedName = root == edge.one ? edge.two : edge.one
            if(spec.classifyEdge && spec.classifyEdge(edge, root, connectedName, spec.colors[connectedName]) == Traversal.STOP) {
                return Traversal.STOP
            }
            if (spec.colors[connectedName] == TraversalColor.WHITE) {
                if (Traversal.STOP == depthFirstTraversalConnected(connectedName, spec)) {
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
     * executes closure on each {@link Vertex} in breadth first order. See {@link #breadthFirstTraversal} for details.
     * @param closure
     */
    void eachBfs(Closure closure) {
        eachBfs(null, closure)
    }

    /**
     * executes closure on each {@link Vertex} in breadth first order starting at the given root {@link Vertex}. See {@link #breadthFirstTraversal} for details.
     * @param root
     * @param closure
     */
    void eachBfs(String root, Closure closure) {
        breadthFirstTraversal {
            delegate.root = root
            visit { vertex ->
                closure(vertex)
                return null
            }
        }
    }

    /**
     * Executes closure on each {@link Vertex} in breadth first order. If the closure returns true the {@link Vertex} is
     * returned.
     * @param closure closure to execute on each {@link Vertex}
     * @return first {@link Vertex} where closure returns true
     */
    Vertex findBfs(Closure closure) {
        findBfs(null, closure)
    }

    /**
     * Executes closure on each {@link Vertex} in breadth first order starting at root. If the closure returns true the
     * {@link Vertex} is returned.
     * @param root where to start breadth first traversal
     * @param closure closure to execute on each {@link Vertex}
     * @return first {@link Vertex} where closure returns true
     */
    Vertex findBfs(String root, Closure closure) {
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
     * Executes closure on each vertex in breadth first order. object is the initial value passed to the closure. Each returned
     * value from the closure is passed to the next call.
     * @param object
     * @param closure
     * @return object returned from the final call to closure.
     */
    def injectBfs(Object object, Closure closure) {
        injectBfs(null, object, closure)
    }

    /**
     * Executes closure on each vertex in breadth first order starting at root. object is the initial value passed to the closure. Each returned
     * value from the closure is passed to the next call.
     * @param root
     * @param object
     * @param closure
     * @return object returned from the final call to closure.
     */
    def injectBfs(String root, Object object, Closure closure) {
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
     * Runs closure on each vertex in breadth first order. The vertices where closure returns true are returned.
     * @param closure to run on each vertex
     * @return the vertices where closure returns true
     */
    def findAllBfs(Closure closure) {
        findAllBfs(null, closure)
    }

    /**
     * Runs closure on each vertex in breadth first order starting at root. The vertices where closure returns true are returned.
     * @param root the vertex to start from
     * @param closure to run on each vertex
     * @return the vertices where closure returns true
     */
    def findAllBfs(String root, Closure closure) {
        injectBfs(root, []) { result, vertex ->
            if(closure(vertex)) {
                result << vertex
            }
            result
        }
    }

    /**
     * Runs closure on each vertex in breadth first order collecting the result.
     * @param closure to run on each vertex
     * @return the results from closure
     */
    def collectBfs(Closure closure) {
        collectBfs(null, closure)
    }

    /**
     * Runs closure on each vertex in breadth first order, starting at root, collecting the result.
     * @param root vertex to start at
     * @param closure to run on each vertex
     * @return the results from closure
     */
    def collectBfs(String root, Closure closure) {
        injectBfs(root, []) { result, vertex ->
            result << closure(vertex)
        }
    }

    /**
     * configures a breadth first traversal with the given closure using
     * breadthFirstTraversalSpec().
     *
     * Once the spec is configured traversal(this.&breadthFirstTraversalConnected, spec) is called.
     *
     * @param specClosure
     * @return
     */
    Traversal breadthFirstTraversal(Closure specClosure) {
        def spec = breadthFirstTraversalSpec(specClosure)
        traversal(this.&breadthFirstTraversalConnected, spec)
    }


    /**
     * Performs a breadth first traversal on a connected component of the graph starting
     * at the vertex identified by root. The behavior of the traversal is determined by
     * spec.colors and spec.visit.
     *
     * Traversal.STOP - It is possible to stop the traversal early by returning this value
     * in visit.
     * @param root the root of the vertex to start at
     * @param spec the BreadthFirstTraversalSpec
     * @return null or a Traversal value
     */
    Traversal breadthFirstTraversalConnected(String root, BreadthFirstTraversalSpec spec) {
        if (!vertices[root]) {
            throw new IllegalArgumentException("Could not find $root in graph")
        }
        def traversal = spec.visit(vertices[root])
        if (traversal == Traversal.STOP) {
            spec.colors[root] = TraversalColor.GREY
            return traversal
        }
        spec.colors[root] = TraversalColor.GREY
        Queue<String> queue = new LinkedList<>()
        queue << root
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
     * Creates a {@link VertexSpec}
     * @param name
     * @return a {@link VertexSpec} with name set to the property name.
     */
    def propertyMissing(String name) {
        VertexSpec.newInstance(name:name)
    }

    /**
     * Creates a {@link VertexSpec}. The result is similar to calling {@link VertexSpec#newInstance(Map)}
     * <pre>
     *     VertexSpec.newInstance([name:name + args[0])
     * </pre>
     * @param name
     * @param args
     * @return a {@link VertexSpec}
     */
    def methodMissing(String name, args) {
        if(args.size() == 0) {
            return VertexSpec.newInstance(name:name)
        }

        if(args.size() == 1) {
            return VertexSpec.newInstance([name:name] + args[0])
        }
    }
}
