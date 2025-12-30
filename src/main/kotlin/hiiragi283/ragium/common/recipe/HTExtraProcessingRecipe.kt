package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

abstract class HTExtraProcessingRecipe(
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    val extra: Optional<HTItemResult>,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun assembleItems(input: HTRecipeInput, provider: HolderLookup.Provider, random: RandomSource): List<ItemStack> = buildList {
        addAll(super.assembleItems(input, provider, random))
        extra.map { it.getStackOrEmpty(provider) }.ifPresent(this::add)
    }
}
