package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(RagiumBlockEntityTypes.COMPRESSOR, pos, state) {
    override fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTItemToItemRecipe, *> = RagiumRecipeTypes.COMPRESSING

    override fun playSound() {
        playSound(SoundEvents.ANVIL_PLACE, pitch = 0.5f)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.compressor
}
