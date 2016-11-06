package graph

import spock.lang.Specification

class VerticeSpec extends Specification {

    def vertice = new Vertice()

    def 'can set name of vertice'() {
        when:
        vertice.name = 'step1'

        then:
        vertice.name == 'step1'
    }

    def 'can add weight to vertice'() {
        when:
        def weightedVertice = vertice.withTraits(Weight)
        weightedVertice.weight { -> 5 }

        then:
        weightedVertice.weight == 5
    }

    def 'can add value to vertice'() {
        when:
        def valuedVertice = vertice.withTraits(Value)
        valuedVertice.value = ['work1', 'work2']

        then:
        valuedVertice.value == ['work1', 'work2']
    }
}