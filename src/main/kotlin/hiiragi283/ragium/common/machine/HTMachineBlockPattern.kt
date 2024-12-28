package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import net.minecraft.block.BlockState
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTMachineBlockPattern : HTMultiblockPattern {
    override val text: MutableText = Text.literal("Any Machines")

    override fun checkState(world: World, pos: BlockPos, provider: HTMultiblockProvider): Boolean = world.getMachineEntity(pos) != null

    override fun getPlacementState(world: World, pos: BlockPos, provider: HTMultiblockProvider): BlockState? = null
}
