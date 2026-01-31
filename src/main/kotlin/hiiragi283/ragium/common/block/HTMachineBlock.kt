package hiiragi283.ragium.common.block

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.core.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
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

open class HTMachineBlock(private val translation: HTTranslation, type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTHorizontalEntityBlock(type, properties),
    HTBlockWithDescription,
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
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (player is ServerPlayer) {
            HTBlockWidgetHolderContext.openMenu(player, pos)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_ACTIVE)
    }

    override fun getDescription(): HTTranslation = translation

    override fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        (context.blockEntity as? HTMachineBlockEntity)?.setupMenu(widgetHolder)
    }
}
