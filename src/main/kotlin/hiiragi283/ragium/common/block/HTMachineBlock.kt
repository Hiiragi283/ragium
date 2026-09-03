package hiiragi283.ragium.common.block

import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult

open class HTMachineBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTHorizontalEntityBlock(type, properties),
    HTBlockWidgetHolderContext.Factory {
    companion object {
        @JvmField
        val IS_ACTIVE: BooleanProperty = BooleanProperty.create("is_active")
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(IS_ACTIVE, false))
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (player is ServerPlayer) {
            HTBlockWidgetHolderContext.openMenu(player, pos)
        }
        return InteractionResult.SUCCESS
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_ACTIVE)
    }

    override fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        (context.blockEntity as? HTBlockEntityWithMenu)?.setupMenu(widgetHolder)
    }
}
