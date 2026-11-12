package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTCatalysedItemToItemRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTCatalysedItemToItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTEngraverBlockEntity(pos: BlockPos, state: BlockState) :
    HTCatalysedItemToItemBlockEntity(RagiumBlockEntityTypes.ENGRAVER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTCatalysedItemToItemRecipe> = RagiumRecipeLookups.ENGRAVING

    override fun onComplete() {
        playSound(SoundEvents.ALLAY_HURT)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.engraver
}
