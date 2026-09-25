package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.base.HTTripleItemToItemRecipe
import hiiragi283.lib.recipe.lookup.mapNotNull
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTDoubleItemToItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTDoubleItemToItemBlockEntity(
        RagiumBlockEntityTypes.ALLOY_SMELTER.get(),
        RagiumRecipeLookups.ALLOYING.mapNotNull(HTTripleItemToItemRecipe::asDoubleInput),
        pos,
        state
    ) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.FIRE_EXTINGUISH)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.alloySmelter
}
