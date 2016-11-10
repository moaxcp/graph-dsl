package graph

class Graph {
    def vertices = [:]
    def edges = []

    def vertice(name) {
        vertices[name] = new Vertice(name: name)
    }

    def vertice(name, closure) {
        def vertice = new Vertice(name: name)
        closure.delegate = vertice
        closure.call()
        vertices[name] = vertice
    }

    def edge(one, two) {
        def edge = new Edge(one: one, two: two)
        validateEdge(edge)
        edges += edge
    }

    def edge(one, two, closure) {
        def edge = new Edge(one: one, two: two)
        validateEdge(edge)
        closure.delegate = edge
        closure.call()
        edges += edge
    }

    def validateEdge(edge) {
        if(!validEdge(edge)) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }
    }

    def validEdge(edge) {
        edges.any {
            (it.one == edge.one || it.one == edge.two) && (it.two == edge.one || it.two == edge.two)
        }
    }
}
