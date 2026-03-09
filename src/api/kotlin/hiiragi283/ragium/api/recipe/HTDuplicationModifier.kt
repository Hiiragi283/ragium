package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack

interface HTDuplicationModifier {
    fun test(stack: ItemStack): Boolean

    fun calculateExtraAmount(stack: ItemStack): Int
}
