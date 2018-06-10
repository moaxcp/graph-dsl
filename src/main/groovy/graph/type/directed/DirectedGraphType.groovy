package graph.type.directed

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
 *     vertices cannot have the same direction. Direction is determined by the values of {@link DirectedEdge#getFrom()}
 *     and {@link DirectedEdge#getTo()}. The values of one and two cannot be the same in any two {@link DirectedEdge}s.
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
    Edge newEdge(Map<String, ?> map) {
        new DirectedEdge(map)
    }

    /**
     * Creates a new {@link graph.type.directed.DirectedVertexSpec} from map.
     * @param map
     * @return
     */
    @Override
    DirectedVertexSpec newVertexSpec(Map<String, ?> map, Closure closure = null) {
        new DirectedVertexSpec(graph, map, closure)
    }

    @Override
    boolean canConvert() {
        if (graph.edges.size() == 0) {
            return true
        }
        Set edges = [] as Set
        !graph.edges.find { Edge current ->
            Edge edge = new DirectedEdge(current)
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
     * @param id  for vertex
     * @return
     */
    Set<? extends Edge> inEdges(Object id) {
        graph.edges.findAll {
            id == it.to
        }
    }

    /**
     * returns number of in edges from vertex
     * @param graph
     * @param id
     * @return
     */
    int inDegree(Object id) {
        graph.inEdges(id).size()
    }

    /**
     * returns out edges of vertex.
     * @param graph
     * @param id
     * @return
     */
    Set<? extends Edge> outEdges(Object id) {
        graph.edges.findAll {
            id == it.from
        }
    }

    /**
     * returns number of out edges of vertex
     * @param graph
     * @param id
     * @return
     */
    int outDegree(Object id) {
        graph.outEdges(id).size()
    }

    /**
     * Returns out edges of graph.
     * @param graph
     * @param id
     * @return
     */
    Set<? extends Edge> traverseEdges(Object id) {
        graph.outEdges(id)
    }
}
