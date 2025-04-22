package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

class HTEmiRecipeCategory(id: ResourceLocation, val iconStack: EmiStack, private val name: Component = iconStack.name) :
    EmiRecipeCategory(id, iconStack) {
    constructor(id: ResourceLocation, icon: ItemLike) : this(id, EmiStack.of(icon))

    override fun getName(): Component = name
}
