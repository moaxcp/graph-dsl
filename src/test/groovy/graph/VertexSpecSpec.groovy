package graph

import spock.lang.Specification

class VertexSpecSpec extends Specification {

    Graph graph = new Graph()
    VertexSpec spec = new VertexSpec()

    def 'apply throws exception on invalid name'() {
        setup:
        spec.name = ''

        when:
        spec.apply(graph)

        then:
        thrown IllegalArgumentException
    }

    def 'apply can add vertex'() {
        setup:
        spec.name = 'step1'

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices[vertex.name] == vertex
    }

    def 'apply can rename vertex'() {
        setup:
        graph.vertex 'step1'
        spec.name = 'step1'
        spec.rename = 'step2'

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices.size() == 1
        vertex.name == 'step2'
        graph.vertices[vertex.name] == vertex
    }

    def 'apply can add edges using edgesFirst with'() {
        setup:
        spec.name = 'step1'
        spec.edgesFirst 'step2', 'step3'

        when:
        spec.apply(graph)

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }

    def 'apply can add traits'() {
        setup:
        spec.name = 'step1'
        spec.traits Mapping, Weight

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices.size() == 1
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can make VertexSpec with Closure'() {
        when:
        VertexSpec spec = VertexSpec.newInstance {
            name = 'step1'
            traits Mapping, Weight
            edgesFirst 'step2', 'step3'
            runnerCode {
                label = 'the first step'
                weight 100
            }
        }

        then:
        spec.name == 'step1'
        spec.traits == [Mapping, Weight] as Set<Class>
        spec.getEdgesFirst() == ['step2', 'step3'] as Set<String>
        spec.runnerCode != null
        spec.runnerCode instanceof Closure
    }

    def 'overlay with null'() {
        setup:
        VertexSpec first = new VertexSpec()
        VertexSpec second = new VertexSpec()
        second.name = 'step1'
        second.traits Mapping
        second.edgesFirst 'step2'
        second.edgesSecond 'step3'
        second.runnerCode {
            label = 'step1'
        }

        when:
        VertexSpec result = first.overlay second

        then:
        result.name == 'step1'
        result.traits == [Mapping] as Set<Class>
        result.getEdgesFirst() == ['step2'] as Set<String>
        result.edgesSecond == ['step3'] as Set<String>

    }
}
