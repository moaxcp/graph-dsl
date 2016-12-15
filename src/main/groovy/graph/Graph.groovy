package graph

/**
 * Implementation of a Graph. Vertices are represented as key/value pairs in a map. The edges connect the keys in
 * the map to form a graph. The values in the map are the contents of the vertices. This makes it easy to represent
 * a graph as string vertices with edges that connect strings.
 */
class Graph {
    final def vertices = [:]
    final def edges = [] as Set
    def enum Traversal {
        STOP
    }

    def getVertices() {
        Collections.unmodifiableMap(vertices)
    }

    def getEdges() {
        Collections.unmodifiableSet(edges)
    }

    def vertex(String name, closure = null) {
        if (closure) {
            vertex(name: name, closure)
        } else {
            vertex(name: name)
        }
    }

    def vertex(map, closure = null) {
        def vertex = new Vertex(name: map.name)

        vertex = map.traits?.inject(vertex) { val, it ->
            val.withTraits(it)
        } ?: vertex

        if (closure != null) {
            closure.delegate = vertex
            closure.call()
        }

        vertices[map.name] = vertex
    }

    def edge(String one, String two, closure = null) {
        if (closure) {
            edge(one: one, two: two, closure)
        } else {
            edge(one: one, two: two)
        }
    }

    def edge(map, closure = null) {
        def edge = new Edge(one: map.one, two: map.two)

        edge = map.traits?.inject(edge) { val, it ->
            val.withTraits(it)
        } ?: edge

        if (closure) {
            closure.delegate = edge
            closure.call()
        }

        if (!edges.add(edge)) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }
    }

    def visitVertexDFSCollect(name, visited, results, closure) {
        if (!visited.contains(name)) {
            closure.delegate = vertices[name]
            def result = closure.call()
            visited << name
            results << result
        }
    }

    def visitVertexDFS(name, visited, closure) {
        if (!visited.contains(name)) {
            closure.delegate = vertices[name]
            def result = closure(vertices[name])
            visited << name
            result ? vertices[name] : null
        }
    }

    def getUnvisitedVertex(visited) {
        vertices.find { k, v ->
            !visited.contains(k)
        }?.key
    }

    def getUnvisitedChildName(visited, parentName) {
        def edge = edges.find {
            if (it.one != it.two && (parentName == it.one || parentName == it.two)) {
                def childName = parentName == it.one ? it.two : it.one
                return !(childName in visited)
            }
        }
        if (!edge) {
            return null
        }
        parentName == edge.one ? edge.two : edge.one
    }

    //TODO add methods for find, findAll, inject
    def dfsVerticesCollect(closure) {
        def results = []
        def visited = []
        def stack = [] as LinkedList

        def root = getUnvisitedVertex(visited)
        while (root) {
            visitVertexDFSCollect(root, visited, results, closure)
            stack.push(root)
            while (!stack.isEmpty()) {
                def parent = stack.peek()
                def child = getUnvisitedChildName(visited, parent)
                if (child) {
                    visitVertexDFSCollect(child, visited, results, closure)
                    stack.push(child)
                } else {
                    stack.pop()
                }
            }
            root = getUnvisitedVertex(visited)
        }
        results
    }

    def adjacentEdges(name) {
        edges.findAll {
            name == it.one || name == it.two
        }
    }

    def depthFirstTraversal(previsit, postvisit = null) {
        def visited = []
        def root = getUnvisitedVertex(visited)
        while (root) {
            def traversal = depthFirstTraversalConnected(root, visited, previsit, postvisit)
            if(traversal == Traversal.STOP) {
                return Traversal.STOP
            }
            root = getUnvisitedVertex(visited)
        }
    }

    def depthFirstTraversalConnected(name, visited, previsit, postvisit = null) {
        previsit.delegate = vertices[name]
        if (previsit() == Traversal.STOP) {
            return Traversal.STOP
        }
        visited << name
        adjacentEdges(name).each { edge ->
            def connectedName = name == edge.one ? edge.two : edge.one
            if (!visited.contains(connectedName)) {
                def traversal = depthFirstTraversal(connectedName, visited, previsit, postvisit)
                if (traversal == Traversal.STOP) {
                    return Traversal.STOP
                }
            }
        }
        if(!postvisit) {
            return
        }
        postvisit.delegate = vertices[name]
        if (postvisit() == Traversal.STOP) {
            return Traversal.STOP
        }

    }


    def dfsOld(closure) {
        def visited = []
        def stack = [] as LinkedList

        def root = getUnvisitedVertex(visited)
        while (root) {
            def result = visitVertexDFS(root, visited, closure)
            if (result) {
                return result
            }
            stack.push(root)
            while (!stack.isEmpty()) {
                def parent = stack.peek()
                def child = getUnvisitedChildName(visited, parent)
                if (child) {
                    result = visitVertexDFS(child, visited, closure)
                    if (result) {
                        return result
                    }
                    stack.push(child)
                } else {
                    stack.pop()
                }
            }
            root = getUnvisitedVertex(visited)
        }
    }

}
