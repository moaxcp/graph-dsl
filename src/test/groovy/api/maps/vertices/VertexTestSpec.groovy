package api.maps.vertices

import api.maps.BaseMapSpec
import graph.Vertex

class VertexTestSpec extends BaseMapSpec {

    def setup() {
        emptyMap = new Vertex()
        map = new Vertex(key: 'step1')
        equalMap = new Vertex(key: 'step1', value: 'value')
        differentMap = new Vertex(key: 'step2')
    }

    def 'constructor set key'() {
        expect:
        map.key == 'step1'
    }

    def 'getAt with key'() {
        expect:
        map['key'] == 'step1'
    }

    def 'getKey'() {
        expect:
        'step1' == map.getKey()
    }
}