package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.core.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.storage.HTStorageBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

abstract class HTStorageBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTBasicEntityBlock(type, properties),
    HTBlockWithDescription,
    HTBlockWidgetHolderContext.Factory {
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

    final override fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        (context.blockEntity as? HTStorageBlockEntity)?.setupMenu(widgetHolder)
    }
}
