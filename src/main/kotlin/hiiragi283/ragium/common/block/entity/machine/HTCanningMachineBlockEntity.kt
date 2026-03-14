package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCanningMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.CANNING_MACHINE, pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemAndFluidRecipeInput, out HTItemOrFluidRecipe, *> = RagiumRecipeTypes.REFINING // TODO

    override fun playSound() {
        playSound(SoundEvents.BOTTLE_FILL)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.canningMachine
}
