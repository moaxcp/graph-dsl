package graph.type.undirected

import graph.Edge
import graph.Graph

/**
 * Delegate of the runnerCode closure in {@link graph.EdgeSpec}. This provides methods and properties that can be used
 * in the closure. Method and property missing is delegated to the {@link Edge}
 */
class EdgeSpecCodeRunner {
    private final Graph graph
    private final Edge edge

    EdgeSpecCodeRunner(Graph graph, Edge edge) {
        this.graph = graph
        this.edge = edge
    }

    /**
     * Gets the graph the {@link Edge} has been added to. This can be used inside the runnerCode to access the graph.
     * @return the graph the {@link Edge} has been added to.
     */
    Graph getGraph() {
        graph
    }

    //todo edge returned should not be able to set one or two. A trait can be applied to prevent this.
    // This is an issue on all edge methods.
    /**
     * Gets the {@link Edge} that has been added. This can be used inside the runnerCode to access the edge.
     * @return the edge that has been added.
     */
    Edge getEdge() {
        edge
    }

    /**
     * Changes edge.one to changeOne. This allows edge.one to point to a different {@link graph.Vertex}
     * @param changeOne the new key for edge.one
     */
    void changeOne(Object changeOne) {
        graph.newEdgeSpec([one:edge.one, two:edge.two, changeOne:changeOne]).apply()
    }

    /**
     * Changes edge.two to changeTwo. This allows edge.two to point to a different {@link graph.Vertex}.
     * @param changeTwo  the new key for edge.two
     */
    void changeTwo(Object changeTwo) {
        graph.newEdgeSpec([one:edge.one, two:edge.two, changeTwo:changeTwo]).apply()
    }

    /**
     * Calls the missing method on the {@link Edge}.
     * @param name
     * @param args
     * @return
     */
    @SuppressWarnings('NoDef')
    def methodMissing(String name, args) {
        edge.invokeMethod(name, args)
    }

    /**
     * Gets the property from the {@link Edge}.
     * @param name
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        if (edge[name]) {
            edge[name]
        } else {
            throw new MissingPropertyException("Missing ${edge[name]}")
        }
    }

    /**
     * Sets the property on the {@link Edge}.
     * @throws MissingPropertyException when attempting to set one or two.
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        if (name == 'one') {
            throw new MissingPropertyException('Cannot set one in dsl. Consider using changeOne method')
        }
        if (name == 'two') {
            throw new MissingPropertyException('Cannot set two in dsl. Consider using changeTwo method')
        }
        edge[name] = value
    }

    /**
     * This object becomes the delegate of the closure. The closure is run with a {@link Closure#DELEGATE_FIRST}
     * strategy and then it is called.
     * @param closure
     */
    void runCode(@DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
