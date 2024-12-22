package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlockTagPattern(val tagKey: TagKey<Block>) : HTMultiblockPattern {
    private val entryList: HTRegistryEntryList<Block> = HTRegistryEntryList.ofTag(tagKey, Registries.BLOCK)

    override val text: MutableText = tagKey.name.copy()

    override fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean = world.getBlockState(pos).isIn(tagKey)

    override fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState? = getCurrentState(world)

    fun getCurrentState(world: World): BlockState? = when (entryList.size) {
        0 -> null
        1 -> entryList[0].defaultState
        else -> entryList[((world.time % (20 * entryList.size)) / 20).toInt()].defaultState
    }
}
