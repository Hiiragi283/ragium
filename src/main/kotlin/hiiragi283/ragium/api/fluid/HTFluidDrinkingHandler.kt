package hiiragi283.ragium.api.fluid

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

fun interface HTFluidDrinkingHandler {
    fun onDrink(stack: ItemStack, world: World, user: LivingEntity)
}
