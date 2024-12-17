package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlockTagPattern(val tagKey: TagKey<Block>) : HTMultiblockPattern {
    val entryList: HTRegistryEntryList<Block> = HTRegistryEntryList.ofTag(tagKey, Registries.BLOCK)

    override val text: MutableText = tagKey.name.copy()

    override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isIn(tagKey)
}
