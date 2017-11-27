package nondsl.edges

import graph.Edge
import spock.lang.Specification

abstract class BaseEdgeSpec extends Specification {
    Edge edge
    Edge equalEdge
    Edge bothDifferent
    Edge firstDifferent
    Edge secondDifferent
    Edge switched

    def 'constructor sets one and two entries'() {
        expect:
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can check for missing entry'() {
        expect:
        !edge.containsKey('weight')
    }

    def 'can check for added entry'() {
        when:
        edge.weight = 10

        then:
        edge.containsKey('weight')
        edge.weight == 10
    }

    def 'equals with null'() {
        expect:
        !edge.equals(null)
    }

    def 'equals with not Edge'() {
        expect:
        edge != 'hello'
    }

    def 'equals with self'() {
        expect:
        edge.equals(edge)
    }

    def 'equals with second edge'() {
        expect:
        edge == equalEdge
    }

    def 'equals with both vertices different'() {
        expect:
        edge != bothDifferent
        bothDifferent != edge
    }

    def 'equals with first vertex different'() {
        expect:
        edge != firstDifferent
        firstDifferent != edge
    }

    def 'equals with second vertex different'() {
        expect:
        edge != secondDifferent
        secondDifferent != edge
    }

    def 'can set property with index operation'() {
        when:
        edge['weight'] = 10

        then:
        edge.containsKey('weight')
        edge['weight'] == 10
    }

    def 'getAt with one'() {
        when:
        edge.one = 'step1'

        then:
        edge['one'] == 'step1'
    }

    def 'getAt with two'() {
        when:
        edge.two = 'step2'

        then:
        edge['two'] == 'step2'
    }

    def 'getAt with delegate'() {
        when:
        edge.key = 'value'

        then:
        edge['key'] == 'value'
    }
}
