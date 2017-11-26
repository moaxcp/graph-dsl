package graph

import spock.lang.Specification

class VertexTestSpec extends Specification {

    def vertex = new Vertex()

    def 'can set name of vertex'() {
        when:
        vertex.key = 'step1'

        then:
        vertex.key == 'step1'
    }

    def 'can add value to vertex'() {
        when:
        vertex.value = ['work1', 'work2']

        then:
        vertex.value == ['work1', 'work2']
    }

    def 'vertex equals null is false'() {
        when:
        def equals = vertex.equals(null)

        then:
        !equals
    }

    def 'vertex equals non-equal vertex is false'() {
        setup:
        vertex.key = 'step1'
        def compare = new Vertex(key:'step2')

        when:
        def equals = vertex == compare

        then:
        !equals
    }

    def 'vertex equals self'() {
        setup:
        vertex.key = 'step1'

        when:
        def equals = vertex == vertex

        then:
        equals
    }

    def 'vertex equals equal vertex is true'() {
        given:
        vertex.key = 'step1'
        def compare = new Vertex(key:'step1')

        expect:
        vertex == compare
    }

    def 'getAt with name'() {
        given:
        vertex.key = 'step1'

        expect:
        vertex['key'] == 'step1'
    }

    def 'getAt with delegate'() {
        given:
        vertex.key = 'value'

        expect:
        vertex['key'] == 'value'
    }
}