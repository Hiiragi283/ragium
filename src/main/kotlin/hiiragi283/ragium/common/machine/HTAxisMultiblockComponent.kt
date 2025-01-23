package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.function.Function
import java.util.function.Supplier

sealed class HTAxisMultiblockComponent(val getter: Function<HTMachineTier, out Supplier<out Block>>) : HTMultiblockComponent {
    fun getBlock(controller: HTControllerDefinition): Block =
        (controller.getCapability(RagiumAPI.BlockCapabilities.MACHINE_TIER) ?: HTMachineTier.BASIC)
            .let(getter::apply)
            .get()

    abstract fun getAxis(controller: HTControllerDefinition): Direction.Axis

    final override fun getType(): HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.AXIS.get()

    final override fun getBlockName(controller: HTControllerDefinition): Component = getBlock(controller).let(::ItemStack).displayName

    final override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean {
        val block: Block = getBlock(controller)
        val state: BlockState = controller.level.getBlockState(pos)
        if (!state.`is`(block)) return false
        val currentAxis: Direction.Axis = state.getOrNull(BlockStateProperties.AXIS) ?: return true
        return getAxis(controller) == currentAxis
    }

    final override fun getPlacementState(controller: HTControllerDefinition): BlockState? =
        getBlock(controller).defaultBlockState().setValue(BlockStateProperties.AXIS, getAxis(controller))

    //    YStatic    //

    class YStatic(blockGetter: Function<HTMachineTier, out Supplier<out Block>>) : HTAxisMultiblockComponent(blockGetter) {
        override fun getAxis(controller: HTControllerDefinition): Direction.Axis = Direction.Axis.Y
    }

    //    FrontHorizontal    //

    class FrontHorizontal(blockGetter: Function<HTMachineTier, out Supplier<out Block>>) : HTAxisMultiblockComponent(blockGetter) {
        override fun getAxis(controller: HTControllerDefinition): Direction.Axis = controller.front.axis
    }

    //    FrontVertical    //

    class FrontVertical(blockGetter: Function<HTMachineTier, out Supplier<out Block>>) : HTAxisMultiblockComponent(blockGetter) {
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
