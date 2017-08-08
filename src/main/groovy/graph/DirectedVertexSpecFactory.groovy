package graph

class DirectedVertexSpecFactory implements VertexSpecFactory {
    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new DirectedVertexSpec(map)
    }

    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        VertexSpec vspec = new DirectedVertexSpec(spec.map)
        vspec.runnerCode spec.closure
        vspec
    }
}
