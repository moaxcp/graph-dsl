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

    def 'constructor sets from and to entries'() {
        expect:
        map.from == 'step1'
        map.to == 'step2'
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
    def 'edge with from="#e.from" and to="#e.to" is false'() {
        expect:
        !e

        where:
        e << falseEdges
    }

    def 'getAt with one'() {
        when:
        emptyMap.from = 'step1'

        then:
        emptyMap['from'] == 'step1'
    }

    def 'getAt with to'() {
        when:
        emptyMap.to = 'step2'

        then:
        emptyMap['to'] == 'step2'
    }

    def 'test from'() {
        when:
        emptyMap.from = 'step1'

        then:
        'step1' == emptyMap.getFrom()
    }

    def 'test to'() {
        when:
        emptyMap.to = 'step2'

        then:
        'step2' == emptyMap.getTo()
    }
}
