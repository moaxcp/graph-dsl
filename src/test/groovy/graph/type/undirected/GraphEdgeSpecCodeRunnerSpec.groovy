package graph.type.undirected

import graph.Edge
import graph.Graph
import spock.lang.Specification

class GraphEdgeSpecCodeRunnerSpec extends Specification {
    Graph graph = new Graph()
    EdgeSpecCodeRunner runner
    Edge edge

    def setup() {
        edge = graph.edge('step1', 'step2')
        runner = new EdgeSpecCodeRunner(graph, edge)
    }

    def 'can changeFrom'() {
        when:
        runner.runCode {
            changeFrom 'step3'
        }

        then:
        edge.from == 'step3'
    }

    def 'can changeTo'() {
        when:
        runner.runCode {
            changeTo 'step3'
        }

        then:
        edge.to == 'step3'
    }
}
