package graph

import graph.type.VertexSpec
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

    def 'apply can add edges using edgesFirst'() {
        setup:
        spec.name = 'step1'
        spec.connectsTo 'step2', 'step3'

        when:
        spec.apply(graph)

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }

    def 'overlay with null'() {
        setup:
        VertexSpec first = new VertexSpec()
        VertexSpec second = new VertexSpec()
        second.name = 'step1'
        second.rename = 'step2'
        second.traits Mapping
        second.connectsTo 'step2'
        second.runnerCode {
            label = 'step1'
        }

        when:
        VertexSpec result = first.overlay second

        then:
        result.name == 'step1'
        result.rename == 'step2'
        result.traits == [Mapping] as Set<Class>
        result.getConnectsTo() == ['step2'] as Set<String>
        result.runnerCode != null
    }

    def 'overlay with values'() {
        setup:
        VertexSpec first = new VertexSpec()
        first.name = 'stepa'
        first.rename = 'step4'
        first.traits Weight
        first.connectsTo 'step5'
        first.runnerCode {
            label = 'step7'
        }
        VertexSpec second = new VertexSpec()
        second.name = 'step1'
        second.rename = 'step2'
        second.traits Mapping
        second.connectsTo 'step2'
        second.runnerCode {
            label = 'step1'
        }

        when:
        VertexSpec result = first.overlay second

        then:
        result.name == 'step1'
        result.rename == 'step2'
        result.traits == [Mapping, Weight] as Set<Class>
        result.getConnectsTo() == ['step2', 'step5'] as Set<String>
        result.runnerCode != null
    }
}
