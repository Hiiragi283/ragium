package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.ingredient.HTStackPreview
import hiiragi283.ragium.api.recipe.RTResourceExtractingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.debug.DebugEntryBiome
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.util.context.ContextMap
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.biome.Biome

class RTResourceExtractingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTResourceExtractingRecipe>(
        guiHelper,
        RagiumJeiRecipeTypes.RESOURCE_EXTRACTING,
        18 * 4,
        18 * 1,
        RTResourceExtractingRecipe.CODEC
    ) {
    /**
     * @see DebugEntryBiome
     */
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTResourceExtractingRecipe, focuses: IFocusGroup) {
        val player: Player = Minecraft.getInstance().player ?: return
        val biomeAt: Holder<Biome> = player.level().getBiome(player.blockPosition())
        // biome condition
        builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, getPosition(0), getPosition(0))
            .add(
                HTStackPreview { contextMap: ContextMap ->
                    val condition: HTBiomeCondition = recipe.condition
                    condition
                        .display
                        .resolveForStacks(contextMap)
                        .map { stack: ItemStack ->
                            if (condition.test(biomeAt)) {
                                stack[DataComponents.ENCHANTMENT_GLINT_OVERRIDE] = true
                            }
                            stack
                        }
                }
            )
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: RTResourceExtractingRecipe,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
