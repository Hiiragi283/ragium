package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

interface HTPlantingRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<Iterable<ItemStack>>,
    HTProgressRecipe<RecipeInput>
