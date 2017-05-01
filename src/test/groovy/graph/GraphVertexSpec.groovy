package graph

import spock.lang.Specification

/**
 * contains tests for GraphVertex methods. These do not include methods that deal with the graph and visit nodes.
 */
class GraphVertexSpec extends Specification {

    def graph

    def setup() {
        graph = new Graph()
    }

    def 'cannot add !name with vertex(String)'() {
        when:
        graph.vertex ''

        then:
        thrown IllegalArgumentException
    }

    def 'can add/get with vertex(String)'() {
        when:
        def vertex = graph.vertex 'step1'

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String)'() {
        setup:
        def expected = graph.vertex 'step1'

        when:
        def result = graph.vertex 'step1'

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Map)'() {
        when:
        def vertex = graph.vertex name:'step1'

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(Map)'() {
        setup:
        def expected = graph.vertex name:'step1'

        when:
        def result = graph.vertex name:'step1'

        then:
        result.is expected
    }

    def 'can add traits with vertex(Map)'() {
        when:
        def vertex = graph.vertex name:'step1', traits: [Mapping, Weight]

        then:
        vertex.name == 'step1'
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can add edges using edgesFirst with vertex(Map)'() {
        when:
        graph.vertex name:'step1', edgesFirst:['step2', 'step3']
        def edgeOne = graph.edges.find {
            it.one == 'step1' && it.two == 'step2'
        }
        def edgeTwo = graph.edges.find {
            it.one == 'step1' && it.two == 'step3'
        }

        then:
        graph.edges.size() == 2
        edgeOne != null
        edgeTwo != null
    }

    def 'can add edges using edgesSecond with vertex(Map)'() {
        when:
        graph.vertex name:'step1', edgesSecond:['step2', 'step3']
        def edgeOne = graph.edges.find {
            it.one == 'step2' && it.two == 'step1'
        }
        def edgeTwo = graph.edges.find {
            it.one == 'step3' && it.two == 'step1'
        }

        then:
        graph.edges.size() == 2
        edgeOne != null
        edgeTwo != null
    }

    def 'can configure vertex traits with VertexSpec.config(Closure) in vertex(Map)'() {
        when:
        Vertex vertex = graph.vertex name:'step1', traits:[Mapping, Weight],
            config:{
                label = 'the first step'
                weight { 100 }
            }

        then:
        vertex.label == 'the first step'
        vertex.getWeight() == 100
    }

    def 'can add/get vertex with vertex(String, Closure)'() {
        when:
        def vertex = graph.vertex 'step1', {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Closure)'() {
        setup:
        def expected = graph.vertex 'step1', {}

        when:
        def result = graph.vertex 'step1', {}

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(String, Map)'() {
        when:
        def vertex = graph.vertex 'step1', [:]

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Map)'() {
        setup:
        def expected = graph.vertex 'step1', [:]

        when:
        def result = graph.vertex 'step1', [:]

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Map, Closure)'() {
        when:
        def vertex = graph.vertex(name:'step1') {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(Map, Closure)'() {
        setup:
        def expected = graph.vertex(name:'step1') {}

        when:
        def result = graph.vertex(name:'step1') {}

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(String, Map, Closure)'() {
        when:
        def vertex = graph.vertex('step1', [:]) {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Map, Closure)'() {
        setup:
        def expected = graph.vertex('step1', [:]) {}

        when:
        def result = graph.vertex('step1', [:]) {}

        then:
        result.is expected
    }

    def 'vertices is unmodifiable'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.vertices.step2 = new Vertex(name: 'step2')

        then:
        thrown(UnsupportedOperationException)
    }

    def 'can rename vertex'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'

        when:
        graph.rename 'step1', 'step5'

        then:
        graph.vertices['step1'] == null
        graph.vertices.size() == 4
        graph.adjacentEdges('step1').size() == 0
        graph.adjacentEdges('step5').size() == 3
    }

    def 'cannot rename vertex to a false name'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'

        when:
        graph.rename 'step1', ''

        then:
        thrown IllegalArgumentException
    }

    def 'can create multiple vertices'() {
        when:
        Set<Vertex> vertices = graph.vertex 'step1', 'step2', 'step3', 'step4'

        then:
        vertices == [
                new Vertex(name:'step1'),
                new Vertex(name:'step2'),
                new Vertex(name:'step3'),
                new Vertex(name:'step4')
        ] as Set<Vertex>
    }

    def 'can create Vertex with VertexSpec property'() {
        when:
        def vertex = graph.vertex graph.step1

        then:
        vertex.name == 'step1'
    }

    def 'can create Vertex with VertexSpec method'() {
        when:
        graph.with {
            vertex step1(traits:Mapping) {
                config {
                    label = 'step1'
                }
            }
        }
        Vertex vertex = graph.vertices.step1

        then:
        vertex.name == 'step1'
        vertex.delegate instanceof Mapping
        vertex.label == 'step1'
    }
}
