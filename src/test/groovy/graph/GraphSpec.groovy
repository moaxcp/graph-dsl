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

    def 'dynamic method returns ConfigSpec with name set'() {
        given:
        ConfigSpec spec = new Graph().step1()
        expect:
        spec.map.name =='step1'
    }

    def 'dynamic property returns NameSpec with name set'() {
        setup:
        NameSpec spec = new Graph().step1

        expect:
        spec instanceof NameSpec
        spec.name == 'step1'
    }

    def 'dynamic method with Map returns ConfigSpec'() {
        when:
        ConfigSpec spec = new Graph().step1(x:'y')

        then:
        spec.map.name == 'step1'
        spec.map.x == 'y'
    }

    def 'dynamic method with closure returns ConfigSpec'() {
        when:
        ConfigSpec spec = new Graph().step1 {}

        then:
        spec.map.name == 'step1'
        spec.closure != null
    }

    def 'dynamic method with map and closure returns ConfigSpec'() {
        when:
        ConfigSpec spec = new Graph().step1(traits:Mapping) { edgesFirst 'step2' }

        then:
        spec.map.name == 'step1'
        spec.map.traits == Mapping
        spec.closure != null
    }

    def 'missingMethod with name vertex is illegal'() {
        when:
        ConfigSpec spec = new Graph().methodMissing('vertex', [])

        then:
        thrown IllegalArgumentException
    }

    def 'methodMissing with one bad argument throws Exception'() {
        when:
        new Graph().methodMissing('noVertexSpec', ['one'])

        then:
        thrown MissingMethodException
    }

    def 'methodMissing with two bad arguments throws exception'() {
        when:
        new Graph().methodMissing('noVertexSpec', ['one', 'two'])

        then:
        thrown MissingMethodException
    }

    def 'methodMissing with a bad argument and map throws exception'() {
        when:
        new Graph().methodMissing('noVertexSpec', [[one:'one'], 'two'])

        then:
        thrown MissingMethodException
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

    def 'set must be empty in replaceEdgesSet'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        def set = new TreeSet<>(new OrderBy([{ it.one }, { it.two }]))
        set.add(new Edge(one:'step5', two:'step6'))
        graph.replaceEdgesSet(set)

        then:
        thrown IllegalArgumentException
    }

    def 'edgeTraits adds traits to all old  and new edges'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step2', 'step3'

        when:
        graph.edgeTraits(Mapping)

        and:
        graph.edgeTraits(Mapping)
        graph.edge 'step3', 'step4'
        graph.edge 'step4', 'step5'

        then:
        graph.edges.every { edge ->
            edge.delegate instanceof Mapping
        }
    }

    def 'vertexTraits adds traits to all old and new vertices'() {
        setup:
        Graph graph = new Graph()
        graph.vertex 'step1'
        graph.vertex 'step2'

        when:
        graph.vertexTraits(Mapping)

        and:
        graph.vertexTraits(Mapping)
        graph.vertex 'step3'
        graph.vertex 'step4'

        then:
        graph.vertices.every { name, vertex ->
            vertex.delegate instanceof Mapping
        }
    }
}
