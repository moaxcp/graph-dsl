package graph

class TraversalResult {
    List<?> roots = []
    Map<Object, TraversalColor> colors = [:]
    TraversalState traversal
}
