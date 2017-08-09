package graph.type.undirected

import graph.ConfigSpec
import graph.type.VertexSpec
import graph.type.VertexSpecFactory

class UnDirectedVertexSpecFactory implements VertexSpecFactory {
    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new VertexSpec(map)
    }

    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        VertexSpec vspec = new VertexSpec(spec.map)
        vspec.runnerCode spec.closure
        vspec
    }
}
