package graph.type.undirected

import graph.ConfigSpec
import graph.type.EdgeSpec
import graph.type.EdgeSpecFactory

class UnDirectedEdgeSpecFactory implements EdgeSpecFactory {
    @Override
    EdgeSpec newEdgeSpec(ConfigSpec spec) {
        EdgeSpec espec = new EdgeSpec(spec.map)
        espec.runnerCode spec.closure
        espec
    }
}
