package graph

import spock.lang.Specification

class EdgeTestSpec extends Specification {
    Edge edge = new Edge(one: 'step1', two: 'step2')

    def 'constructor sets properties'() {
        expect:
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'unset properties throw MissingPropertyException'() {
        when:
        edge.weight == null

        then:
        thrown MissingPropertyException
    }

    def 'toString is added by transform'() {
        expect:
        edge.toString() == 'graph.Edge(one:step1, two:step2)'
    }

    def 'equals with null'() {
        when:
        def edge2 = new Edge()

        then:
        !edge2.equals(null)
    }

    def 'equals with self'() {
        when:
        def edge2 = new Edge()

        then:
        edge2.equals(edge2)
    }

    def 'equals with second edge'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step2')

        then:
        edge == edge2
    }

    def 'equals with both vertices different'() {
        when:
        Edge edge2 = new Edge(one: 'step4', two: 'step3')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with first vertex different'() {
        when:
        def edge2 = new Edge(one: 'step3', two: 'step2')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with second vertex different'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step3')

        then:
        edge != edge2
        edge2 != edge
    }

    def 'equals with both vertices switched'() {
        when:
        def edge2 = new Edge(one: 'step1', two: 'step2')

        then:
        edge == edge2
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
