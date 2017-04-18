package graph

import spock.lang.Specification

class BreadthFirstSearchMethods extends Specification {

    Graph graph = new Graph()

    def setup() {
        graph.with {
            vertex 'A', [connectsTo:['B', 'D']]
            vertex 'B', [connectsTo:['C', 'D']]
            vertex 'D', [connectsTo:['C', 'E', 'A']]
        }
    }

    def 'eachBfs is in breadthFirstOrder'() {
        setup:
        def expected = []
        graph.breadthFirstTraversal {
            root = 'A'
            visit { vertex ->
                expected << vertex.name
            }
        }

        when:
        def result = []
        graph.eachBfs {
            result << it.name
        }

        then:
        expected == result
    }

    def 'eachBfs can start a different root'() {
        setup:
        def expected = []
        graph.breadthFirstTraversal {
            root = 'D'
            visit { vertex ->
                expected << vertex.name
            }
        }

        when:
        def result = []
        graph.eachBfs('D') {
            result << it.name
        }

        then:
        expected == result
    }
}
