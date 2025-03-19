package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class HTExtractingRecipe(group: String, ingredient: Ingredient, result: ItemStack) :
    HTSingleItemRecipe(RagiumRecipes.EXTRACTING, group, ingredient, result)
