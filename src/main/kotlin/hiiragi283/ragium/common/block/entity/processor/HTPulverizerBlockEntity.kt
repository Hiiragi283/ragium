package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPulverizerBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity(RagiumBlockEntityTypes.PULVERIZER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemToItemRecipe> = RagiumRecipeLookups.PULVERIZING

    override fun onComplete() {
        playSound(SoundEvents.GRINDSTONE_USE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.pulverizer
}
