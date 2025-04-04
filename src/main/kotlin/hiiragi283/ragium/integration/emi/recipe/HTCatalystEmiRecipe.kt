package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTCatalystEmiRecipe(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    private val input: Block,
    private val output: ItemStack,
) : HTSimpleEmiRecipe {
    override fun getFirstInput(): EmiIngredient = EmiStack.of(input)

    override fun getFirstOutput(): EmiStack = EmiStack.of(output)

    override fun getCategory(): EmiRecipeCategory = category

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = listOf(getFirstInput())

    override fun getOutputs(): List<EmiStack> = listOf(getFirstOutput())
}
