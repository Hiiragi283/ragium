package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.registry.getHolderDataMap
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeHolder

class HTCompressingEmiRecipe(holder: RecipeHolder<HTCompressingRecipe>) :
    HTEmiHolderRecipe<HTCompressingRecipe>(RagiumEmiRecipeCategories.COMPRESSING, holder) {
    companion object {
        @JvmStatic
        private val EXPLOSIVES: EmiIngredient by lazy {
            BuiltInRegistries.ITEM
                .asLookup()
                .getHolderDataMap(RagiumDataMapTypes.EXPLOSIVE)
                .keys
                .map { it.get().toEmi() }
                .let(EmiIngredient::of)
        }
    }

    init {
        addInput(recipe.ingredient)

        addCatalyst(recipe.catalyst)

        addOutputs(recipe.result)
        addOutputs(HTCompressingRecipe.ASH_RESULT.toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow()
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0), HTBackgroundType.INPUT)
        widgets.addSlot(EXPLOSIVES, getPosition(3.25), getPosition(0), HTBackgroundType.EXTRA_INPUT)
        // catalyst
        widgets.addSlot(catalyst(0), getPosition(1.5), getPosition(2), HTBackgroundType.NONE)
        // output
        widgets.addSlot(output(0), getPosition(5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addSlot(output(1), getPosition(5), getPosition(2), HTBackgroundType.EXTRA_OUTPUT)
    }
}
