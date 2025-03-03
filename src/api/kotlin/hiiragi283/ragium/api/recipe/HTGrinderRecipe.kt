package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.minecraft.world.item.crafting.Ingredient
import java.util.*

class HTGrinderRecipe(
    group: String,
    input: HTItemIngredient,
    catalyst: Optional<Ingredient>,
    itemOutput: HTItemOutput,
) : HTSingleItemRecipe(group, input, catalyst, itemOutput) {
    override fun getRecipeType(): HTRecipeType<*> = HTRecipeTypes.GRINDER
}
