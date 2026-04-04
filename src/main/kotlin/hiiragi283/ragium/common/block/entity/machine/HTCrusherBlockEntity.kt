package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToChancedBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) : HTItemToChancedBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.GRINDSTONE_USE)
    }

    override fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTItemToChancedRecipe, *> = HCRecipeTypes.CRUSHING

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.crusher
}
