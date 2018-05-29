package graph.type

import graph.ConfigSpec
import graph.Edge
import graph.EdgeSpec
import graph.Graph
import graph.Vertex

import graph.VertexSpec

import java.awt.image.BufferedImage

/**
 * Classes implementing this interface are able to change the behavior of a {@link graph.Graph}. A Type can change the
 * base class of all {@link graph.Vertex} and {@link graph.Edge} objects to implement the  behavior needed but a Type
 * should not add traits to delegates. A Type is required to provide factories for {@link Edge}, {@link Vertex},
 * {@link EdgeSpec}, and {@link VertexSpec}. Subclasses can be returned by these factory methods which allow different
 * behavior in the graph. For instance {@link Edge} methods can return {@link graph.type.directed.DirectedEdge} objects
 * and {@link graph.type.undirected.UndirectedVertexSpec} methods can return
 * {@link graph.type.directed.DirectedVertexSpec} objects.
 */
interface Type {

    void setGraph(Graph graph)

    Set<? extends Edge> traverseEdges(Object key)

    /**
     * Creates a new {@link graph.Edge}
     * @return the new {@link graph.Edge}
     */
    Edge newEdge(Map<String, ?> map)

    /**
     * Creates a new {@link graph.Vertex}
     * @return the new {@link Vertex}
     */
    Vertex newVertex(Map<String, ?> map)

    EdgeSpec newEdgeSpec(Map<String, ?> map)

    /**
     * Creates a new {@link EdgeSpec}
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param map  specification for spec
     * @return the new {@link EdgeSpec}
     */
    EdgeSpec newEdgeSpec(Map<String, ?> map, Closure closure)

    /**
     * Creates a new {@link VertexSpec} from a map.
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param map specification for spec
     * @return the new {@link graph.VertexSpec}
     */
    VertexSpec newVertexSpec(Map<String, ?> map)

    VertexSpec newVertexSpec(Map<String, ?> map, Closure closure)

    boolean canConvert()

    /**
     * Converts the type of behavior of a {@link graph.Graph}.
     * @param graph  to convert type
     */
    void convert()

    boolean isMultiGraph()

    boolean isDirected()

    boolean isWeighted()
}
