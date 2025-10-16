package hiiragi283.ragium.client.integration.emi.recipe

import dev.emi.emi.EmiPort
import dev.emi.emi.EmiUtil
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.render.EmiTooltipComponents
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.recipe.EmiSmithingRecipe
import hiiragi283.ragium.api.extension.compose
import hiiragi283.ragium.client.integration.emi.RagiumEmiPlugin
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SmithingRecipeInput
import java.util.*
import kotlin.random.asKotlinRandom

class HTSmithingModifyEmiRecipe(template: EmiIngredient, addition: EmiIngredient, private val recipe: HTSmithingModifyRecipe) :
    EmiSmithingRecipe(
        template,
        BuiltInRegistries
            .ITEM
            .asSequence()
            .map(::ItemStack)
            .filter(recipe::isBaseIngredient)
            .map(ItemStack::toEmi)
            .toList()
            .let(EmiIngredient::of),
        addition,
        EmiStack.EMPTY,
        EmiPort.getId(recipe),
    ) {
    private val uniq: Int = EmiUtil.RANDOM.nextInt()

    override fun supportsRecipeTree(): Boolean = false

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1)
        widgets.addSlot(template, 0, 0)
        widgets
            .addGeneratedSlot({ random: Random -> getStack(random, 0) }, uniq, 18, 0)
            .appendTooltip(EmiTooltipComponents::getIngredientTooltipComponent.compose(EmiIngredient::getEmiStacks))
        widgets
            .addGeneratedSlot({ random: Random -> getStack(random, 1) }, uniq, 36, 0)
            .appendTooltip(EmiTooltipComponents::getIngredientTooltipComponent.compose(EmiIngredient::getEmiStacks))
        widgets.addGeneratedSlot({ random: Random -> getStack(random, 2) }, uniq, 94, 0).recipeContext(this)
    }

    private fun getStack(random: Random, index: Int): EmiStack {
        val input: EmiStack = this.input.emiStacks.random(random.asKotlinRandom())
        val addition: EmiStack = this.addition.emiStacks.random(random.asKotlinRandom())
        val recipeInput = SmithingRecipeInput(template.emiStacks[0].itemStack, input.itemStack, addition.itemStack)
        val result: EmiStack = recipe.assemble(recipeInput, RagiumEmiPlugin.registryAccess).toEmi()
        return arrayOf(input, addition, result)[index]
    }
}
