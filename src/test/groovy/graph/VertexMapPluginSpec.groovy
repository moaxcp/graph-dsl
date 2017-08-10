package graph

import graph.plugin.VertexMapPlugin
import graph.trait.Mapping
import spock.lang.Specification

class VertexMapPluginSpec extends Specification {
    def graph = new Graph()

    def 'can apply plugin'() {
        when:
        graph.apply VertexMapPlugin

        then:
        graph.plugins.contains(VertexMapPlugin)
    }

    def 'existing vertices are maps'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.apply VertexMapPlugin

        then:
        graph.vertices.size() == 1
        graph.vertices.every {
            it.value.delegate instanceof Mapping
        }
    }

    def 'new vertices are also maps'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.apply VertexMapPlugin

        and:
        graph.vertex 'step2'

        then:
        graph.vertices.size() == 2
        graph.vertices.every {
            it.value.delegate instanceof Mapping
        }
    }
}
