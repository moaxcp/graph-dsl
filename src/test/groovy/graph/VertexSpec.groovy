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
        def weightedVertice = vertex.withTraits(Weight)
        weightedVertice.weight { -> 5 }

        then:
        weightedVertice.weight == 5
    }

    def 'can add value to vertex'() {
        when:
        def valuedVertice = vertex.withTraits(Value)
        valuedVertice.value = ['work1', 'work2']

        then:
        valuedVertice.value == ['work1', 'work2']
    }

    def 'can get weight from value'() {
        when:
        def traitVertice = vertex.withTraits(Weight, Value)
        traitVertice.value = ['work1', 'work2']
        traitVertice.weight {
            value.size()
        }

        then:
        traitVertice.weight == 2
    }
}