package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.PYROLYZER, pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemAndFluidRecipeInput, out HTItemOrFluidRecipe> = RagiumRecipeLookups.PYROLYZING

    override fun playSound() {
        playSound(SoundEvents.FIRE_EXTINGUISH)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.pyrolyzer
}
