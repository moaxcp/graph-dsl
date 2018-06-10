package api.maps.edges

import graph.type.directed.DirectedEdge

class DirectedEdgeSpec extends BaseEdgeSpec {
    def setup() {
        emptyMap = new DirectedEdge()
        map = new DirectedEdge(from:'step1', to:'step2')
        equalMap = new DirectedEdge(from:'step1', to:'step2')
        differentMap = new DirectedEdge(from:'step3', to:'step4')
        firstDifferent = new DirectedEdge(from:'step3', to:'step2')
        secondDifferent = new DirectedEdge(from:'step1', to:'step4')
        switched = new DirectedEdge(from:'step2', to:'step1')
        falseEdges = [new DirectedEdge(), new DirectedEdge(from:''), new DirectedEdge(from:'is true'), new DirectedEdge(from:'is true', to:'')]
    }

    def 'equals with both vertices switched'() {
        expect:
        map != switched
    }
}
