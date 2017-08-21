package graph

/**
 * Specification for a name.
 */
class NameSpec {
    String name

    NameSpec minus(NameSpec other) {
        new NameSpec(name:name + '-' + other.name)
    }
}
