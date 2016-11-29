package graph

import spock.lang.Specification

class GraphSpec extends Specification {
    def graph = new Graph()

    def setup() {
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
            edge 'step4', 'step1'
            edge 'step1', 'step1'
        }
    }

    def 'can visit vertex with result'() {
        setup:
        def results = []
        def visited = []

        when:
        graph.visitVertex 'step1', visited, results, {
            name
        }

        then:
        results == ['step1']
        visited == ['step1']
    }

    def 'does not visit vertex twice'() {
        setup:
        def results = []
        def visited = ['step1']

        when:
        graph.visitVertex 'step1', visited, results, {
            name
        }

        then:
        results == []
        visited == ['step1']
    }

    def 'can get correct first unvisited vertex'() {
        setup:
        def visited = []

        when:
        def name = graph.getUnvisitedVertex(visited)

        then:
        name == 'step1'
    }

    def 'can get correct second unvisited vertex'() {
        setup:
        def visited = ['step1']

        when:
        def name = graph.getUnvisitedVertex(visited)

        then:
        name == 'step2'
    }

    def 'can get unvisited child right'() {
        setup:
        def visited = ['step1', 'step2', 'step4']

        when:
        def childName = graph.getUnvisitedChildName(visited, 'step1')

        then:
        childName == 'step3'
    }

    def 'can get unvisited child left'() {
        setup:
        def visited = ['step1', 'step2', 'step3']

        when:
        def childName = graph.getUnvisitedChildName(visited, 'step1')

        then:
        childName == 'step4'
    }

    def 'can get no unvisited child'() {
        setup:
        def visited = ['step1', 'step2', 'step3', 'step4']

        when:
        def childName = graph.getUnvisitedChildName(visited, 'step1')

        then:
        childName == null

    }

    def 'dfsVerticesCollect by name'() {
        when:
        def results = graph.dfsVerticesCollect { name == 'step1' }

        then:
        results == [true, false, false, false]
    }

    def 'dfsVerticesCollect by name in a disconnected graph'() {
        when:
        graph.vertex('step5')
        def results = graph.dfsVerticesCollect { name == 'step5' }

        then:
        results == [false, false, false, false, true]
    }
}
