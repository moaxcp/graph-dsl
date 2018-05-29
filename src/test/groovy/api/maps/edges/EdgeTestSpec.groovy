package api.maps.edges

import graph.Edge

class EdgeTestSpec extends BaseEdgeSpec {
    def setup() {
        emptyMap = new Edge()
        map = new Edge(one:'step1', two:'step2')
        equalMap = new Edge(one: 'step1', two: 'step2')
        differentMap = new Edge(one: 'step4', two: 'step3')
        firstDifferent = new Edge(one: 'step3', two: 'step2')
        secondDifferent = new Edge(one: 'step1', two: 'step3')
        switched = new Edge(one: 'step2', two: 'step1')
        falseEdges = [new Edge(), new Edge(one:''), new Edge(one:'is true'), new Edge(one:'is true', two:'')]
    }

    def 'equals with both vertices switched'() {
        expect:
        map == switched
    }
}
