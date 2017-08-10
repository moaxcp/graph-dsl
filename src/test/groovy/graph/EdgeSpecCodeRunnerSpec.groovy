package graph

import graph.trait.Mapping
import graph.trait.Weight
import graph.type.undirected.EdgeSpecCodeRunner
import spock.lang.Specification

class EdgeSpecCodeRunnerSpec extends Specification {
    Graph graph = new Graph()
    EdgeSpecCodeRunner runner
    Edge edge

    def setup() {
        edge = graph.edge('step1', 'step2')
        runner = new EdgeSpecCodeRunner(graph:graph, edge:edge)
    }

    def 'can renameOne'() {
        when:
        runner.runCode {
            renameOne 'step3'
        }

        then:
        edge.one == 'step3'
    }

    def 'can renameTwo'() {
        when:
        runner.runCode {
            renameTwo 'step3'
        }

        then:
        edge.two == 'step3'
    }

    def 'can add traits'() {
        when:
        runner.runCode {
            traits Mapping, Weight
        }

        then:
        edge.delegate instanceof Mapping
        edge.delegate instanceof Weight
    }

    def 'cannot setOne'() {
        when:
        runner.runCode {
            one = 'step45'
        }

        then:
        thrown MissingPropertyException
    }

    def 'cannot setTwo'() {
        when:
        runner.runCode {
            two = 'step43'
        }

        then:
        thrown MissingPropertyException
    }
}
