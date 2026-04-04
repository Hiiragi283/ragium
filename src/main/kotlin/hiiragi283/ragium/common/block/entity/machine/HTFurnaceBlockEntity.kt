package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTItemToItemRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(RagiumBlockEntityTypes.ELECTRIC_FURNACE, pos, state) {
    // TODO: Support Blasting or Smoking recipe type
    override fun getCache(): HTRecipeCache<SingleRecipeInput, out HTItemToItemRecipe> = HTVanillaRecipeTypes.SMELTING.createCache()

    override fun playSound() {
        playSound(SoundEvents.FIRE_EXTINGUISH)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.electricFurnace
}
