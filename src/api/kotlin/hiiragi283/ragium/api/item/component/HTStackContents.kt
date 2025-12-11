package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableComponentStack
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.network.RegistryFriendlyByteBuf
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

typealias HTItemContents = HTStackContents<ImmutableItemStack>
typealias HTFluidContents = HTStackContents<ImmutableFluidStack>

/**
 * @see mekanism.common.attachments.containers.item.AttachedItems
 */
data class HTStackContents<STACK : ImmutableComponentStack<*, STACK>>(val stacks: List<Optional<STACK>>) : AbstractList<STACK?>() {
    companion object {
        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> codec(
            stackCodec: BiCodec<RegistryFriendlyByteBuf, STACK>,
        ): BiCodec<RegistryFriendlyByteBuf, HTStackContents<STACK>> = stackCodec
            .toOptional()
            .listOrElement()
            .xmap(::HTStackContents, HTStackContents<STACK>::stacks)

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> empty(): HTStackContents<STACK> = HTStackContents(listOf())

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> fromNullable(stacks: List<STACK?>): HTStackContents<STACK> = when {
            stacks.isEmpty() || stacks.filterNotNull().isEmpty() -> empty()
            else -> HTStackContents(stacks.map(Optional<STACK>::ofNullable))
        }

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> fromOptional(stacks: List<Optional<STACK>>): HTStackContents<STACK> = when {
            stacks.isEmpty() || stacks.none(Optional<STACK>::isPresent) -> empty()
            else -> HTStackContents(stacks)
        }
    }

    override val size: Int
        get() = stacks.size

    override fun get(index: Int): STACK? = stacks[index].getOrNull()

    override fun isEmpty(): Boolean = super.isEmpty() || stacks.all(Optional<STACK>::isEmpty)

    fun unwrap(): MutableList<Optional<STACK>> = stacks.toMutableList()

    override fun equals(other: Any?): Boolean = when (other) {
        !is HTStackContents<STACK> -> false
        else -> ImmutableComponentStack.listMatches(this, other)
    }

    override fun hashCode(): Int = ImmutableComponentStack.hashStackList(this)
}
