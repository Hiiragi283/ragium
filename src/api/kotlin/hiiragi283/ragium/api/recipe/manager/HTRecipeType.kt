package hiiragi283.ragium.api.recipe.manager

import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

interface HTRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTRecipeGetter<INPUT, RECIPE>,
    HTHasText
