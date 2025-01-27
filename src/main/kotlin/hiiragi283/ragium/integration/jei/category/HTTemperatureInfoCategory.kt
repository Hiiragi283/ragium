package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.integration.jei.JEITemperatureInfo
import hiiragi283.ragium.integration.jei.RagiumJEIPlugin
import hiiragi283.ragium.integration.jei.createEmptyBlockStack
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

class HTTemperatureInfoCategory(
    guiHelper: IGuiHelper,
    recipeType: RecipeType<JEITemperatureInfo>,
    title: Component,
    icon: ItemLike,
) : AbstractRecipeCategory<JEITemperatureInfo>(
        recipeType,
        title,
        guiHelper.createDrawableItemLike(icon),
        18 * 3 + 8,
        18 * 1 + 8,
    ),
    HTRecipeCategory<JEITemperatureInfo> {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: JEITemperatureInfo, focuses: IFocusGroup) {
        // Block
        var item = ItemStack(recipe.block.value())
        if (item.isEmpty) {
            item = createEmptyBlockStack(recipe.block)
        }
        builder
            .addSlot(RecipeIngredientRole.INPUT, getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(item)
        // Tier
        builder
            .addSlot(RecipeIngredientRole.OUTPUT, getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredient(RagiumJEIPlugin.MACHINE_TIER_TYPE, recipe.machineTier)
    }

    override fun getRegistryName(recipe: JEITemperatureInfo): ResourceLocation = recipe.block.idOrThrow

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<JEITemperatureInfo> = JEITemperatureInfo.CODEC
}
