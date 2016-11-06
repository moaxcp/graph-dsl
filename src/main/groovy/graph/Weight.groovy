package graph

trait Weight {

    private Closure weight

    def getWeight() {
        weight.delegate = this
        weight.call()
    }

    def weight(Closure c) {
        weight = c
    }
}