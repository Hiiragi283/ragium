package hiiragi283.ragium.common.storge.attachment

import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.attachments.HTAttachedContainers
import hiiragi283.core.common.storage.HTCapabilityCodec
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.common.attachments.containers.ComponentBackedHandler
 */
abstract class HTComponentHandler<TYPE, CONTAINER : HTValueSerializable, ATTACHED : HTAttachedContainers<TYPE, ATTACHED>>(
    protected val attachedTo: ItemStack,
    override val size: Int,
    private val containerFactory: ContainerFactory<CONTAINER>,
) : AbstractList<CONTAINER>() {
    fun interface ContainerFactory<CONTAINER : HTValueSerializable> {
        fun create(context: ContainerContext): CONTAINER

        fun create(attachedTo: ItemStack, size: Int, index: Int): CONTAINER = create(ContainerContext(attachedTo, size, index))
    }

    private lateinit var containers: MutableList<CONTAINER?>
    private var notInitialized: Int = -1

    protected abstract fun capabilityCodec(): HTCapabilityCodec<CONTAINER, ATTACHED>

    protected fun getAttached(): ATTACHED = capabilityCodec().getOrCreate(attachedTo, size)

    protected fun getContents(index: Int): TYPE? = getAttached().getOrNull(index)

    fun getContainers(): List<CONTAINER> {
        val containers: List<CONTAINER?> = getOrCreateContainers()
        var i = 0
        val size: Int = containers.size
        while (notInitialized > 0 && i < size) {
            if (containers[i] == null) {
                initContainer(i)
            }
            i++
        }
        return LazyList(containers)
    }

    private fun getOrCreateContainers(): MutableList<CONTAINER?> {
        if (!::containers.isInitialized) {
            containers = MutableList(size) { null }
            notInitialized = size
        }
        return containers
    }

    private fun initContainer(index: Int): CONTAINER {
        val container: CONTAINER = containerFactory.create(attachedTo, size, index)
        getOrCreateContainers()[index] = container
        notInitialized--
        return container
    }

    protected fun getContainer(index: Int): CONTAINER = when (val container: CONTAINER? = getOrCreateContainers()[index]) {
        null -> initContainer(index)
        else -> container
    }

    override fun get(index: Int): CONTAINER = getContainer(index)

    @JvmRecord
    data class ContainerContext(val attachedTo: ItemStack, val size: Int, val index: Int)

    /**
     * nullableな[List]を遅延評価でnot-nullに変換する[List]の実装
     */
    private class LazyList<T : Any>(val delegate: List<T?>) : AbstractList<T>() {
        override val size: Int get() = delegate.size

        override fun get(index: Int): T = delegate[index] ?: error("Element at $index is not initialized")
    }
}
