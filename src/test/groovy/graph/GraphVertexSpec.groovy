package graph

import spock.lang.Specification

class GraphVertexSpec extends Specification {

    def graph = new Graph()

    def 'can add vertex to graph'() {
        when:
        graph.vertex 'step1'

        then:
        graph.vertices.contains(new Vertex(name: 'step1'))
    }

    def 'can add vertex with closure to graph'() {
        setup:
        def vertexName

        when:
        graph.vertex 'step1', {
            vertexName = name
        }

        then:
        vertexName == 'step1'
    }
}
