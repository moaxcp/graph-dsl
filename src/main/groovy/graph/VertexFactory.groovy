package graph

/**
 * Base interface for all vertex factories.
 */
interface VertexFactory {
    /**
     * Creates a new {@link Vertex} with the given name.
     * @param name - the name to give the {@link Vertex}.
     * @return the resulting {@link Vertex}.
     */
    Vertex newVertex(String name)
}
