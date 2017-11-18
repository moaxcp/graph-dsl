package graph.type.directed

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.type.undirected.GraphType

/**
 * This type changes the behavior of a {@link Graph} to that of a directed graph.
 * <p>
 *
 * The following modifications are made to the graph:
 * <p>
 * <b>members</b>
 * <dl>
 *     <dt>{@code edges}</dt>
 *     <dd>All edges are converted to {@link DirectedEdge}s and all future edges will be {@link DirectedEdge}s. Any two
 *     vertices may be connected by one or two {@link DirectedEdge}s. Two {@link DirectedEdge}s connecting any two
 *     vertices cannot have the same direction. Direction is determined by the values of {@link DirectedEdge#getOne()}
 *     and {@link DirectedEdge#getTwo()}. The values of one and two cannot be the same in any two {@link DirectedEdge}s.
 *     </dd>
 * </dl>
 */
class DirectedGraphType extends GraphType {

    /**
     * Returns a new DirectedEdge with the given parameters.
     * @param one
     * @param two
     * @return a new DirectedEdge
     */
    @Override
    Edge newEdge(Object one, Object two, Map delegate = null) {
        if (delegate) {
            new DirectedEdge(one:one, two:two, delegate:delegate)
        } else {
            new DirectedEdge(one:one, two:two)
        }
    }

    /**
     * Creates a new {@link graph.type.directed.DirectedVertexSpec} from map.
     * @param map
     * @return
     */
    @Override
    DirectedVertexSpec newVertexSpec(Map<String, ?> map) {
        new DirectedVertexSpec(graph, map)
    }

    /**
     * Creates a new {@link graph.type.directed.DirectedVertexSpec} from spec.
     * @param spec
     * @return
     */
    @Override
    DirectedVertexSpec newVertexSpec(ConfigSpec spec) {
        new DirectedVertexSpec(graph, spec.map, spec.closure)
    }

    @Override
    boolean canConvert() {
        if (graph.edges.size() == 0) {
            return true
        }
        Set edges = [] as Set
        !graph.edges.find { Edge current ->
            Edge edge = new DirectedEdge(one:current.one, two:current.two)
            !edges.add(edge)
        }
    }

    @Override
    boolean isDirected() {
        true
    }

    /**
     * Returns in-edges of vertex
     * @param graph
     * @param key  for vertex
     * @return
     */
    Set<? extends Edge> inEdges(Object key) {
        graph.edges.findAll {
            key == it.two
        }
    }

    /**
     * returns number of in edges from vertex
     * @param graph
     * @param key
     * @return
     */
    int inDegree(Object key) {
        graph.inEdges(key).size()
    }

    /**
     * returns out edges of vertex.
     * @param graph
     * @param key
     * @return
     */
    Set<? extends Edge> outEdges(Object key) {
        graph.edges.findAll {
            key == it.one
        }
    }

    /**
     * returns number of out edges of vertex
     * @param graph
     * @param key
     * @return
     */
    int outDegree(Object key) {
        graph.outEdges(key).size()
    }

    /**
     * Returns out edges of graph.
     * @param graph
     * @param key
     * @return
     */
    Set<? extends Edge> traverseEdges(Object key) {
        graph.outEdges(key)
    }

    /**
     * Returns the names of vertices sorted in reverse post order.
     * @return
     */
    Deque<String> reversePostOrderSort() {
        Deque<String> deque = [] as LinkedList<String>
        graph.depthFirstTraversal {
            postorder { vertex ->
                deque.addFirst(vertex.key)
            }
        }
        deque
    }

    /**
     * Runs the given closure on each vertex in reverse post order.
     * @param closure
     */
    void reversePostOrder(Closure closure) {
        Deque<String> deque = graph.reversePostOrderSort()
        deque.each {
            closure(graph.vertices[it])
        }
    }
}
