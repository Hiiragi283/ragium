package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

class HTEmiRecipeCategory(id: ResourceLocation, val icon: EmiStack, private val name: Component = icon.name) : EmiRecipeCategory(id, icon) {
    constructor(id: ResourceLocation, icon: ItemLike) : this(id, EmiStack.of(icon))

    constructor(recipeType: HTMachineRecipeType, icon: ItemLike) : this(
        RagiumAPI.id(recipeType.name),
        EmiStack.of(icon),
    )

    override fun getName(): Component = name
}
