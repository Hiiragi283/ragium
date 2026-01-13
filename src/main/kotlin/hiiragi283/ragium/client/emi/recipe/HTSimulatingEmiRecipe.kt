package hiiragi283.ragium.client.emi.recipe

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.integration.emi.HTHolderModularEmiRecipe
import hiiragi283.core.api.integration.emi.slot.HTListItemSlot
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTSimulatingEmiRecipe(holder: RecipeHolder<HTSimulatingRecipe<*>>) :
    HTHolderModularEmiRecipe<HTSimulatingRecipe<*>>({ recipe: HTSimulatingRecipe<*>, root: UIElement ->
        val element: UIElement = when (recipe) {
            is HTBlockSimulatingRecipe -> recipe.catalyst.map { ItemStack(it.value()) }
            is HTEntitySimulatingRecipe -> recipe.catalyst.map(HTImitationSpawnerBlock::createStack)
            else -> null
        }?.let(::HTListItemSlot)?.let(::HTItemSlotElement) ?: HTItemSlotElement()

        RagiumModularUIHelper.complex(
            root,
            inputSlot(recipe.ingredient.getOrNull()),
            element,
            outputSlot(recipe.result.getLeft()),
            outputSlot(recipe.result.getRight()),
        )
        root.addChild(RagiumModularUIHelper.fakeProgress(recipe.time))
    }, RagiumEmiRecipeCategories.SIMULATING, holder)
