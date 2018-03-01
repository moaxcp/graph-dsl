package api.edges

import graph.type.directed.DirectedEdge

public class DirectedEdgeSpec extends BaseEdgeSpec {
    def setup() {
        emptyEdge = new DirectedEdge()
        edge = new DirectedEdge(one:'step1', two:'step2')
        equalEdge = new DirectedEdge(one:'step1', two:'step2', weight:10)
        bothDifferent = new DirectedEdge(one:'step3', two:'step4')
        firstDifferent = new DirectedEdge(one:'step3', two:'step2')
        secondDifferent = new DirectedEdge(one:'step1', two:'step4')
        switched = new DirectedEdge(one:'step2', two:'step1')
        falseEdges = [new DirectedEdge(), new DirectedEdge(one:''), new DirectedEdge(one:'is true'), new DirectedEdge(one:'is true', two:'')]
    }

    def 'equals with both vertices switched'() {
        expect:
        edge != switched
    }
}
