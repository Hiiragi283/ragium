package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

sealed class HTAxisMultiblockComponent(val blockGetter: (HTMachineTier) -> HTBlockContent) : HTMultiblockComponent {
    fun getBlock(controller: HTControllerDefinition): Block? = controller.find(HTMachineTier.SIDED_LOOKUP)?.let(blockGetter)?.get()

    abstract fun getAxis(controller: HTControllerDefinition): Direction.Axis

    final override val type: HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.AXIS

    final override fun getBlockName(controller: HTControllerDefinition): Text =
        getBlock(controller)?.let(::ItemStack)?.name ?: Text.literal("Error!")

    final override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean {
        val block: Block = getBlock(controller) ?: return false
        val state: BlockState = controller.world.getBlockState(pos)
        if (!state.isOf(block)) return false
        val currentAxis: Direction.Axis = state.getOrNull(Properties.AXIS) ?: return false
        return getAxis(controller) == currentAxis
    }

    final override fun getPlacementState(controller: HTControllerDefinition): BlockState? =
        getBlock(controller)?.defaultState?.with(Properties.AXIS, getAxis(controller))

    class YStatic(blockGetter: (HTMachineTier) -> HTBlockContent) : HTAxisMultiblockComponent(blockGetter) {
        override fun getAxis(controller: HTControllerDefinition): Direction.Axis = Direction.Axis.Y
    }

    class FrontHorizontal(blockGetter: (HTMachineTier) -> HTBlockContent) : HTAxisMultiblockComponent(blockGetter) {
        override fun getAxis(controller: HTControllerDefinition): Direction.Axis = controller.front.axis
    }

    class FrontVertical(blockGetter: (HTMachineTier) -> HTBlockContent) : HTAxisMultiblockComponent(blockGetter) {
        override fun getAxis(controller: HTControllerDefinition): Direction.Axis = when (controller.front) {
            Direction.DOWN -> Direction.Axis.Y
            Direction.UP -> Direction.Axis.Y
            Direction.NORTH -> Direction.Axis.X
            Direction.SOUTH -> Direction.Axis.X
            Direction.WEST -> Direction.Axis.Z
            Direction.EAST -> Direction.Axis.Z
        }
    }
}
