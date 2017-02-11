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
        graph.edges.every {
            it instanceof LinkedHashMap
        }
    }

    def 'new edges are maps'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.apply EdgeMapPlugin

        and:
        graph.edge 'step2', 'step3'

        then:
        graph.edges.every {
            it instanceof LinkedHashMap
        }
    }
}
