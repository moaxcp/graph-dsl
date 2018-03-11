package graph

import static graph.TraversalColor.*
import static graph.EdgeType.*
import static graph.TraversalState.*

class TraversalAlgorithms {
    private TraversalAlgorithms() {

    }

    static Map preOrderTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        if(graph.getVertex(root) && action(graph.getVertex(root)) == STOP) {
            colors[root] = GREY
            spec.state = STOP
            return spec
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
        if(graph.getVertex(root) && action(graph.getVertex(root)) == STOP) {
            colors[root] = BLACK
            spec.state = STOP
            return spec
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
            spec.state = action(root, connectedKey, toColor)
            if(spec.state == STOP) {
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

    private static EdgeType edgeType(Map map, Object from, Object to, TraversalColor color) {
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
                    colors[connected] = GREY
                    if(!traversal) {
                        throw new IllegalStateException('Invalid TraversalState value returned by action.')
                    }
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
