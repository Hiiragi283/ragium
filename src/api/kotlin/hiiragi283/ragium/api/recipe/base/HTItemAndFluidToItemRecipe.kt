package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack

interface HTItemAndFluidToItemRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack>,
    HTProgressRecipe<HTItemAndFluidRecipeInput>
