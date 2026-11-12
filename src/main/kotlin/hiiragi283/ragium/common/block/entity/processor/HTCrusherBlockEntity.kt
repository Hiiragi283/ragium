package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemToMultiItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) : HTItemToMultiItemBlockEntity(RagiumBlockEntityTypes.CRUSHER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemToMultiItemRecipe> = RagiumRecipeLookups.CRUSHING

    override fun onComplete() {
        playSound(SoundEvents.GRAVEL_BREAK)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.crusher
}
