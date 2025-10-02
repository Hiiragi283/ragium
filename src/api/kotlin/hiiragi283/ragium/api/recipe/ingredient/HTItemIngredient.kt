package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTItemIngredient : HTIngredient<ItemStack> {
    fun unwrap(): Either<Pair<HolderSet<Item>, Int>, List<ItemStack>>

    fun interface CountGetter {
        fun getRequiredCount(stack: ItemStack): Int
    }
}
