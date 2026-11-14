package hiiragi283.ragium.common.block.misc

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.block.RagiumBlockBehaviors
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTUpgradeStationBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTHorizontalEntityBlock(type, properties),
    HTBlockWidgetHolderContext.FactoryForBlockEntity {
    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult =
        RagiumBlockBehaviors.openMenu(player, pos)
}
