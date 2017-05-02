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
    def 'dynamic method and property returns VertexSpec with name set'() {
        expect:
        spec instanceof VertexSpec
        spec.name =='step1'

        where:
        spec << [new Graph().step1, new Graph().step1(), new Graph().step2(name:'step1')]
    }

    def 'dynamic method with Map returns VertexSpec'() {
        when:
        VertexSpec spec = new Graph().step1(x:'y')

        then:
        spec.name == 'step1'
    }

    def 'dynamic method with closure returns VertexSpec'() {
        when:
        VertexSpec spec = new Graph().step1 {}

        then:
        spec.name == 'step1'
        spec.runnerCode != null
    }

    def 'dynamic method with map and closure returns VertexSpec'() {
        when:
        VertexSpec spec = new Graph().step1(traits:Mapping) { edgesFirst 'step2' }

        then:
        spec.name == 'step1'
        spec.traits == [Mapping] as Set<Class>
        spec.runnerCode != null
    }
}
