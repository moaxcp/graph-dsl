package graph

import spock.lang.Specification

class VertexSpec extends Specification {

    def vertex = new Vertex()

    def 'can set name of vertex'() {
        when:
        vertex.name = 'step1'

        then:
        vertex.name == 'step1'
    }

    def 'can add weight to vertex'() {
        when:
        def weightedVertex = vertex.withTraits(Weight)
        weightedVertex.weight { -> 5 }

        then:
        weightedVertex.weight == 5
    }

    def 'can add value to vertex'() {
        when:
        def valuedVertex = vertex.withTraits(Value)
        valuedVertex.value = ['work1', 'work2']

        then:
        valuedVertex.value == ['work1', 'work2']
    }

    def 'can get weight from value'() {
        when:
        def traitVertex = vertex.withTraits(Weight, Value)
        traitVertex.value = ['work1', 'work2']
        traitVertex.weight {
            value.size()
        }

        then:
        traitVertex.weight == 2
    }

    def 'vertex equals null is false'() {
        when:
        def equals = vertex.equals(null)

        then:
        !equals
    }

    def 'vertex equals non-equal vertex is false'() {
        setup:
        vertex.name = 'step1'
        def compare = new Vertex(name: 'step2')

        when:
        def equals = vertex == compare

        then:
        !equals
    }

    def 'vertex equals self'() {
        setup:
        vertex.name = 'step1'

        when:
        def equals = vertex == vertex

        then:
        equals
    }

    def 'vertex equals equal vertex is true'() {
        setup:
        vertex.name = 'step1'
        def compare = new Vertex(name: 'step1')

        when:
        def equals = vertex == compare

        then:
        equals
    }
}