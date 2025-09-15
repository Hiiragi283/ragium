package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.EmiPort
import dev.emi.emi.EmiUtil
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.render.EmiTooltipComponents
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.recipe.EmiSmithingRecipe
import hiiragi283.ragium.api.extension.holdersNotEmpty
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.SmithingRecipeInput
import java.util.*
import java.util.function.Supplier

class HTSmithingModifyEmiRecipe(template: EmiIngredient, addition: EmiIngredient, private val recipe: HTSmithingModifyRecipe) :
    EmiSmithingRecipe(
        template,
        BuiltInRegistries.ITEM
            .holdersNotEmpty()
            .map(Holder<Item>::value)
            .map(Item::getDefaultInstance)
            .filter(recipe::isBaseIngredient)
            .map(EmiStack::of)
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
            .appendTooltip(Supplier { EmiTooltipComponents.getIngredientTooltipComponent(input.emiStacks) })
        widgets
            .addGeneratedSlot({ random: Random -> getStack(random, 1) }, uniq, 36, 0)
            .appendTooltip(Supplier { EmiTooltipComponents.getIngredientTooltipComponent(addition.emiStacks) })
        widgets.addGeneratedSlot({ random: Random -> getStack(random, 2) }, uniq, 94, 0).recipeContext(this)
    }

    private fun getStack(random: Random, index: Int): EmiStack {
        val input: EmiStack = this.input.emiStacks.let { stacks: List<EmiStack> ->
            stacks[random.nextInt(stacks.size)]
        }
        val addition: EmiStack = this.addition.emiStacks.let { stacks: List<EmiStack> ->
            stacks[random.nextInt(stacks.size)]
        }
        val recipeInput = SmithingRecipeInput(
            template.emiStacks[0].itemStack,
            input.itemStack,
            addition.itemStack,
        )
        val result: EmiStack = Minecraft
            .getInstance()
            .level
            ?.registryAccess()
            ?.let {
                recipe.assemble(recipeInput, it)
            }?.let(EmiStack::of) ?: EmiStack.EMPTY
        return arrayOf(input, addition, result)[index]
    }
}
