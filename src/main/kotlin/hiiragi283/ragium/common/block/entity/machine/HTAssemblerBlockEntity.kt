package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTTripleItemToItemRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTDoubleItemToItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAssemblerBlockEntity(pos: BlockPos, state: BlockState) :
    HTDoubleItemToItemBlockEntity(RagiumBlockEntityTypes.ASSEMBLER.get(), LOOKUP, pos, state) {
    companion object {
        @JvmStatic
        private val LOOKUP: HTRecipeLookup<HTDoubleItemToItemRecipe> =
            HTRecipeLookup { context: HTRecipeLookup.Context ->
                RagiumRecipeLookups.ASSEMBLING
                    .getAllRecipes(context)
                    .mapNotNull { it.mapNotNull(HTTripleItemToItemRecipe::asDoubleInput) }
            }
    }

    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.CRAFTER_CRAFT)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.assembler
}
