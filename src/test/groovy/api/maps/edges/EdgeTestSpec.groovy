package api.maps.edges

import graph.Edge

class EdgeTestSpec extends BaseEdgeSpec {
    def setup() {
        emptyMap = new Edge()
        map = new Edge(from:'step1', to:'step2')
        equalMap = new Edge(from: 'step1', to: 'step2')
        differentMap = new Edge(from: 'step4', to: 'step3')
        firstDifferent = new Edge(from: 'step3', to: 'step2')
        secondDifferent = new Edge(from: 'step1', to: 'step3')
        switched = new Edge(from: 'step2', to: 'step1')
        falseEdges = [new Edge(), new Edge(from:''), new Edge(from:'is true'), new Edge(from:'is true', to:'')]
    }

    def 'equals with both vertices switched'() {
        expect:
        map == switched
    }
}
