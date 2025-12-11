package hiiragi283.ragium.api.storage.attachments

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see mekanism.common.attachments.containers.item.AttachedItems
 */
@JvmRecord
data class HTAttachedItems(override val containers: List<ImmutableItemStack?>) :
    HTAttachedContainers<ImmutableItemStack?, HTAttachedItems> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTAttachedItems> = ImmutableItemStack.CODEC
            .toOptional()
            .listOf()
            .xmap(
                { stacks: List<Optional<ImmutableItemStack>> -> HTAttachedItems(stacks.map(Optional<ImmutableItemStack>::getOrNull)) },
                { attached: HTAttachedItems -> attached.containers.map(Optional<ImmutableItemStack>::ofNullable) },
            )

        @JvmField
        val EMPTY = HTAttachedItems(listOf())

        @JvmStatic
        fun create(size: Int): HTAttachedItems = HTAttachedItems(List(size) { null })
    }

    override fun create(containers: List<ImmutableItemStack?>): HTAttachedItems = HTAttachedItems(containers)

    override fun equals(other: Any?): Boolean {
        when {
            this === other -> return true
            other !is HTAttachedItems -> return false
            else -> {
                val otherContainers: List<ImmutableItemStack?> = other.containers
                return when {
                    containers.size != otherContainers.size -> {
                        false
                    }
                    else -> {
                        for (i: Int in containers.indices) {
                            val matches: Boolean = ItemStack.matches(
                                containers[i]?.unwrap() ?: ItemStack.EMPTY,
                                otherContainers[i]?.unwrap() ?: ItemStack.EMPTY,
                            )
                            if (!matches) return false
                        }
                        true
                    }
                }
            }
        }
    }

    override fun hashCode(): Int {
        var hash = 0
        for (stack: ImmutableItemStack? in containers) {
            hash = hash * 31 + (stack?.hashCode() ?: 0)
        }
        return hash
    }
}
