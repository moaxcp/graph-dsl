package graph

/**
 * Specification for a DepthFirstTraversal. Contains actions that are called when an
 * event happens during the traversal.
 */
class DepthFirstTraversalSpec extends TraversalSpec {
    private Closure preorderClosure
    private Closure postorderClosure
    private Closure classifyEdgeClosure

    /**
     * returns the preorder event.
     * @return
     */
    Closure getPreorder() {
        preorderClosure
    }

    /**
     * returns the postorder event.
     * @return
     */
    Closure getPostorder() {
        postorderClosure
    }

    /**
     * returns the classifyEdge event.
     * @return
     */
    Closure getClassifyEdge() {
        classifyEdgeClosure
    }

    /**
     * method to set the preorder event
     * @param preorderClosure
     * @return
     */
    void preorder(Closure preorderClosure) {
        this.preorderClosure = preorderClosure
    }

    /**
     * method to set the postorder event
     * @param postorderClosure
     * @return
     */
    void postorder(Closure postorderClosure) {
        this.postorderClosure = postorderClosure
    }

    /**
     * method to set the classifyEdge event
     * @param classifyEdgeClosure
     */
    void classifyEdge(Closure classifyEdgeClosure) {
        this.classifyEdgeClosure = classifyEdgeClosure
    }
}
