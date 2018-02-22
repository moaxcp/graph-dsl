package graph

import static graph.TraversalColor.*

class TraversalAlgorithms {
    private TraversalAlgorithms() {

    }

    static Map preOrderTraversal(Graph graph, Map spec, Closure action) {
        Object root = spec.root
        Map<Object, TraversalColor> colors = spec.colors
        if(action(graph.getVertex(root)) == TraversalState.STOP) {
            colors[root] == GREY
            spec.state = TraversalState.STOP
            return spec
        }
        colors[root] = GREY

        Set<Edge> adjacentEdges = graph.traverseEdges(root)
        for(int index = 0; index < adjacentEdges.size(); index++) {
            Edge edge = adjacentEdges[index]
            Object connectedKey = root == edge.one ? edge.two : edge.one
            if(colors[connectedKey] == WHITE) {
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
            if(colors[connectedKey] == WHITE) {
                spec.root = connectedKey
                postOrderTraversal(graph, spec, action)
                if(spec.state == TraversalState.STOP) {
                    return spec
                }
            }
        }
        if(action(graph.getVertex(root)) == TraversalState.STOP) {
            TraversalState state = TraversalState.STOP
            colors[root] = BLACK
            spec.state = state
            return spec
        }
        colors[root] = BLACK
        spec.state = TraversalState.CONTINUE
        spec
    }

    static Map classifyEdgesTraversal(Graph graph, Map spec, Closure action) {
        //todo implement. Add call tree and map of classification to edge list to spec
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
