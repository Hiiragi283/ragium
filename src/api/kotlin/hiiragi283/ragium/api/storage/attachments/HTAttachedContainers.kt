package hiiragi283.ragium.api.storage.attachments

/**
 * @see mekanism.common.attachments.containers.IAttachedContainers
 */
interface HTAttachedContainers<TYPE, ATTACHED : HTAttachedContainers<TYPE, ATTACHED>> : Collection<TYPE> {
    val containers: List<TYPE>

    operator fun get(index: Int): TYPE = containers[index]

    fun getOrNull(index: Int): TYPE? = containers.getOrNull(index)

    fun create(containers: List<TYPE>): ATTACHED

    fun with(index: Int, element: TYPE): ATTACHED {
        val copy: MutableList<TYPE> = containers.toMutableList()
        copy[index] = element
        return create(copy)
    }

    //    Collection    //

    override val size: Int get() = containers.size

    override fun isEmpty(): Boolean = containers.isEmpty()

    override fun contains(element: TYPE): Boolean = element in containers

    override fun iterator(): Iterator<TYPE> = containers.iterator()

    override fun containsAll(elements: Collection<TYPE>): Boolean = elements.all { it in containers }
}
