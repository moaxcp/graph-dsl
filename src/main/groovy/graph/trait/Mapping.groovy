package graph.trait

/**
 * Delegates propertyMissing methods to a map.
 */
trait Mapping {

    /**
     * Map to which propertyMissing methods are delegated.
     */
    Map map = [:]

    /**
     * Returns the value in map using name as the key.
     * @param name - the key in the map
     * @return the value in map where the key is name.
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name) {
        map[name]
    }

    /**
     * Sets the value in map using name as the key.
     * @param name - the key in the map.
     * @param value - the value to set it to.
     * @return the value in map where the key is name.
     */
    @SuppressWarnings('NoDef')
    def propertyMissing(String name, value) {
        map[name] = value
    }
}
