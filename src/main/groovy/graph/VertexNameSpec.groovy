package graph

class VertexNameSpec {
    String name

    VertexSpec toVertexSpec() {
        new VertexSpec(name:name)
    }
}
