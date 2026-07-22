package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.common.recipe.VanillaRecipeLookups
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFurnaceBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity.Basic(RagiumBlockEntityTypes.ELECTRIC_FURNACE.get(), pos, state) {
    // TODO: Support Blasting or Smoking recipe type
    override fun getCache(): HTRecipeCaches.SingleItem<out HTItemToItemRecipe> = HTRecipeCaches.SingleItem(VanillaRecipeLookups.SMELTING)

    override fun playSound() {
        playSound(SoundEvents.FIRE_EXTINGUISH)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.electricFurnace
}
