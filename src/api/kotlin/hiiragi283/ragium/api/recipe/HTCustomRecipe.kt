package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.ImmutableRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.level.Level

abstract class HTCustomRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    final override fun matches(input: CraftingInput, level: Level): Boolean = matches(ImmutableRecipeInput(input), level)

    protected abstract fun matches(input: ImmutableRecipeInput, level: Level): Boolean

    final override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack =
        assemble(ImmutableRecipeInput(input), registries)

    protected abstract fun assemble(input: ImmutableRecipeInput, registries: HolderLookup.Provider): ItemStack
}
