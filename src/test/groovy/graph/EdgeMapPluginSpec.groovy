package graph

import spock.lang.Specification

class EdgeMapPluginSpec extends Specification  {

    def graph = new Graph()

    def 'can apply plugin'() {
        when:
        graph.apply EdgeMapPlugin

        then:
        graph.plugins.contains(EdgeMapPlugin)
    }

    def 'existing edges are maps'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.apply EdgeMapPlugin

        then:
        graph.edges.size() == 1
        graph.edges.every {
            it.delegate instanceof Mapping
        }
    }

    def 'new edges are also maps'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.apply EdgeMapPlugin

        and:
        graph.edge 'step2', 'step3'

        then:
        graph.edges.size() == 2
        graph.edges.every {
            it.delegate instanceof Mapping
        }
    }
}
