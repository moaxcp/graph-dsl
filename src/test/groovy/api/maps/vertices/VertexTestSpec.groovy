package api.maps.vertices

import api.maps.BaseMapSpec
import graph.Vertex

class VertexTestSpec extends BaseMapSpec {

    def setup() {
        emptyMap = new Vertex()
        map = new Vertex(id: 'step1')
        equalMap = new Vertex(id: 'step1', value: 'value')
        differentMap = new Vertex(id: 'step2')
    }

    def 'constructor set key'() {
        expect:
        map.id == 'step1'
    }

    def 'getAt with key'() {
        expect:
        map['id'] == 'step1'
    }

    def 'getId'() {
        expect:
        'step1' == map.getId()
    }
}