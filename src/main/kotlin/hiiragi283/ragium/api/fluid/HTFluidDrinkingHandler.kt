package hiiragi283.ragium.api.fluid

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * 液体キューブを右クリックした時の処理を行う
 * @see [HTFluidDrinkingHandlerRegistry]
 * @see [hiiragi283.ragium.common.item.HTFilledFluidCubeItem.finishUsing]
 */
fun interface HTFluidDrinkingHandler {
    fun onDrink(stack: ItemStack, world: World, user: LivingEntity)
}
