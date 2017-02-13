package graph

import spock.lang.Specification

/**
 * Created by john on 2/12/17.
 */
class MapDelegateSpec extends Specification {
    def edge = new Edge()

    def setup() {
        edge.delegateAs(MapDelegate)
    }

    def 'can use map from edge'() {
        when:
        edge.key = 'value'

        then:
        'value' == edge.key
    }
}
