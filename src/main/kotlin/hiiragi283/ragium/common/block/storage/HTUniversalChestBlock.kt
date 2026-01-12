package hiiragi283.ragium.common.block.storage

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

class HTUniversalChestBlock(properties: Properties) :
    HTBasicEntityBlock(RagiumBlockEntityTypes.UNIVERSAL_CHEST, properties),
    HTBlockWithDescription,
    BlockUIMenuType.BlockUI {
    override fun getDescription(): HTTranslation = RagiumTranslation.UNIVERSAL_CHEST

    override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI {
        val level: Level = holder.player.level()
        val pos: BlockPos = holder.pos
        return HTUniversalChestManager.createUI(
            holder.player,
            level.getBlockState(pos).block.name,
            level.getTypedBlockEntity<HTUniversalChestBlockEntity>(pos)?.getItemHandler(null),
        )
    }
}
