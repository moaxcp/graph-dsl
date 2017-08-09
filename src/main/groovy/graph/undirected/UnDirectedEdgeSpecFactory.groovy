package graph.undirected

import graph.ConfigSpec
import graph.EdgeSpec
import graph.EdgeSpecFactory

class UnDirectedEdgeSpecFactory implements EdgeSpecFactory {
    @Override
    EdgeSpec newEdgeSpec(ConfigSpec spec) {
        EdgeSpec espec = new EdgeSpec(spec.map)
        espec.runnerCode spec.closure
        espec
    }
}
