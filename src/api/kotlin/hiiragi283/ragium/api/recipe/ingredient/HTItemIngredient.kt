package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

/**
 * [ItemStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTItemIngredient : HTIngredient<ItemStack> {
    companion object {
        @JvmStatic
        fun wrapVanilla(ingredient: Ingredient): HTItemIngredient = object : HTItemIngredient {
            override fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ItemStack>> = Either.right(getMatchingStacks())

            override fun test(stack: ItemStack): Boolean = ingredient.test(stack)

            override fun testOnlyType(stack: ItemStack): Boolean = ingredient.test(stack)

            override fun getMatchingStack(stack: ItemStack): ItemStack = if (test(stack)) stack.copyWithCount(1) else ItemStack.EMPTY

            override fun getRequiredAmount(stack: ItemStack): Int = if (test(stack)) 1 else 0

            override fun hasNoMatchingStacks(): Boolean = ingredient.hasNoItems()

            override fun getMatchingStacks(): List<ItemStack> = ingredient.items.toList()
        }
    }

    fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ItemStack>>

    fun getRequiredAmount(stack: HTItemStorageStack): Int = getRequiredAmount(stack.stack)

    fun interface CountGetter {
        fun getRequiredCount(stack: ItemStack): Int

        fun getRequiredCount(stack: HTItemStorageStack): Int = getRequiredCount(stack.stack)
    }
}
