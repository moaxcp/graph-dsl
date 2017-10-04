package graph.type

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.Vertex
import graph.type.undirected.GraphEdgeSpec
import graph.type.undirected.GraphVertexSpec

/**
 * Classes implementing this interface are able to change the behavior of a {@link graph.Graph}. A Type can change the
 * base class of all {@link graph.Vertex} and {@link graph.Edge} objects to implement the  behavior needed but a Type
 * should not add traits to delegates. A Type is required to provide factories for {@link Edge}, {@link Vertex},
 * {@link GraphEdgeSpec}, and {@link GraphVertexSpec}. Subclasses can be returned by these factory methods which allow different
 * behavior in the graph. For instance {@link Edge} methods can return {@link DirectedEdge} objects and {@link graph.type.undirected.GraphVertexSpec}
 * methods can return {@link DirectedVertexSpec} objects.
 */
interface Type {

    void setGraph(Graph graph)
    /**
     * Creates a new {@link graph.Edge}
     * @param one  name of {@link graph.Vertex}
     * @param two  name of {@link graph.Vertex}
     * @return the new {@link graph.Edge}
     */
    Edge newEdge(String one, String two)

    /**
     * Creates a new {@link graph.Vertex}
     * @param name  name of the the {@link Vertex}
     * @return the new {@link Vertex}
     */
    Vertex newVertex(String name)

    /**
     * Creates a new {@link GraphEdgeSpec}
     * @param graph  {@link graph.Graph} to create the {@link GraphEdgeSpec} for
     * @param map  specification for spec
     * @return the new {@link GraphEdgeSpec}
     */
    GraphEdgeSpec newEdgeSpec(Map<String, ?> map)

    /**
     * Creates a new {@link GraphEdgeSpec} from a {@link ConfigSpec}
     * @param graph  {@link graph.Graph} to create the {@link GraphEdgeSpec} for
     * @param spec  {@link ConfigSpec} for the new {@link GraphEdgeSpec}
     * @return the new {@link GraphEdgeSpec}
     */
    GraphEdgeSpec newEdgeSpec(ConfigSpec spec)

    /**
     * Creates a new {@link GraphVertexSpec} from a map.
     * @param graph  {@link graph.Graph} to create the {@link GraphEdgeSpec} for
     * @param map specification for spec
     * @return the new {@link graph.type.undirected.GraphVertexSpec}
     */
    GraphVertexSpec newVertexSpec(Map<String, ?> map)

    /**
     * Creates a new {@link GraphVertexSpec} from a {@link ConfigSpec}
     * @param graph  {@link graph.Graph} to create the {@link GraphEdgeSpec} for
     * @param spec  {@link ConfigSpec} for the new {@link GraphVertexSpec}
     * @return the new {@link GraphVertexSpec}
     */
    GraphVertexSpec newVertexSpec(ConfigSpec spec)

    boolean canConvert()

    /**
     * Converts the type of behavior of a {@link graph.Graph}.
     * @param graph  to convert type
     */
    void convert()
}
