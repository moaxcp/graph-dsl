package api

import graph.Graph
import graph.plugin.Plugin
import spock.lang.Specification

abstract class GraphBaseSpec extends Specification {
    class FakePlugin implements Plugin {
        void apply(Graph graph) {

        }

        void setGraph(Graph graph) {

        }
    }
    Graph graph = new Graph()

    def 'missing method throws MissingMethodException'() {
        when:
        graph.iAmMissing()

        then:
        thrown MissingMethodException
    }

    def 'method missing in plugin'() {
        given:
        graph.plugin(FakePlugin)

        when:
        graph.iAmMissing()

        then:
        thrown MissingMethodException
    }
}
