package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) : HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.REFINERY, pos, state) {
    override fun getLookup(): HTRecipeLookup<out HTItemOrFluidRecipe> = RagiumRecipeLookups.CHEMICAL_WASHING // TODO

    override fun playSound() {
        playSound(SoundEvents.LAVA_POP)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.refinery
}
