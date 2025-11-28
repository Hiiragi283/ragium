package hiiragi283.ragium.client.integration.emi.recipe.custom

import dev.emi.emi.EmiUtil
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.common.recipe.machine.HTCopyEnchantingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTCopyEnchantingEmiRecipe(category: HTEmiRecipeCategory, id: ResourceLocation) :
    HTEmiRecipe<HTCopyEnchantingRecipe>(category, id, HTCopyEnchantingRecipe) {
    companion object {
        @JvmStatic
        private val UNIQUE_ID: Int = EmiUtil.RANDOM.nextInt()
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        widgets.addGeneratedSlot({ HTEnchantEmiRecipeHelper.getTool(it, 1) }, UNIQUE_ID, getPosition(0), getPosition(0))
        widgets.addTexture(EmiTexture.PLUS, getPosition(1) + 3, getPosition(0) + 3)
        widgets.addSlot(EmiStack.of(Items.ENCHANTED_BOOK), getPosition(2), getPosition(0))

        widgets.addTexture(EmiTexture.EMPTY_FLAME, getPosition(1) + 2, getPosition(1) + 2)
        widgets.addSlot(RagiumFluidContents.EXPERIENCE.toFluidEmi(), getPosition(1), getPosition(2))

        widgets.addGeneratedOutput(
            { HTEnchantEmiRecipeHelper.getTool(it, 0) },
            UNIQUE_ID,
            getPosition(4.5),
            getPosition(1),
            true,
        )
    }

    override fun getBackingRecipe(): RecipeHolder<HTCopyEnchantingRecipe> = RecipeHolder(id, HTCopyEnchantingRecipe)
}
