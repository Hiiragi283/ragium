package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemAndFluidToItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTChemicalBathBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndFluidToItemBlockEntity(RagiumBlockEntityTypes.CHEMICAL_BATH.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemAndFluidToItemRecipe> = RagiumRecipeLookups.BATHING

    override fun onComplete() {
        playSound(SoundEvents.SPONGE_ABSORB)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.chemicalBath
}
