package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTItemIngredient : HTIngredient<ItemStack> {
    fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ImmutableItemStack>>

    fun getRequiredAmount(stack: ImmutableItemStack): Int = getRequiredAmount(stack.stack)

    fun interface CountGetter {
        fun getRequiredCount(stack: ItemStack): Int

        fun getRequiredCount(stack: ImmutableItemStack): Int = getRequiredCount(stack.stack)
    }
}
