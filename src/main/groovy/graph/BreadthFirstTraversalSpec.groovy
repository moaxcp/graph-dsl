package graph

/**
 * Specification for a BreadFirstTraversal. Contains actions that are called
 * when an event happens during the traversal.
 */
class BreadthFirstTraversalSpec extends TraversalSpec {
    private Closure visitClosure

    /**
     * Returns the visit closure
     * @return
     */
    Closure getVisit() {
        visitClosure
    }

    /**
     * sets the visit closure
     * @param visitClosure
     * @return
     */
    void visit(Closure visitClosure) {
        this.visitClosure = visitClosure
    }
}
