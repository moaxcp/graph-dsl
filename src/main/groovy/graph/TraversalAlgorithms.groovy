package graph

import static graph.TraversalColor.*

class TraversalAlgorithms {
    private TraversalAlgorithms() {

    }

    static Map preOrderTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        if(graph.getVertex(root) && action(graph.getVertex(root)) == TraversalState.STOP) {
            colors[root] = GREY
            spec.state = TraversalState.STOP
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
                if(spec.state == TraversalState.STOP) {
                    return spec
                }
            }
        }
        colors[root] = BLACK
        spec.state = TraversalState.CONTINUE
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
                if(spec.state == TraversalState.STOP) {
                    return spec
                }
            }
        }
        if(graph.getVertex(root) && action(graph.getVertex(root)) == TraversalState.STOP) {
            colors[root] = BLACK
            spec.state = TraversalState.STOP
            return spec
        }
        colors[root] = BLACK
        spec.state = TraversalState.CONTINUE
        spec
    }

    static Map classifyEdgesTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        colors[root] = GREY
        Set<Edge> adjacentEdges = graph.traverseEdges(root)
        for(int index = 0; index < adjacentEdges.size(); index++) {
            Edge edge = adjacentEdges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            TraversalColor toColor = colors[connectedKey]
            EdgeType edgeType = edgeType(spec, root, connectedKey, toColor)
            action(root, connectedKey, edgeType)
            if(colors[connectedKey] == WHITE) {
                spec.root = connectedKey
                classifyEdgesTraversal(graph, spec, action)
                if(spec.state == TraversalState.STOP) {
                    return spec
                }
            }
        }
        colors[root] = BLACK
        spec.state = TraversalState.CONTINUE
        spec
    }

    private static EdgeType edgeType(Map map, Object from, Object to, TraversalColor color) {
        if(!map.forrest) {
            map.forrest = new Graph()
        }
        EdgeType edgeType
        switch(color) {
            case WHITE:
                map.forrest.edge(from, to)
                edgeType = EdgeType.TREE_EDGE
                break
            case GREY:
                edgeType = EdgeType.BACK_EDGE
                break
            case BLACK:
                if(map.forrest.vertices[to]) {
                    edgeType = EdgeType.CROSS_EDGE
                } else {
                    edgeType = EdgeType.FORWARD_EDGE
                }
                break
            default:
                throw new IllegalStateException("Edge from $from to $to needs to be WHITE, GREY, or BLACK.")
        }
        edgeType
    }

    static Map breadthFirstTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        TraversalState traversal = action(graph.getVertex(root))
        if(traversal == TraversalState.STOP) {
            colors[root] = GREY
            spec.state = TraversalState.STOP
            return spec
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
                if(colors[connected] == WHITE) {
                    traversal = action(graph.getVertex(connected))
                    if(!traversal) {
                        throw new IllegalStateException('Invalid TraversalState value returned by action.')
                    }
                    if(traversal == TraversalState.STOP) {
                        colors[connected] = GREY
                        spec.state = TraversalState.STOP
                        return spec
                    }
                    colors[connected] = GREY
                    queue << connected
                }
            }
            colors[current] = BLACK
        }
        spec.state = TraversalState.CONTINUE
        spec
    }
}
