package graph

import static graph.TraversalColor.*
import static graph.EdgeType.*
import static graph.TraversalState.*

/**
 * Traversal algorithms which are expected to be called from
 * {@link graph.Graph.traversal(Closure algorithm, Object root, Map<Object, TraversalColor> colors, Closure action)}.
 */
class TraversalAlgorithms {
    private TraversalAlgorithms() {

    }

    /**
     * Uses the specification {@code spec} to performs a pre-order depth first traversal on a component of
     * {@code graph} returning the results. The traversals starts at {@code spec.root}. {@code action} must return a
     * {@link TraversalState}. As the traversal runs, {@code action} is called with each vertex as a parameter. If
     * {@code action} returns {@link TraversalState#STOP} the algorithm stops.
     * <p>
     * {@code spec} is the specification for the algorithm. {@code spec} is returned so it can be reused on other
     * components of {@code graph}. {@code spec} must contain the following.
     * <dl>
     *     <dt>{@link Object} {@code root}</dt>
     *     <dd>root of traversal. root will change for each recursive call in this algorithm. (not null)</dd>
     *     <dt>{@link Map<Object, TraversalColor>} {@code colors}</dt>
     *     <dd>colors for each vertex. This is updated as the traversal runs. If the color of a vertex is not found it
     *     is assumed to be {@link TraversalColor#WHITE}. (not null, can be empty)</dd>
     * </dl>
     * <p>
     *     The following will be added to {@code spec} by this algorithm.
     * </p>
     * <dl>
     *     <dt>{@link TraversalState} {@code state}</dt>
     *     <dd>state of traversal returned by action.</dd>
     * </dl>
     * @param graph  to perform algorithm on
     * @param spec  specification of algorithm
     * @param action  taken on each vertex
     * @return resulting specification. will always return the spec param.
     */
    static Map preOrderTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        if(graph.getVertex(root)) {
            TraversalState state = action(graph.getVertex(root))
            if(!state) {
                throw new NullPointerException('action cannot return null TraversalState.')
            }
            if(state == STOP) {
                colors[root] = GREY
                spec.state = STOP
                return spec
            }
        }
        colors[root] = GREY

        Set<Edge> edges = graph.traverseEdges(root)
        for(int index = 0; index < edges.size(); index++) {
            Edge edge = edges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            if(!colors[connectedKey] || colors[connectedKey] == WHITE) {
                colors[connectedKey] = WHITE
                spec.root = connectedKey
                preOrderTraversal(graph, spec, action)
                if(spec.state == STOP) {
                    return spec
                }
            }
        }
        colors[root] = BLACK
        spec.state = CONTINUE
        spec
    }

    static Map postOrderTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        colors[root] = GREY
        Set<Edge> adjacentEdges = graph.traverseEdges(root)
        for(int index = 0; index < adjacentEdges.size(); index++) {
            Edge edge = adjacentEdges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            if(!colors[connectedKey] || colors[connectedKey] == WHITE) {
                colors[connectedKey] = WHITE
                spec.root = connectedKey
                postOrderTraversal(graph, spec, action)
                if(spec.state == STOP) {
                    return spec
                }
            }
        }
        if(graph.getVertex(root)) {
            TraversalState state = action(graph.getVertex(root))
            if(!state) {
                throw new NullPointerException('action cannot return null TraversalState.')
            }
            if(state == STOP) {
                colors[root] = BLACK
                spec.state = STOP
                return spec
            }
        }
        colors[root] = BLACK
        spec.state = CONTINUE
        spec
    }

    static Map preOrderEdgesTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        colors[root] = GREY
        Set<Edge> adjacentEdges = graph.traverseEdges(root)
        for(int index = 0; index < adjacentEdges.size(); index++) {
            Edge edge = adjacentEdges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            TraversalColor toColor = colors[connectedKey] ?: WHITE
            TraversalState state = action(root, connectedKey, toColor)
            if(!state) {
                throw new NullPointerException('action cannot return null TraversalState.')
            }
            if(state == STOP) {
                spec.state = STOP
                return spec
            }
            if(!colors[connectedKey] || colors[connectedKey] == WHITE) {
                spec.root = connectedKey
                preOrderEdgesTraversal(graph, spec, action)
                if(spec.state == STOP) {
                    return spec
                }
            }
        }
        colors[root] = BLACK
        spec.state = CONTINUE
        spec
    }

    static Map classifyEdgesTraversal(Graph graph, Map spec, Closure action) {
        if(!spec.forrest) {
            spec.forrest = new Graph()
        }
        def edgesAction = { Object from, Object to, TraversalColor toColor ->
            EdgeType edgeType = edgeType(spec, from, to, toColor)
            if(edgeType == TREE_EDGE) {
                spec.forrest.vertex from, [rootKey:spec.traversalRoot]
                spec.forrest.vertex to, [rootKey:spec.traversalRoot]
                spec.forrest.edge(from, to)
            }
            return action(from, to, edgeType)
        }
        return preOrderEdgesTraversal(graph, spec, edgesAction)
    }

    static EdgeType edgeType(Map map, Object from, Object to, TraversalColor color) {
        EdgeType edgeType
        switch(color) {
            case WHITE:
                edgeType = TREE_EDGE
                break
            case GREY:
                edgeType = BACK_EDGE
                break
            case BLACK:
                def fromTree = map.forrest.vertices[from].rootKey
                def toTree = map.forrest.vertices[to].rootKey
                if(fromTree == toTree) {
                    edgeType = FORWARD_EDGE
                } else {
                    edgeType = CROSS_EDGE
                }
                break
        }
        edgeType
    }

    static Map breadthFirstTraversal(Graph graph, Map spec, Closure action) {
        if(!spec.root) {
            throw new NullPointerException('spec.root is null')
        }
        if(!action) {
            throw new NullPointerException('action is null')
        }
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        if(graph.getVertex(root)) {
            TraversalState traversal = action(graph.getVertex(root))
            if(!traversal) {
                throw new NullPointerException('action cannot return null TraversalState.')
            }
            if(traversal == STOP) {
                colors[root] = GREY
                spec.state = STOP
                return spec
            }
        }
        colors[root] = GREY
        Queue<Object> queue = [] as Queue<Object>
        queue << root
        while(!queue.isEmpty()) {
            Object current = queue.poll()
            spec.root = current
            Set<Edge> adjacentEdges = graph.traverseEdges(current)
            for (int i = 0; i < adjacentEdges.size(); i++) {
                Edge edge = adjacentEdges[i]
                Object connected = current == edge.one ? edge.two : edge.one
                if(!colors[connected] || colors[connected] == WHITE) {
                    TraversalState traversal = action(graph.getVertex(connected))
                    if(!traversal) {
                        throw new NullPointerException('action cannot return null TraversalState.')
                    }
                    colors[connected] = GREY
                    if(traversal == STOP) {
                        spec.root = connected
                        spec.state = STOP
                        return spec
                    }
                    queue << connected
                }
            }
            colors[current] = BLACK
        }
        spec.state = CONTINUE
        spec
    }
}
