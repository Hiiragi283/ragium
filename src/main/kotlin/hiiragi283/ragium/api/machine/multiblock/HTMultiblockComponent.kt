package hiiragi283.ragium.api.machine.multiblock

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.content.HTEntryDelegated
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.World
import java.util.function.Predicate

class HTMultiblockComponent private constructor(private val entryList: RegistryEntryList<Block>) : Predicate<BlockState> {
    companion object {
        @JvmField
        val EMPTY = HTMultiblockComponent(RegistryEntryList.empty())

        @JvmField
        val CODEC: Codec<HTMultiblockComponent> =
            RegistryCodecs.entryList(RegistryKeys.BLOCK, Registries.BLOCK.codec).xmap(
                ::HTMultiblockComponent,
                HTMultiblockComponent::entryList,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMultiblockComponent> = PacketCodec.tuple(
            PacketCodecs.registryEntryList(RegistryKeys.BLOCK),
            HTMultiblockComponent::entryList,
            ::HTMultiblockComponent,
        )

        @JvmStatic
        fun of(delegated: HTEntryDelegated<Block>): HTMultiblockComponent = of(delegated.value)

        @Suppress("DEPRECATION")
        @JvmStatic
        fun of(block: Block): HTMultiblockComponent = when (block) {
            Blocks.AIR -> EMPTY
            else -> HTMultiblockComponent(RegistryEntryList.of(block.registryEntry))
        }

        @JvmStatic
        fun of(tagKey: TagKey<Block>): HTMultiblockComponent = Registries.BLOCK.getOrCreateEntryList(tagKey).let(::HTMultiblockComponent)
    }

    private val matchingStates: List<BlockState>
        get() = entryList.map(RegistryEntry<Block>::value).map(Block::getDefaultState)

    fun getPreviewState(world: World): BlockState? = matchingStates.getOrNull(getIndex(world, matchingStates.size))

    private fun getIndex(world: World, size: Int): Int = when (size) {
        0 -> 0
        1 -> 0
        else -> ((world.time % (20 * size)) / 20).toInt()
    }

    override fun toString(): String = "HTMultiblockComponent[$entryList]"

    //    Predicate    //

    override fun test(state: BlockState): Boolean = when {
        state.isAir -> this == EMPTY
        else -> entryList.any(state::isOf)
    }
}
