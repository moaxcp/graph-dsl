package graph

/**
 * This plugin makes a Graph behave like a directed graph.
 */
class DirectedGraphPlugin implements Plugin {

    /**
     * The following modifications are made to the graph:
     * <p>
     * members
     * edges - converted to DirectedEdges
     * edgeFactory - changed to a DirectedEdgeFactory
     * <p>
     * methods
     * The static graph methods in this class are added to the graph with graph as the first parameter.
     * @param graph
     * @return
     */
    void apply(Graph graph) {
        graph.@edges = graph.@edges.collect { edge ->
            new DirectedEdge(one:edge.one, two:edge.two)
        } as LinkedHashSet

        graph.edgeFactory = new DirectedEdgeFactory()

        graph.metaClass.inEdges = this.&inEdges.curry(graph)
        graph.metaClass.inDegree = this.&inDegree.curry(graph)
        graph.metaClass.outEdges = this.&outEdges.curry(graph)
        graph.metaClass.outDegree = this.&outDegree.curry(graph)
        graph.metaClass.adjacentEdges = this.&adjacentEdges.curry(graph)
        graph.metaClass.reversePostOrderSort = this.&reversePostOrderSort.curry(graph)
        graph.metaClass.reversePostOrder = this.&reversePostOrder.curry(graph)
    }

    /**
     * Returns in edges from vertex with name in graph.
     * @param graph
     * @param name
     * @return
     */
    static Set<? extends Edge> inEdges(Graph graph, String name) {
        graph.@edges.findAll {
            name == it.two
        }
    }

    /**
     * returns number of in edges from vertex with name in graph.
     * @param graph
     * @param name
     * @return
     */
    static int inDegree(Graph graph, String name) {
        graph.inEdges(name).size()
    }

    /**
     * returns out edges with vertex name in graph.
     * @param graph
     * @param name
     * @return
     */
    static Set<? extends Edge> outEdges(Graph graph, String name) {
        graph.@edges.findAll {
            name == it.one
        }
    }

    /**
     * returns number of out edges with vertex name in graph.
     * @param graph
     * @param name
     * @return
     */
    static int outDegree(Graph graph, String name) {
        graph.outEdges(name).size()
    }

    /**
     * returns out edges with vertex name in graph. This method replaces adjacentEdges in {@link Graph}.
     * @param graph
     * @param name
     * @return
     */
    static Set<? extends Edge> adjacentEdges(Graph graph, String name) {
        graph.outEdges(name)
    }

    /**
     * Returns the names of vertices sorted in reverse post order for the given graph.
     * @param graph
     * @return
     */
    static Deque<String> reversePostOrderSort(Graph graph) {
        Deque<String> deque = new LinkedList<String>()
        graph.depthFirstTraversal {
            postorder { vertex ->
                deque.addFirst(vertex.name)
            }
        }
        deque
    }

    /**
     * Runs the given closure on each vertex in graph in reverse post order.
     * @param graph
     * @param closure
     */
    static void reversePostOrder(Graph graph, Closure closure) {
        Deque<String> deque = graph.reversePostOrderSort()
        deque.each {
            closure(graph.@vertices[it])
        }
    }
}
