package graph.type

import graph.ConfigSpec

/**
 * Creates new {@link EdgeSpec}s.
 */
interface EdgeSpecFactory {
    /**
     * Creates a new {@link EdgeSpec} from spec.
     * @param spec
     * @return
     */
    EdgeSpec newEdgeSpec(ConfigSpec spec)
}
