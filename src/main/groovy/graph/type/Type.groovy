package graph.type

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.Vertex

/**
 * Classes implementing this interface are able to change the behavior of a {@link graph.Graph}. A Type can change the
 * base class of all {@link graph.Vertex} and {@link graph.Edge} objects to implement the  behavior needed but a Type
 * should not add traits to delegates. A Type is required to provide factories for {@link Edge}, {@link Vertex},
 * {@link EdgeSpec}, and {@link VertexSpec}. Subclasses can be returned by these factory methods which allow different
 * behavior in the graph. For instance {@link Edge} methods can return {@link DirectedEdge} objects and {@link VertexSpec}
 * methods can return {@link DirectedVertexSpec} objects.
 */
interface Type {
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
     * Creates a new {@link EdgeSpec}
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param map  specification for spec
     * @return the new {@link EdgeSpec}
     */
    EdgeSpec newEdgeSpec(Graph graph, Map<String, ?> map)

    /**
     * Creates a new {@link EdgeSpec} from a {@link ConfigSpec}
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param spec  {@link ConfigSpec} for the new {@link EdgeSpec}
     * @return the new {@link EdgeSpec}
     */
    EdgeSpec newEdgeSpec(Graph graph, ConfigSpec spec)

    /**
     * Creates a new {@link VertexSpec} from a map.
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param map specification for spec
     * @return the new {@link VertexSpec}
     */
    VertexSpec newVertexSpec(Graph graph, Map<String, ?> map)

    /**
     * Creates a new {@link VertexSpec} from a {@link ConfigSpec}
     * @param graph  {@link graph.Graph} to create the {@link EdgeSpec} for
     * @param spec  {@link ConfigSpec} for the new {@link VertexSpec}
     * @return the new {@link VertexSpec}
     */
    VertexSpec newVertexSpec(Graph graph, ConfigSpec spec)

    boolean canConvert(Graph graph)

    /**
     * Converts the type of behavior of a {@link graph.Graph}.
     * @param graph  to convert type
     */
    void convert(Graph graph)
}
