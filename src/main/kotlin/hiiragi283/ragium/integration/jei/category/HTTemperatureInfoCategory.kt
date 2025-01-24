package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.integration.jei.HTTemperatureInfo
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
    recipeType: RecipeType<HTTemperatureInfo>,
    title: Component,
    icon: ItemLike,
) : AbstractRecipeCategory<HTTemperatureInfo>(
        recipeType,
        title,
        guiHelper.createDrawableItemLike(icon),
        18 * 3 + 8,
        18 * 1 + 8,
    ),
    HTRecipeCategory<HTTemperatureInfo> {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTTemperatureInfo, focuses: IFocusGroup) {
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

    override fun getRegistryName(recipe: HTTemperatureInfo): ResourceLocation = recipe.block
        .unwrapKey()
        .orElseThrow()
        .location()

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTTemperatureInfo> = HTTemperatureInfo.CODEC
}
