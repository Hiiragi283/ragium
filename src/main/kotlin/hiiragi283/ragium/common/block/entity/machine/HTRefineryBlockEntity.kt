package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTFluidSeparatingBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) : HTFluidSeparatingBlockEntity(RagiumBlockEntityTypes.REFINERY.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTFluidSeparatingRecipe> = RagiumRecipeLookups.REFINING

    override fun createRecipeHandler(): HTRecipeHandler = createRecipeHandler(HTSoundInstance(SoundEvents.FIRE_EXTINGUISH))

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.refinery
}
