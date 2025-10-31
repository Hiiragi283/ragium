package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[HTIngredient]の拡張インターフェース
 */
interface HTItemIngredient : HTIngredient<ImmutableItemStack> {
    fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ImmutableItemStack>>

    fun test(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    fun interface CountGetter {
        fun getRequiredCount(stack: ImmutableItemStack): Int
    }
}
