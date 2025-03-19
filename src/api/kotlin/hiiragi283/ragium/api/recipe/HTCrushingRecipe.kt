package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class HTCrushingRecipe(group: String, ingredient: Ingredient, result: ItemStack) :
    HTSingleItemRecipe(RagiumRecipes.CRUSHING, group, ingredient, result)
