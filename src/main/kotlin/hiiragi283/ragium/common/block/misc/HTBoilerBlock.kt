package hiiragi283.ragium.common.block.misc

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.HTGeneratorBlock
import hiiragi283.ragium.common.block.RagiumBlockBehaviors
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTBoilerBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTGeneratorBlock(type, properties),
    HTBlockWidgetHolderContext.FactoryForBlockEntity {

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult =
        RagiumBlockBehaviors.openMenu(player, pos)
}
