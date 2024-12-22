package hiiragi283.ragium.api.fluid

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Handle [LivingEntity] interaction for [hiiragi283.ragium.common.init.RagiumItemsNew.FILLED_FLUID_CUBE]
 * @see [HTFluidDrinkingHandlerRegistry]
 */
fun interface HTFluidDrinkingHandler {
    fun onDrink(stack: ItemStack, world: World, user: LivingEntity)
}
