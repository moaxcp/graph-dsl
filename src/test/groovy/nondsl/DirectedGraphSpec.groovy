package nondsl

import spock.lang.Specification

class DirectedGraphSpec extends GraphBaseSpec {
    def setup() {
        graph.type('directed-graph')
    }
}
