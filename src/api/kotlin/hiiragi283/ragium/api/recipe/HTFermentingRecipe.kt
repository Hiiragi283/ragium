package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class HTFermentingRecipe(group: String, ingredient: Ingredient, result: ItemStack) :
    HTSingleItemRecipe(RagiumRecipes.FERMENTING, group, ingredient, result)
