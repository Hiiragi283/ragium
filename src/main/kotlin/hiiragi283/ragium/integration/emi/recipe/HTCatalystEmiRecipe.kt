package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTCatalystEmiRecipe(
    category: EmiRecipeCategory,
    id: ResourceLocation,
    firstInput: EmiIngredient,
    firstOutput: EmiStack,
) : HTSimpleEmiRecipe.Base(category, id, firstInput, firstOutput) {
    constructor(
        category: EmiRecipeCategory,
        id: ResourceLocation,
        block: Block,
        stack: ItemStack,
    ) : this(category, id, EmiStack.of(block), EmiStack.of(stack))
}
