package graph

trait Mapping {
    Map map = [:]

    def propertyMissing(String name) {
        map[name]
    }

    def propertyMissing(String name, value) {
        map[name] = value
    }
}
