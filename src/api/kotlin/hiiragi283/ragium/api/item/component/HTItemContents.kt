package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * @see net.minecraft.world.item.component.ItemContainerContents
 */
@JvmInline
value class HTItemContents private constructor(private val items: Array<ImmutableItemStack>): Iterable<ImmutableItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemContents> = ImmutableItemStack.CODEC
            .listOf()
            .xmap(
                { stacks: List<ImmutableItemStack> -> of(stacks.toTypedArray()) },
                { contents: HTItemContents -> contents.items.toList() },
            )

        val EMPTY = HTItemContents(arrayOf())

        @JvmStatic
        fun of(stack: ImmutableItemStack): HTItemContents = of(arrayOf(stack))

        @JvmStatic
        fun of(items: Array<ImmutableItemStack>): HTItemContents = when {
            items.isEmpty() -> EMPTY
            else -> HTItemContents(items)
        }

        @JvmStatic
        fun of(size: Int): HTItemContents = when (size) {
            0 -> EMPTY
            else -> HTItemContents(Array(size) { ImmutableItemStack.EMPTY })
        }
    }

    val size: Int get() = items.size
    val indices: IntRange get() = (0..<size)

    fun isEmpty(): Boolean = items.isEmpty() || items.all(ImmutableItemStack::isEmpty)

    operator fun get(slot: Int): ImmutableItemStack = items[slot]

    fun getOrNull(slot: Int): ImmutableItemStack? = items.getOrNull(slot)

    fun copy(): HTItemContents = when {
        this.items.isEmpty() -> EMPTY
        else -> HTItemContents(items.map(ImmutableItemStack::copy).toTypedArray())
    }

    override fun iterator(): Iterator<ImmutableItemStack> = items.iterator()
}
