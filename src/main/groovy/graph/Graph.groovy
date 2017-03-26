package graph

import groovy.transform.PackageScope

/**
 * Implementation of a Graph. Vertices are represented as key/value pairs in a map. The edges connect the keys in
 * the map to form a graph. The values in the map are the contents of the vertices. This makes it easy to represent
 * a graph as string vertices with edges that connect strings.
 */
class Graph {
    final def vertices = [:] as LinkedHashMap<String, Vertex>
    def edges = [] as LinkedHashSet
    def plugins = [] as LinkedHashSet
    def EdgeFactory edgeFactory = new UnDirectedEdgeFactory()
    def VertexFactory vertexFactory = new DefaultVertexFactory()

    /**
     * An enum defining traversal status. A value from this enum can be returned
     * from one of the closures passed to search methods changing the behavior of a traversal.
     */
    def enum Traversal {
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
    def static graph(Closure c) {
        def graph = new Graph()
        graph.with(c)
        graph
    }

    /**
     * returns the vertices as an unmodifiableMap
     * @return
     */
    def getVertices() {
        Collections.unmodifiableMap(vertices)
    }

    /**
     * returns the edges as an unmodifiable set
     * @return
     */
    def getEdges() {
        Collections.unmodifiableSet(edges)
    }

    /**
     * returns plugins as an unmodifiable set
     * @return
     */
    def getPlugins() {
        Collections.unmodifiableSet(plugins)
    }

    /**
     * applies a plugin to this graph.
     * @param pluginClass
     * @return
     */
    def apply(Class pluginClass) {
        if (plugins.contains(pluginClass)) {
            throw new IllegalArgumentException("$pluginClass.name is already applied.")
        }
        if (!pluginClass.interfaces.contains(Plugin)) {
            throw new IllegalArgumentException("$pluginClass.name does not implement Plugin")
        }
        plugins << pluginClass
        def plugin = pluginClass.newInstance()
        plugin.apply(this)
    }

    /**
     * Adds a vertex object directly.
     * @param vertex
     * @return true if add was successful.
     */
    @PackageScope
    boolean addVertex(Vertex vertex) {
        vertices[vertex.name] = vertex
    }

    /**
     * Finds the vertex with the given name or creates a new one.
     * @param name
     * @return the resulting vertex
     */
    Vertex vertex(String name) {
        if(!name) {
            throw new IllegalArgumentException("!name failed. Name must be groovy truth.")
        }
        Vertex vertex = vertices[name] ?: vertexFactory.newVertex(name)
        vertices[name] = vertex
        vertex
    }

    Vertex vertex(@DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = makeVertexSpec(closure)
        Vertex vertex = vertex(spec.name)
        applySpecToVertexAndGraph(spec, vertex)
        vertex
    }

    @PackageScope
    VertexSpec makeVertexSpec(@DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec spec = new VertexSpec()

        Closure code = closure.rehydrate(spec, this, this)
        code()

        spec
    }

    @PackageScope
    void applySpecToVertexAndGraph(VertexSpec spec, Vertex vertex) {
        if (spec.traits) {
            vertex.delegateAs(spec.traits as Class[])
        }
        spec.connectsTo.each {
            edge vertex.name, it
        }
        if(spec.config) {
            vertex << spec.config
        }

        vertices[vertex.name] = vertex
    }

    Vertex vertex(Map<String, ?> map) {
        VertexSpec spec = makeVertexSpec(map)
        Vertex vertex = vertex(spec.name)
        applySpecToVertexAndGraph(spec, vertex)
        vertex
    }

    @PackageScope
    VertexSpec makeVertexSpec(Map<String, ?> map) {
        VertexSpec spec = new VertexSpec(name:map.name)
        if (map.traits) {
            spec.traits(map.traits as Class[])
        }
        if (map.connectsTo) {
            spec.connectsTo(map.connectsTo as String[])
        }
        if(map.config) {
            spec.config(map.config)
        }

        spec
    }

    Vertex vertex(String name, Closure closure) {
        //TODO if vertex is renamed in closure must rename across all edges.
        VertexSpec spec = makeVertexSpec(closure)
        Vertex vertex = vertex(name)
        applySpecToVertexAndGraph(spec, vertex)
        vertex
    }

    Vertex vertex(String name, Map<String, ?> map) {
        VertexSpec spec = makeVertexSpec(map)
        Vertex vertex = vertex(name)
        applySpecToVertexAndGraph(spec, vertex)
        vertex
    }

    /**
     * Adds a Vertex using the provided map to set its properties. The Vertex
     * is then added to vertices overwriting any previous Vertex with the given
     * name entry in the map.
     *
     * The provided closure is called with the vertex as the delegate.
     *
     * If the map contains a traits entry the value should contain a list of traits or classes
     * to apply to the Vertex as traits. The resulting Vertex has all of those traits
     * applied in the order of the list.
     *
     * @param map a map with a name entry. There can be an optional traits entry with a list of classes as a value.
     * @param closure
     * @return the resulting vertex
     */
    Vertex vertex(Map<String, ?> map, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec mapSpec = makeVertexSpec(map)
        VertexSpec closureSpec = makeVertexSpec(closure)
        Vertex vertex = vertex(map.name)
        applySpecToVertexAndGraph(mapSpec, vertex)
        applySpecToVertexAndGraph(closureSpec, vertex)
        vertex
    }

    Vertex vertex(String name, Map<String, ?> map, @DelegatesTo(VertexSpec) Closure closure) {
        VertexSpec mapSpec = makeVertexSpec(map)
        VertexSpec closureSpec = makeVertexSpec(closure)
        Vertex vertex = vertex(name)
        applySpecToVertexAndGraph(mapSpec, vertex)
        applySpecToVertexAndGraph(closureSpec, vertex)
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
     * Creates a map with the entries one and two set to the params one and two.
     * This map is then passed to edge(map, closure = null).
     * @param one
     * @param two
     * @param closure
     * @return the resulting edge
     */
    def edge(String one, String two, closure = null) {
        edge(one: one, two: two, closure)
    }

    /**
     * Uses map to create an Edge object. And adds it to edges. If an edge already
     * exists between the to vertices it cannot be added and an IllegalArgumentException is thrown.
     *
     * The provided closure is called with the edge as the delegate.
     *
     * If the map contains a traits entry its value should contain a list of traits
     * or classes to apply to the Edge as traits. The resulting Edge as all of those traits
     * applied in the order of the list.
     *
     * @param map
     * @param closure
     * @throws IllegalArgumentException
     * @return the resulting edge
     */
    def edge(map, closure = null) {
        def e = edgeFactory.newEdge(map.one, map.two)
        def edge = edges.find { it == e } ?: e

        edge = map.traits?.inject(edge) { val, it ->
            val.delegateAs(it)
        } ?: edge

        if (map.trait) {
            edge.delegateAs(map.trait)
        }

        if (closure) {
            closure.delegate = edge
            closure()
        }

        edges.add(edge)

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
        def edge = adjacentEdges(parentName).findAll {
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
    Set<Edge> adjacentEdges(name) {
        edges.findAll {
            name == it.one || name == it.two
        }
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

        Set<Edge> adjacentEdges = adjacentEdges(root)
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
            Set<Edge> adjacentEdges = adjacentEdges(current)
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
}
