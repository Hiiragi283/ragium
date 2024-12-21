package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.minecraft.block.Block
import net.minecraft.text.MutableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSimpleBlockPattern(val block: Block) : HTMultiblockPattern {
    constructor(content: HTBlockContent) : this(content.get())

    override val text: MutableText = block.name

    override fun test(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean = world.getBlockState(pos).isOf(block)
}
