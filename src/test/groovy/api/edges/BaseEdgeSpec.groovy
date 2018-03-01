package api.edges

import graph.Edge
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

abstract class BaseEdgeSpec extends Specification {
    Edge emptyEdge
    Edge edge
    Edge equalEdge
    Edge bothDifferent
    Edge firstDifferent
    Edge secondDifferent
    Edge switched
    @Shared List falseEdges

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
        edge != null
    }

    def 'equals with not Edge'() {
        expect:
        edge != 'hello'
    }

    def 'equals with self'() {
        expect:
        edge == edge
    }

    def 'equals with second edge'() {
        expect:
        edge == equalEdge

        and:
        equalEdge.weight == 10
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

    def 'edge with one and two set is true'() {
        expect:
        edge
    }

    @Unroll
    def 'edge with one="#e.one" and two="#e.two" is false'() {
        expect:
        !e

        where:
        e << falseEdges
    }

    def 'can add entry with index operation'() {
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

    def 'can replace value in map'() {
        given: 'key:value entry added to edge'
        edge.put('key', 'value')

        when: '"value" is replaced with "value2"'
        String value = edge.replace('key', 'value2')

        then: '"value" is returned and key maps to "value2"'
        value == 'value'
        edge.get('key') == 'value2'
    }

    def 'can replace value in map if value exists'() {
        given: 'key:value entry added to edge'
        edge.put('key', 'value')

        when: '"value" is replace with "value2"'
        boolean replaced = edge.replace('key', 'value', 'value2')

        then: 'true is returned'
        replaced

        and: '"value" is replaced with "value2"'
        edge.get('key') == 'value2'
    }

    def 'can remove entry'() {
        given: 'key:value entry added to edge'
        edge.put('key', 'value')

        when: 'entry is removed'
        String value = edge.remove('key')

        then: '"value" is returned'
        value == 'value'

        and: 'entry is removed'
        edge.size() == 2
        edge.get('key') == null
    }

    def 'can remove entry if value exists'() {
        given: 'key:value entry added to edge'
        edge.put('key', 'value')

        when: 'entry with value is removed'
        boolean removed = edge.remove('key', 'value')

        then: 'true is returned'
        removed

        and: 'entry is removed'
        edge.size() == 2
        edge.get('key') == null
    }

    def 'containsValue can return true'() {
        given: 'key:value entry added to edge'
        edge.put('key', 'value')

        expect: 'containsValue is true'
        edge.containsValue('value')
    }

    def 'isEmpty can return true'() {
        expect: 'isEmpty returns true with emptyEdge'
        emptyEdge.isEmpty()
    }
}
