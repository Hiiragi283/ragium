package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.network.chat.Component

class HTEmiRecipeCategory(path: String, val iconStack: EmiStack, sorter: Comparator<EmiRecipe>) :
    EmiRecipeCategory(RagiumAPI.id(path), iconStack, iconStack, sorter) {
    override fun getName(): Component = iconStack.name
}
