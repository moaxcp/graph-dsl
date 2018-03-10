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

    static Map classifyEdgesTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        colors[root] = GREY
        Set<Edge> adjacentEdges = graph.traverseEdges(root)
        for(int index = 0; index < adjacentEdges.size(); index++) {
            Edge edge = adjacentEdges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            TraversalColor toColor = colors[connectedKey]
            if(!spec.forrest) {
                spec.forrest = new Graph()
            }
            EdgeType edgeType = edgeType(spec, root, connectedKey, toColor)
            if(edgeType == TREE_EDGE) {
                spec.forrest.vertex root, [rootKey:spec.traversalRoot]
                spec.forrest.vertex connectedKey, [rootKey:spec.traversalRoot]
                spec.forrest.edge(root, connectedKey)
            }
            spec.state = action(root, connectedKey, edgeType)
            if(spec.state == STOP) {
                return spec
            }
            if(!colors[connectedKey] || colors[connectedKey] == WHITE) {
                spec.root = connectedKey
                classifyEdgesTraversal(graph, spec, action)
                if(spec.state == STOP) {
                    return spec
                }
            }
        }
        colors[root] = BLACK
        spec.state = CONTINUE
        spec
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
            case null:
                edgeType = TREE_EDGE
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
        if(traversal == STOP) {
            colors[root] = GREY
            spec.state = STOP
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
                    if(traversal == STOP) {
                        colors[connected] = GREY
                        spec.state = STOP
                        return spec
                    }
                    colors[connected] = GREY
                    queue << connected
                }
            }
            colors[current] = BLACK
        }
        spec.state = CONTINUE
        spec
    }
}
