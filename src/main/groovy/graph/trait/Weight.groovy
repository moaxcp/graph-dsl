package graph.trait

/**
 * Adds a weight attribute to {@link Vertex} and {@link Edge} objects.
 */
trait Weight {

    private Closure weightClosure = { 0 }

    /**
     * Gets the weight by calling the weightClosure with this as the delegate.
     * @return the resulting weight.
     */
    int getWeight() {
        weightClosure.delegate = this
        weightClosure.call()
    }

    /**
     * Sets the weight closure to closure.
     * @param closure - to run when getWeight is called.
     */
    void weight(Closure closure) {
        weightClosure = closure
    }
}
