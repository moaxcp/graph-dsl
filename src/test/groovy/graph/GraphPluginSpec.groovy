package graph

import spock.lang.Specification

class GraphPluginSpec extends Specification {
    def 'plugin class must implement Plugin'() {
        given: 'a graph'
        Graph graph = new Graph()

        when: 'plugin called with class that does not implement Plugin'
        graph.plugin(String)

        then: 'IllegalArgumentException is thrown'
        thrown IllegalArgumentException
    }
}
