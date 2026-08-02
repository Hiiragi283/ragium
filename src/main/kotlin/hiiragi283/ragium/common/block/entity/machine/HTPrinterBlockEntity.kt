package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.ragium.common.block.entity.machine.base.HTDoubleItemToItemBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTPrinterBlockEntity(pos: BlockPos, state: BlockState) : HTDoubleItemToItemBlockEntity(RagiumBlockEntityTypes.PRINTER.get(), pos, state) {
    override fun getSecondarySlotInfo(): HTSlotInfo = HTSlotInfo.NONE

    override fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe> = RagiumRecipeLookups.PRINTING

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.assembler
}
