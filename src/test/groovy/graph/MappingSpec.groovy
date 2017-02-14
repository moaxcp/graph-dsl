package graph

import spock.lang.Specification

class MappingSpec extends Specification {

    def 'can use map from edge'() {
        setup:
        def edge = new Edge()

        when:
        edge.delegateAs(Mapping)
        edge.key = 'value'

        then:
        'value' == edge.key
    }

    def 'can use map from vertex'() {
        setup:
        def vertex = new Vertex()

        when:
        vertex.delegateAs(Mapping)
        vertex.key = 'value'

        then:
        'value' == vertex.key
    }
}
