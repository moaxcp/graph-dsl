package graph

/**
 * Specification for a DepthFirstSearch. Contains actions that are called when and
 * even happens during the search.
 */
class DepthFirstSearchSpec {
    private Closure preorder
    private Closure inorder
    private Closure postorder

    /**
     * returns the preorder event.
     * @return
     */
    def getPreorder() {
        return preorder
    }

    /**
     * returns the inorder event.
     * @return
     */
    def getInorder() {
        return inorder
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
        this
    }

    /**
     * method to set the inorder event
     * @param inorder
     * @return
     */
    def inorder(Closure inorder) {
        this.inorder = inorder
        this
    }

    /**
     * method to set the postorder event
     * @param postorder
     * @return
     */
    def postorder(Closure postorder) {
        this.postorder = postorder
        this
    }
}
