package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.common.block.entity.machine.base.HTMixerBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTFluidMixerBlockEntity(pos: BlockPos, state: BlockState) : HTMixerBlockEntity(RagiumBlockEntityTypes.FLUID_MIXER, pos, state) {
    override fun createItemOutputs(): HTItemOutputHandler {
        TODO("Not yet implemented")
    }

    override fun createFluidOutputs(): HTFluidOutputHandler {
        TODO("Not yet implemented")
    }

    override fun createInput(): HTMixingRecipeInput {
        TODO("Not yet implemented")
    }

    override fun consumeInputs(amounts: HTMixingRecipe.RequiredAmounts) {
        TODO("Not yet implemented")
    }
}
