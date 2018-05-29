package graph

trait EdgeDsl {

    abstract Edge edge(ConfigSpec spec)

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The
     * {@link Vertex} objects are identified by the params one and two. If the {@link Vertex} objects do not exist they
     * will be created.
     * @param one  the key of the first {@link Vertex}.
     * @param two  the key of the second {@link Vertex}.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Object one, Object two) {
        if(!one || !two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        ConfigSpec spec = new ConfigSpec(map:[one:one, two:two])
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}.The map may
     * contain configuration for the edge. Default configuration options are:
     * <dl>
     *     <dt>one</dt>
     *     <dd>key of the first {@link Vertex}</dd>
     *     <dt>two</dt>
     *     <dd>key of the second {@link Vertex}</dd>
     *     <dt>changeOne</dt>
     *     <dd>If the edge already exists edge.one will become changeOne. Otherwise edge.one is set to changeOne
     *     instead of one when it is created.</dd>
     *     <dt>changeTwo</dt>
     *     <dd>If the edge already exists edge.two will become changeTwo. Otherwise edge.two is set to changeTwo
     *     instead of two when it is created.</dd>
     * </dl>
     * Additional entries may be added by the type of the graph.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     * @see #edge(String one, String two, Closure closure)
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Map<String, ?> map) {
        if(!map.one || !map.two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        ConfigSpec spec = new ConfigSpec(map:map)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters.
     * @param one  the key of the first {@link Vertex}.
     * @param two  the key of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Object one, Object two, Map<String, ?> map) {
        if(!one || !two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        map.one = map.one ?: one
        map.two = map.two ?: two
        ConfigSpec spec = new ConfigSpec(map:map)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. {@code Closure}
     * is used to further customize the edge and graph. In the closure getting and setting properties delegates to the
     * edge. Methods also delegate to the edge.
     * <p>
     * Variables accessible within the closure:
     * <dl>
     *     <dt>{@code graph}</dt>
     *     <dd>{@link Graph} edge was added to</dd>
     *     <dt>{@code edge}</dt>
     *     <dd>{@link Edge} added to graph</dd>
     * </dl>
     * Attempting to set one or two will throw an exception. Use the {@code changeOne} and {@code changeTwo} methods.
     * <p>
     * By default there are several methods available in the closure.
     * <p>
     * <dl>
     *     <dt>{@code void changeOne(String changeOne)}</dt>
     *     <dd>Changes edge.one</dd>
     *     <dt>{@code void changeOne(NameSpec changeOne)}</dt>
     *     <dd>Changes edge.one</dd>
     *     <dt>{@code void changeTwo(String changeTwo)}</dt>
     *     <dd>Changes edge.two</dd>
     *     <dt>{@code void changeTwo(NameSpec changeTwo)}</dt>
     *     <dd>Changes edge.two</dd>
     *     <dt>{@code void traits(Class... traits)}</dt>
     *     <dd>Applies a trait the edge's delegate</dd>
     * </dl>
     * Plugins may add variables and methods to the passed in closure.
     * @param one  the key of the first {@link Vertex}.
     * @param two  the key of the second {@link Vertex}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Object one, Object two, Closure closure) {
        if(!one || !two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        ConfigSpec spec = new ConfigSpec(map:[one:one, two:two], closure:closure)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}. If one or two are entries in map those values will be used
     * instead of the parameters.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when map.one or map.two are invalid
     */
    Edge edge(Map<String, ?> map, Closure closure) {
        if(!map.one || !map.two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If one or two are entries in map those values will be used
     * instead of the parameters. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}.
     * @param one  the key of the first {@link Vertex}.
     * @param two  the key of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Object one, Object two, Map<String, ?> map, Closure closure) {
        if(!one || !two) {
            throw new IllegalArgumentException("Invalid one and two.")
        }
        map.one = map.one ?: one
        map.two = map.two ?: two
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        edge(spec)
    }
}