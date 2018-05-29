package graph

import spock.lang.Specification
import static graph.TraversalAlgorithms.*

class TraversalAlgorithmsSpec extends Specification {
    def 'edgeType with null color'() {
        expect:
        edgeType([:], null, null, null) == null
    }
}
