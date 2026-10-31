package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.lookup.map
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) : HTItemAndFluidBlockEntity(RagiumBlockEntityTypes.MIXER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler = createRecipeHandler(
        RagiumRecipeLookups.MIXING.map(RTMixingRecipe::asSimple),
        HTSoundInstance(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE),
    )

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.mixer
}
