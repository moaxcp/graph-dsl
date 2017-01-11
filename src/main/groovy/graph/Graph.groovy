package graph

/**
 * Implementation of a Graph. Vertices are represented as key/value pairs in a map. The edges connect the keys in
 * the map to form a graph. The values in the map are the contents of the vertices. This makes it easy to represent
 * a graph as string vertices with edges that connect strings.
 */
class Graph {
    final def vertices = [:]
    final def edges = [] as LinkedHashSet

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
    def enum DepthFirstTraversalColor {
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
     * Creates a map with the name key set to the name param. The map
     * and closure are passed to vertex(Map, Clousre)
     * @param name
     * @param closure
     * @return the resulting vertex
     */
    def vertex(String name, Closure closure = null) {
        vertex(name: name, closure)
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
    def vertex(map, Closure closure = null) {
        def vertex = new Vertex(name: map.name)

        vertex = map.traits?.inject(vertex) { val, it ->
            val.withTraits(it)
        } ?: vertex

        if (closure != null) {
            closure.delegate = vertex
            closure()
        }

        vertices[map.name] = vertex
        vertex
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
        def edge = new Edge(one: map.one, two: map.two)

        edge = map.traits?.inject(edge) { val, it ->
            val.withTraits(it)
        } ?: edge

        if (closure) {
            closure.delegate = edge
            closure()
        }

        if (!edges.add(edge)) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }

        edge
    }

    /**
     * Returns the first unvisited vertex name in vertices.
     *
     * @param colors a map of vertex name entries with the value of the DepthFirstTraversalColor
     * @return the first unvisited vertex name in the vertices.
     */
    def getUnvisitedVertexName(colors) {
        vertices.find { k, v ->
            colors[(k)] != DepthFirstTraversalColor.BLACK && colors[k] != DepthFirstTraversalColor.GREY
        }?.key
    }

    /**
     * returns the name of first unvisited child vertex with a parent matching parentName.
     *
     * @param colors a map of vertex name entries with the value of the DepthFirstTraversalColor
     * @param parentName the name of the parent vertex to start searching from
     * @return the name of the first unvisited child vertex
     */
    def getUnvisitedChildName(colors, parentName) {
        def edge = adjacentEdges(parentName).findAll {
            it.one != it.two
        }.find {
            def childName = parentName == it.one ? it.two : it.one
            def color = colors[childName]
            return !(color == DepthFirstTraversalColor.GREY || color == DepthFirstTraversalColor.BLACK)
        }

        if (!edge) {
            return null
        }
        parentName == edge.one ? edge.two : edge.one
    }

    def adjacentEdges(name) {
        edges.findAll {
            name == it.one || name == it.two
        }
    }

    def makeColorMap() {
        vertices.collectEntries { name, vertex ->
            [(name) : DepthFirstTraversalColor.WHITE]
        }
    }

    def depthFirstTraversal(Closure specClosure) {
        def spec = new DepthFirstSearchSpec()
        specClosure.delegate = spec
        specClosure()
        depthFirstTraversal(spec.preorder, spec.postorder)
    }

    def depthFirstTraversal(Closure preorder, Closure postorder) {
        def colors = makeColorMap()
        def name = getUnvisitedVertexName(colors)
        while (name) {
            def traversal = depthFirstTraversalConnected(name, colors, preorder, postorder)
            if(traversal == Traversal.STOP) {
                return Traversal.STOP
            }
            name = getUnvisitedVertexName(colors)
        }
    }

    def depthFirstTraversalConnected(name, colors, preorder, postorder) {
        if (preorder && preorder(vertices[name]) == Traversal.STOP) {
            return Traversal.STOP
        }

        colors[name] = DepthFirstTraversalColor.GREY

        def adjacentEdges = adjacentEdges(name)
        adjacentEdges.eachWithIndex { edge, index ->
            def connectedName = name == edge.one ? edge.two : edge.one
            if (colors[connectedName] == DepthFirstTraversalColor.WHITE) {
                if (Traversal.STOP == depthFirstTraversalConnected(connectedName, colors, preorder, postorder)) {
                    return Traversal.STOP
                }
            }
        }

        if (postorder && postorder(vertices[name]) == Traversal.STOP) {
            return Traversal.STOP
        }
        colors[name] = DepthFirstTraversalColor.BLACK
    }
}
