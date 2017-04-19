package graph

import spock.lang.Specification

class BreadthFirstSearchMethods extends Specification {

    Graph graph = new Graph()
    def defaultOrder = []
    def orderFromD = []

    def setup() {
        graph.with {
            vertex 'A', [connectsTo:['B', 'D']]
            vertex 'B', [connectsTo:['C', 'D']]
            vertex 'D', [connectsTo:['C', 'E', 'A']]
        }
        graph.breadthFirstTraversal {
            visit { vertex ->
                defaultOrder << vertex.name
            }
        }
        graph.breadthFirstTraversal {
            root = 'D'
            visit { vertex ->
                orderFromD << vertex.name
            }
        }
    }

    def 'eachBfs is in breadthFirstOrder'() {
        when:
        def result = []
        graph.eachBfs {
            result << it.name
        }

        then:
        defaultOrder == result
    }

    def 'eachBfs can start at different root'() {
        when:
        def result = []
        graph.eachBfs('D') {
            result << it.name
        }

        then:
        orderFromD == result
    }

    def 'findBfs is in breadth first order'() {
        when:
        def result = []
        graph.findBfs {
            result << it.name
            false
        }

        then:
        defaultOrder == result
    }

    def 'findBfs can start at different root'() {
        when:
        def result = []
        graph.findBfs('D') {
            result << it.name
            false
        }

        then:
        orderFromD == result
    }

    def 'findBfs can find vertex'() {
        when:
        Vertex vertex = graph.findBfs {
            it.name == 'D'
        }

        then:
        vertex.name == 'D'
    }

    def 'injectBfs is in breadth first order'() {
        when:
        def result = []
        graph.injectBfs(result) { list, vertex ->
            list << vertex.name
            list
        }

        then:
        defaultOrder == result
    }

    def 'injectBfs can start at different root'() {
        when:
        def result = []
        graph.injectBfs('D', result) { list, vertex ->
            list << vertex.name
            list
        }

        then:
        orderFromD == result
    }
}
