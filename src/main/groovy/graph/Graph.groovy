package graph

class Graph {
    def vertices = [] as Set
    def edges = [] as Set

    def vertex(name) {
        vertices += new Vertex(name: name)
    }

    def vertex(name, closure) {
        def vertex = new Vertex(name: name)
        closure.delegate = vertex
        closure.call()
        vertices += vertex
    }

    private void addEdge(edge) {
        def added = edges.add edge
        if(!added) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }
    }

    def edge(one, two) {
        def edge = new Edge(one: one, two: two)
        addEdge(edge)
    }

    def edge(one, two, closure) {
        def edge = new Edge(one: one, two: two)
        closure.delegate = edge
        closure.call()
        addEdge(edge)
    }
}
