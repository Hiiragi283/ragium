package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.ragium.common.block.entity.machine.base.HTDoubleItemToItemBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAssemblerBlockEntity(pos: BlockPos, state: BlockState) : HTDoubleItemToItemBlockEntity(RagiumBlockEntityTypes.ASSEMBLER.get(), pos, state) {
    override fun getSecondarySlotInfo(): HTSlotInfo = HTSlotInfo.EXTRA_INPUT

    override fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe> = RagiumRecipeLookups.ASSEMBLING

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl(SoundEvents.CRAFTER_CRAFT)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.assembler
}
