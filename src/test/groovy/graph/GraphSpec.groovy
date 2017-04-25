package graph

import spock.lang.Specification
import spock.lang.Unroll

class GraphSpec extends Specification {
    def 'can only apply Plugin once'() {
        setup:
        def graph = new Graph()
        graph.apply DirectedGraphPlugin

        when:
        graph.apply DirectedGraphPlugin

        then:
        thrown IllegalArgumentException
    }

    def 'can only apply a Plugin'() {
        setup:
        def graph = new Graph()

        when:
        graph.apply String

        then:
        thrown IllegalArgumentException
    }

    def 'can use graph method as expected'() {
        setup:
        def c = {
            vertex 'step1'
        }

        when:
        def graph = Graph.graph(c)

        then:
        graph.vertices.size() == 1
    }

    @Unroll
    def 'dynamic method and property returns spec with name set'() {
        expect:
        spec instanceof VertexSpec
        spec.name =='step1'

        where:
        spec << [new Graph().step1, new Graph().step1(), new Graph().step2(name:'step1')]
    }
}
