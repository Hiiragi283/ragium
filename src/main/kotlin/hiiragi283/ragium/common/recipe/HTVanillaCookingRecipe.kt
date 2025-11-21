package hiiragi283.ragium.common.recipe

import net.minecraft.world.item.crafting.AbstractCookingRecipe

class HTVanillaCookingRecipe(
    recipe: AbstractCookingRecipe,
    val cookingTime: Int = recipe.cookingTime,
    val experience: Float = recipe.experience,
) : HTVanillaSingleItemRecipe(recipe)
