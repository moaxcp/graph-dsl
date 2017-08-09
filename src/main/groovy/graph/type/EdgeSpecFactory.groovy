package graph.type

import graph.ConfigSpec

interface EdgeSpecFactory {
    EdgeSpec newEdgeSpec(ConfigSpec spec)
}
