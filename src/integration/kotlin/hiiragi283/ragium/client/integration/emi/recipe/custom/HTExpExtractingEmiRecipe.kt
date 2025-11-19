package hiiragi283.ragium.client.integration.emi.recipe.custom

import dev.emi.emi.EmiUtil
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.common.recipe.HTExpExtractingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.neoforge.common.Tags
import java.util.Random
import kotlin.random.asKotlinRandom

class HTExpExtractingEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation) :
    HTEmiRecipe<HTExpExtractingRecipe>(category, id, HTExpExtractingRecipe) {
    companion object {
        @JvmStatic
        private val UNIQUE_ID: Int = EmiUtil.RANDOM.nextInt()

        @JvmStatic
        private fun getTool(random: Random, index: Int): EmiIngredient {
            val items: EmiIngredient = EmiIngredient.of(Tags.Items.ENCHANTABLES)
            return when (index) {
                0 -> {
                    items.emiStacks
                        .map(EmiStack::getItemStack)
                        .filterNot(ItemStack::isEmpty)
                        .onEach { stack: ItemStack ->
                            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                        }.map(EmiStack::of)
                }
                else -> items.emiStacks
            }.random(random.asKotlinRandom())
        }
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addGeneratedSlot({ getTool(it, 0) }, UNIQUE_ID, getPosition(1), getPosition(0))
        widgets.addSlot(EmiStack.of(Items.GRINDSTONE), getPosition(1), getPosition(2)).catalyst(true)
        // Output
        widgets.addGeneratedOutput({ getTool(it, 1) }, UNIQUE_ID, getPosition(5), getPosition(0) + 4, true)
        widgets.addSlot(RagiumFluidContents.EXPERIENCE.toFluidEmi(), getPosition(5), getPosition(2))
    }

    override fun getBackingRecipe(): RecipeHolder<HTExpExtractingRecipe> = RecipeHolder(id, HTExpExtractingRecipe)
}
