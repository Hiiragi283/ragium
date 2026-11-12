package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemToFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) : HTItemToFluidBlockEntity(RagiumBlockEntityTypes.MELTER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemToFluidRecipe> = RagiumRecipeLookups.MELTING

    override fun onComplete() {
        playSound(SoundEvents.LAVA_POP)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.melter
}
