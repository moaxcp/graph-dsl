package graph

/**
 * Specification for a DepthFirstSearch. Contains actions that are called when and
 * even happens during the search.
 */
class DepthFirstTraversalSpec {
    String root
    Map colors
    private Closure preorder
    private Closure postorder

    /**
     * returns the preorder event.
     * @return
     */
    def getPreorder() {
        return preorder
    }

    /**
     * returns the postorder event.
     * @return
     */
    def getPostorder() {
        return postorder
    }

    /**
     * method to set the preorder event
     * @param preorder
     * @return
     */
    def preorder(Closure preorder) {
        this.preorder = preorder
    }

    /**
     * method to set the postorder event
     * @param postorder
     * @return
     */
    def postorder(Closure postorder) {
        this.postorder = postorder
    }
}
