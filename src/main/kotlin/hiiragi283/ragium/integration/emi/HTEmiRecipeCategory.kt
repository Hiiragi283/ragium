package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ItemLike

class HTEmiRecipeCategory(path: String, val iconStack: EmiStack) :
    EmiRecipeCategory(RagiumAPI.id(path), iconStack, iconStack, EmiRecipeSorting.compareOutputThenInput()) {
    constructor(path: String, icon: ItemLike) : this(path, EmiStack.of(icon))

    override fun getName(): Component = iconStack.name
}
