package api.maps.edges

import api.maps.BaseMapSpec
import graph.Edge
import spock.lang.Shared
import spock.lang.Unroll

abstract class BaseEdgeSpec extends BaseMapSpec {
    Edge firstDifferent
    Edge secondDifferent
    Edge switched
    @Shared List falseEdges

    def 'constructor sets one and two entries'() {
        expect:
        map.one == 'step1'
        map.two == 'step2'
    }

    def 'equals with first vertex different'() {
        expect:
        map != firstDifferent
        firstDifferent != map
    }

    def 'equals with second vertex different'() {
        expect:
        map != secondDifferent
        secondDifferent != map
    }

    @Unroll
    def 'edge with one="#e.one" and two="#e.two" is false'() {
        expect:
        !e

        where:
        e << falseEdges
    }

    def 'getAt with one'() {
        when:
        emptyMap.one = 'step1'

        then:
        emptyMap['one'] == 'step1'
    }

    def 'getAt with two'() {
        when:
        emptyMap.two = 'step2'

        then:
        emptyMap['two'] == 'step2'
    }
}
