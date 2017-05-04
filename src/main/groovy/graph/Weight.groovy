package graph

trait Weight {

    private Closure weight

    int getWeight() {
        weight.delegate = this
        weight.call()
    }

    def weight(Closure c) {
        weight = c
    }
}
