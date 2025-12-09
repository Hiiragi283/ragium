package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.EmiPort
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addPlus
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeHolder

class HTPlantingEmiRecipe(holder: RecipeHolder<HTPlantingRecipe>) :
    HTEmiHolderRecipe<HTPlantingRecipe>(RagiumEmiRecipeCategories.PLANTING, holder) {
    init {
        recipe.seed
            .unwrap()
            .map(
                { key: ResourceKey<Item> -> EmiPort.getItemRegistry().get(key)?.toEmi() },
                TagKey<Item>::toEmi,
            ).let(::addInput)
        addInput(recipe.soil)
        addInput(recipe.fluid)

        addOutputs(recipe.crop)
        addOutputs(recipe.seedResult)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.addPlus(getPosition(1), getPosition(1))

        // inputs
        widgets.addSlot(input(0), getPosition(1), getPosition(0))

        widgets.addSlot(input(1), getPosition(0), getPosition(2))
        widgets.addSlot(input(2), getPosition(2), getPosition(2))
        // outputs
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addSlot(output(1), getPosition(4.5), getPosition(2))
    }
}
