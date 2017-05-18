package graph

import spock.lang.Ignore
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

    def 'can delete an unconnected Vertex'() {
        setup:
        Graph graph = new Graph()
        graph.vertex 'step1'

        when:
        graph.delete('step1')

        then:
        graph.vertices.size() == 0
    }

    def 'cannot delete connected vertex'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.delete('step1')

        then:
        thrown IllegalStateException
    }

    def 'can delete edge'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.deleteEdge('step1', 'step2')

        then:
        graph.edges.size() == 0
    }

    def 'can replaceEdges'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        graph.replaceEdges {
            new DirectedEdge(one:it.one, two:it.two, delegate:it.delegate)
        }

        then:
        graph.edges.size() == 4
        graph.edges.every { it instanceof DirectedEdge }
    }

    def 'can replaceEdgesSet'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        graph.replaceEdgesSet(new TreeSet<>(new OrderBy([{ it.one }, { it.two }])))

        then:
        graph.edges.size() == 4
    }
}
