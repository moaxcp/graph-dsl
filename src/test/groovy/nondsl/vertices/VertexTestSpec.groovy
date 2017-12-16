package nondsl.vertices

import graph.Vertex
import spock.lang.Specification

class VertexTestSpec extends Specification {

    def emptyVertex = new Vertex()
    def vertex = new Vertex(key:'step1')
    def equalVertex = new Vertex(key:'step1', value:'value')
    def different = new Vertex(key:'step2')

    def 'constructor set key'() {
        expect:
        vertex.key == 'step1'
    }

    def 'can check for missing entry'() {
        expect:
        !vertex.containsKey('value')
    }

    def 'can check for added entry'() {
        when:
        vertex.value = 10

        then:
        vertex.containsKey('value')
        vertex.value == 10
    }

    def 'equals with null'() {
        expect:
        vertex != null
    }

    def 'equals with not Vertex'() {
        expect:
        vertex != 'hello'
    }

    def 'equals with self'() {
        expect:
        vertex == vertex
    }

    def 'equals with second Vertex'() {
        expect:
        vertex == equalVertex
    }

    def 'not equals with different vertex'() {
        expect:
        vertex != different
        different != vertex
    }

    def 'true vertex'() {
        expect:
        vertex
    }

    def 'false vertex'() {
        expect:
        !emptyVertex
    }

    def 'can add entry with index operation'() {
        when:
        vertex['value'] = 10

        then:
        vertex.containsKey('value')
        vertex['value'] == 10
    }

    def 'getAt with key'() {
        expect:
        vertex['key'] == 'step1'
    }

    def 'getAt with delegate'() {
        given:
        vertex.value = 'value'

        expect:
        vertex['value'] == 'value'
    }
}