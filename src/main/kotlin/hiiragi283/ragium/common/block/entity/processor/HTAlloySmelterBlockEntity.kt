package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTSizedShapelessBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSizedShapelessBlockEntity(RagiumBlockEntityTypes.ALLOY_SMELTER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTSizedShapelessRecipe> = RagiumRecipeLookups.ALLOYING

    override fun onComplete() {
        playSound(SoundEvents.FIRE_EXTINGUISH)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.alloySmelter
}
