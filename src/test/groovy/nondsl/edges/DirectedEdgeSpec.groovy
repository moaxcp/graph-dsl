package nondsl.edges

import graph.type.directed.DirectedEdge
import spock.lang.Specification

public class DirectedEdgeSpec extends BaseEdgeSpec {
    def setup() {
        edge = new DirectedEdge(one:'step1', two:'step2')
        equalEdge = new DirectedEdge(one:'step1', two:'step2', weight:10)
        bothDifferent = new DirectedEdge(one:'step3', two:'step4')
        firstDifferent = new DirectedEdge(one:'step3', two:'step2')
        secondDifferent = new DirectedEdge(one:'step1', two:'step4')
        switched = new DirectedEdge(one:'step2', two:'step1')
    }

    def 'equals with both vertices switched'() {
        expect:
        edge != switched
    }
}
