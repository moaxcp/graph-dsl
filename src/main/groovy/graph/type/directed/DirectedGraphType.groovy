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
    Edge newEdge(String one, String two, Object delegate = null) {
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

    /**
     * Returns in edges from vertex with name in graph.
     * @param graph
     * @param name
     * @return
     */
    Set<? extends Edge> inEdges(String name) {
        graph.edges.findAll {
            name == it.two
        }
    }

    /**
     * returns number of in edges from vertex with name in graph.
     * @param graph
     * @param name
     * @return
     */
    int inDegree(String name) {
        graph.inEdges(name).size()
    }

    /**
     * returns out edges with vertex name in graph.
     * @param graph
     * @param name
     * @return
     */
    Set<? extends Edge> outEdges(String name) {
        graph.edges.findAll {
            name == it.one
        }
    }

    /**
     * returns number of out edges with vertex name in graph.
     * @param graph
     * @param name
     * @return
     */
    int outDegree(String name) {
        graph.outEdges(name).size()
    }

    /**
     * returns out edges with vertex name in graph. This method replaces adjacentEdges in {@link Graph}.
     * @param graph
     * @param name
     * @return
     */
    Set<? extends Edge> traverseEdges(String name) {
        graph.outEdges(name)
    }

    /**
     * Returns the names of vertices sorted in reverse post order for the given graph.
     * @param graph
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
     * Runs the given closure on each vertex in graph in reverse post order.
     * @param graph
     * @param closure
     */
    void reversePostOrder(Closure closure) {
        Deque<String> deque = graph.reversePostOrderSort()
        deque.each {
            closure(graph.@vertices[it])
        }
    }
}
