package graph

/**
 * Base class for all EdgeSpec objects. This class provides access for {@link Type} to add new edges.
 */
abstract class EdgeSpec {
    Graph graph

    protected EdgeSpec(Graph graph) {
        this.graph = graph
    }

    protected void addEdge(Edge edge) {
        graph.addEdge(edge)
    }

    abstract Edge apply()
}