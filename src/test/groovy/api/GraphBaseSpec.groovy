package api

import graph.Graph
import graph.plugin.Plugin
import spock.lang.Specification

abstract class GraphBaseSpec extends Specification {
    class FakePlugin implements Plugin {
        void apply(Graph graph) {

        }

        void setGraph(Graph graph) {

        }
    }
    Graph graph = new Graph()

    def 'missing method throws MissingMethodException'() {
        when:
        graph.iAmMissing()

        then:
        thrown MissingMethodException
    }

    def 'method missing in plugin'() {
        given:
        graph.plugin(FakePlugin)

        when:
        graph.iAmMissing()

        then:
        thrown MissingMethodException
    }

    def 'add a vertex'() {
        given:
        graph.vertex('A')

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add multiple vertices'() {
        given:
        graph.vertex('A', 'B', 'C')

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
    }

    def 'add vertex with string and closure'() {
        given:
        graph.vertex('A') {}

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add vertex with string, map, and closure'() {
        given:
        graph.vertex('A', [:]) {}

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add a vertex with Map'() {
        given:
        graph.vertex([id:'A'])

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add a vertex with Map and closure'() {
        given:
        graph.vertex([id:'A']) {}

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add with id param and id set in map'() {
        given:
        graph.vertex('A', [id:'A'])

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'add with id param and id set in map with closure'() {
        given:
        graph.vertex('A', [id:'A']) {}

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.id == 'A'
    }

    def 'change id in map'() {
        given:
        graph.vertex('A', [changeId:'B'])

        expect:
        graph.vertices.size() == 1
        graph.vertices.B.id == 'B'
    }

    def 'change id in map null'() {
        when:
        graph.vertex('A', [changeId:null])

        then:
        thrown IllegalArgumentException
    }

    def 'create edge using connectsTo in map'() {
        given:
        graph.vertex('A', [connectsTo:'B'])

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
    }

    def 'create two edges using connectsTo in map'() {
        given:
        graph.vertex('A', [connectsTo:['B', 'C']])

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
        graph.edges.find { it.from == 'A' && it.to == 'C' }
    }

    def 'cannot create edge with null in connectsTo collection'() {
        when:
        graph.vertex('A', [connectsTo:[null]])

        then:
        IllegalArgumentException e = thrown()
        e.message == 'Invalid connectsTo item.'
    }

    def 'create edge using connectsFrom in map'() {
        given:
        graph.vertex('A', [connectsFrom:'B'])

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
    }

    def 'cannot create edge with null in connectsFrom collection'() {
        when:
        graph.vertex('A', [connectsFrom:[null]])

        then:
        IllegalArgumentException e = thrown()
        e.message == 'Invalid connectsFrom item.'
    }

    def 'create two edges using connectsFrom in map'() {
        given:
        graph.vertex('A', [connectsFrom:['B', 'C']])

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
        graph.edges.find { it.from == 'C' && it.to == 'A' }
    }

    def 'create edge using connectsTo in closure'() {
        given:
        graph.vertex('A') { connectsTo 'B' }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
    }

    def 'create two edges using connectsTo in closure'() {
        given:
        graph.vertex('A') { connectsTo 'B', 'C' }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'A' && it.to == 'B' }
        graph.edges.find { it.from == 'A' && it.to == 'C' }
    }

    def 'create edge using connectsFrom in closure'() {
        given:
        graph.vertex('A') { connectsFrom 'B' }

        expect:
        graph.vertices.size() == 2
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
    }

    def 'create two edges using connectsFrom in closure'() {
        given:
        graph.vertex('A') { connectsFrom 'B', 'C' }

        expect:
        graph.vertices.size() == 3
        graph.vertices.A.id == 'A'
        graph.vertices.B.id == 'B'
        graph.vertices.C.id == 'C'
        graph.edges.find { it.from == 'B' && it.to == 'A' }
        graph.edges.find { it.from == 'C' && it.to == 'A' }
    }
}
