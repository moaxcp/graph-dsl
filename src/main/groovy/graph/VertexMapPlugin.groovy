package graph

class VertexMapPlugin implements Plugin {
    @Override
    def apply(Graph graph) {
        graph.@vertices.each { name, vertex ->
            vertex.delegateAs(Mapping)
        }

        graph.vertexFactory = new VertexFactory() {
            def oldFactory = graph.vertexFactory

            @Override
            Vertex newVertex(String name) {
                def vertex = oldFactory.newVertex(name)
                vertex.delegateAs(Mapping)
            }
        }
    }
}
