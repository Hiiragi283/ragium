package hiiragi283.ragium.common.multiblock

import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.function.Supplier

sealed class HTAxisMultiblockComponent(val block: Supplier<out Block>) : HTMultiblockComponent {
    abstract fun getAxis(controller: HTControllerDefinition): Direction.Axis

    final override fun getBlockName(definition: HTControllerDefinition): Component = ItemStack(block.get()).displayName

    final override fun checkState(definition: HTControllerDefinition, pos: BlockPos): Boolean {
        val block: Block = block.get()
        val state: BlockState = definition.level.getBlockState(pos)
        if (!state.`is`(block)) return false
        val currentAxis: Direction.Axis = state.getOrNull(BlockStateProperties.AXIS) ?: return true
        return getAxis(definition) == currentAxis
    }

    final override fun getPlacementState(definition: HTControllerDefinition): BlockState? =
        block.get().defaultBlockState().setValue(BlockStateProperties.AXIS, getAxis(definition))

    //    YStatic    //

    class YStatic(block: Supplier<out Block>) : HTAxisMultiblockComponent(block) {
        override fun getAxis(definition: HTControllerDefinition): Direction.Axis = Direction.Axis.Y
    }

    //    FrontHorizontal    //

    class FrontHorizontal(block: Supplier<out Block>) : HTAxisMultiblockComponent(block) {
        override fun getAxis(definition: HTControllerDefinition): Direction.Axis = definition.front.axis
    }

    //    FrontVertical    //

    class FrontVertical(block: Supplier<out Block>) : HTAxisMultiblockComponent(block) {
        override fun getAxis(definition: HTControllerDefinition): Direction.Axis = when (definition.front) {
            Direction.DOWN -> Direction.Axis.Y
            Direction.UP -> Direction.Axis.Y
            Direction.NORTH -> Direction.Axis.X
            Direction.SOUTH -> Direction.Axis.X
            Direction.WEST -> Direction.Axis.Z
            Direction.EAST -> Direction.Axis.Z
        }
    }
}
