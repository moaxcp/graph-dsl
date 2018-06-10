package graph

trait EdgeDsl {

    abstract Edge edge(ConfigSpec spec)

    /**
     * Creates or finds an {@link Edge} between to {@link Vertex} objects returning the {@link Edge}. The
     * {@link Vertex} objects are identified by the params from and to. If the {@link Vertex} objects do not exist they
     * will be created.
     * @param from  the id of the first {@link Vertex}.
     * @param to  the id of the second {@link Vertex}.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when from or to are invalid
     */
    Edge edge(Object from, Object to) {
        if(!from || !to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        ConfigSpec spec = new ConfigSpec(map:[from:from, to:to])
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between two {@link Vertex} objects returning the {@link Edge}.The map may
     * contain configuration for the edge. Default configuration options are:
     * <dl>
     *     <dt>from</dt>
     *     <dd>id of the first {@link Vertex}</dd>
     *     <dt>to</dt>
     *     <dd>id of the second {@link Vertex}</dd>
     *     <dt>changeFrom</dt>
     *     <dd>If the edge already exists edge.from will become changeFrom. Otherwise edge.from is set to changeFrom
     *     instead of 'from' when it is created.</dd>
     *     <dt>changeTo</dt>
     *     <dd>If the edge already exists edge.to will become changeTo. Otherwise edge.to is set to changeTo
     *     instead of 'to' when it is created.</dd>
     * </dl>
     * Additional entries may be added by the type of the graph.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     * @see #edge(String one, String two, Closure closure)
     * @throws IllegalArgumentException when one or two are invalid
     */
    Edge edge(Map<String, ?> map) {
        if(!map.from || !map.to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        ConfigSpec spec = new ConfigSpec(map:map)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between to {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If 'from' or 'to' are entries in map those values will be used
     * instead of the parameters.
     * @param from  the id of the first {@link Vertex}.
     * @param to  the id of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when from or to are invalid
     */
    Edge edge(Object from, Object to, Map<String, ?> map) {
        if(!from || !to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        map.from = map.from ?: from
        map.to = map.to ?: to
        ConfigSpec spec = new ConfigSpec(map:map)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between to {@link Vertex} objects returning the {@link Edge}. {@code Closure}
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
     * Attempting to set 'from' or 'to' will throw an exception. Use the {@code changeFrom} and {@code changeTo} methods.
     * <p>
     * By default there are several methods available in the closure.
     * <p>
     * <dl>
     *     <dt>{@code void changeFrom(String changeFrom)}</dt>
     *     <dd>Changes edge.from</dd>
     *     <dt>{@code void changeTo(String changeTo)}</dt>
     *     <dd>Changes edge.to</dd>
     *     <dt>{@code void traits(Class... traits)}</dt>
     *     <dd>Applies a trait the edge's delegate</dd>
     * </dl>
     * Plugins may add variables and methods to the passed in closure.
     * @param from  the id of the first {@link Vertex}.
     * @param to  the id of the second {@link Vertex}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when from or to are invalid
     */
    Edge edge(Object from, Object to, Closure closure) {
        if(!from || !to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        ConfigSpec spec = new ConfigSpec(map:[from:from, to:to], closure:closure)
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
     * @throws IllegalArgumentException when map.from or map.to are invalid
     */
    Edge edge(Map<String, ?> map, Closure closure) {
        if(!map.from || !map.to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        edge(spec)
    }

    /**
     * Creates or finds an {@link Edge} between to {@link Vertex} objects returning the {@link Edge}. The map contains
     * configuration described in {@link #edge(Map)}. If from or to are entries in map those values will be used
     * instead of the parameters. The configuration given by the closure is described in
     * {@link #edge(String, String, Closure)}.
     * @param from  the id of the first {@link Vertex}.
     * @param to  the id of the second {@link Vertex}.
     * @param map  used to create an {@link Edge}.
     * @param closure  to run.
     * @return the resulting {@link Edge}.
     * @throws IllegalArgumentException when from or to are invalid
     */
    Edge edge(Object from, Object to, Map<String, ?> map, Closure closure) {
        if(!from || !to) {
            throw new IllegalArgumentException("Invalid from and to.")
        }
        map.from = map.from ?: from
        map.to = map.to ?: to
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        edge(spec)
    }
}