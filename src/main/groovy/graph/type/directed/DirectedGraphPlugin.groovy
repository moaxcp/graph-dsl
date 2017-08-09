package graph.type.directed

import graph.Edge
import graph.Graph
import graph.plugin.Plugin

/**
 * This plugin changes the behavior of a {@link Graph} to that of a directed graph.
 * <p>
 *
 * The following modifications are made to the graph:
 * <p>
 * <b>members</b>
 * <dl>
 *     <dt>edges</dt>
 *     <dd>All edges are converted to {@link DirectedEdge}s and all future edges will be {@link DirectedEdge}s. Any two
 *     vertices may be connected by one or two {@link DirectedEdge}s. Two {@link DirectedEdge}s connecting any two
 *     vertices cannot have the same direction. Direction is determined by the values of {@link DirectedEdge#getOne()}
 *     and {@link DirectedEdge#getTwo()}. The values of one and two cannot be the same in any two {@link DirectedEdge}s.
 *     </dd>
 *     <dt>edgeFactory</dt>
 *     <dd>changed to a {@link DirectedEdgeFactory} This causes all future {@link Edge}s added to the {@link Graph} to
 *     be {@link DirectedEdge}s.</dd>
 *     <dt>vertexSpecFactory</dt>
 *     <dd>change to a {@link DirectedVertexSpecFactory}. This causes all future {@link VertexSpec}s used in the
 *     {@link Graph} to be {@link DirectedVertexSpec}. The new configuration options in {@link DirectedVertexSpec} are
 *     available for use in the vertex dsl (connectsFrom).</dd>
 * </dl>
 * <b>methods</b>
 * <p>
 * The static graph methods in this class are added to the graph with graph as the first parameter. The methods can be
 * used in the {@link Graph} directly at runtime. traverseEdges in {@link Graph} is modified.
 * <dl>
 *     <dt>{@code Set<? extends Edge> inEdges(String name)}</dt>
 *     <dd>returns the set of in-edges for a given vertex</dd>
 *     <dt>{@code int inDegree(String name)}</dt>
 *     <dd>returns the in-degree of a vertex</dd>
 *     <dt>{@code Set<? extends Edge> outEdges(String name)}</dt>
 *     <dd>returns the set of out-edges for a given vertex</dd>
 *     <dt>{@code int outDegree(Graph graph, String name)}</dt>
 *     <dd>returns the out-degree of a vertex</dd>
 *     <dt>{@code Set<? extends Edge> traverseEdges(String name)}</dt>
 *     <dd>returns the traverse-edges for traversal algorithms. For directed graph this is out-edges.</dd>
 *     <dt>{@code Deque<String> reversePostOrderSort()}</dt>
 *     <dd>returns vertex names in reverse-post-order</dd>
 *     <dt>{@code void reversePostOrder(Closure closure)}</dt>
 *     <dd>traverses the graph in reverse-post-order</dd>
 * </dl>
 */
class DirectedGraphPlugin implements Plugin {

    /**
     * Applies the plugin to a {@link graph.Graph}.
     * @param graph  to apply plugin
     */
    void apply(Graph graph) {
        graph.replaceEdges { edge ->
            new DirectedEdge(one:edge.one, two:edge.two, delegate:edge.delegate)
        }

        graph.edgeFactory = new DirectedEdgeFactory()
        graph.vertexSpecFactory = new DirectedVertexSpecFactory();

        graph.metaClass.inEdges = this.&inEdges.curry(graph)
        graph.metaClass.inDegree = this.&inDegree.curry(graph)
        graph.metaClass.outEdges = this.&outEdges.curry(graph)
        graph.metaClass.outDegree = this.&outDegree.curry(graph)
        graph.metaClass.traverseEdges = this.&traverseEdges.curry(graph)
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
    static Set<? extends Edge> traverseEdges(Graph graph, String name) {
        graph.outEdges(name)
    }

    /**
     * Returns the names of vertices sorted in reverse post order for the given graph.
     * @param graph
     * @return
     */
    static Deque<String> reversePostOrderSort(Graph graph) {
        Deque<String> deque = [] as LinkedList<String>
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
