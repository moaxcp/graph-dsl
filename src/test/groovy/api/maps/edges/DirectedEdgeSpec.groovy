package api.maps.edges

import graph.type.directed.DirectedEdge

class DirectedEdgeSpec extends BaseEdgeSpec {
    def setup() {
        emptyMap = new DirectedEdge()
        map = new DirectedEdge(one:'step1', two:'step2')
        equalMap = new DirectedEdge(one:'step1', two:'step2')
        differentMap = new DirectedEdge(one:'step3', two:'step4')
        firstDifferent = new DirectedEdge(one:'step3', two:'step2')
        secondDifferent = new DirectedEdge(one:'step1', two:'step4')
        switched = new DirectedEdge(one:'step2', two:'step1')
        falseEdges = [new DirectedEdge(), new DirectedEdge(one:''), new DirectedEdge(one:'is true'), new DirectedEdge(one:'is true', two:'')]
    }

    def 'equals with both vertices switched'() {
        expect:
        map != switched
    }
}
