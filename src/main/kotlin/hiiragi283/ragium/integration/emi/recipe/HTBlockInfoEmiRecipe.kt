package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.integration.emi.RagiumEmiCategories
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid

class HTBlockInfoEmiRecipe(id: ResourceLocation, firstInput: EmiIngredient, firstOutput: EmiStack) :
    HTSimpleEmiRecipe.Base(RagiumEmiCategories.BLOCK_INFO, id, firstInput, firstOutput) {
    constructor(id: ResourceLocation, device: ItemLike, output: ItemLike) : this(id, EmiStack.of(device), EmiStack.of(output))

    constructor(id: ResourceLocation, device: ItemLike, output: Fluid) : this(id, EmiStack.of(device), EmiStack.of(output))
}
