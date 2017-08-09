package graph.type.directed

import graph.ConfigSpec
import graph.type.VertexSpec
import graph.type.VertexSpecFactory

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
