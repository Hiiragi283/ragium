package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import net.minecraft.block.Block
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSimpleBlockPattern(val block: Block) : HTMultiblockPattern {
    constructor(content: HTContent<Block>) : this(content.value)

    override val text: MutableText = block.name

    override fun test(world: World, pos: BlockPos): Boolean = world.getBlockState(pos).isOf(block)
}
