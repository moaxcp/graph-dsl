package graph

class UnDirectedVertexSpecFactory implements VertexSpecFactory {
    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new VertexSpec(map)
    }

    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        VertexSpec vspec = new VertexSpec(name:spec.name)
        vspec = vspec.overlay(new VertexSpec(spec.map))
        vspec.runnerCode spec.closure
        vspec
    }
}
