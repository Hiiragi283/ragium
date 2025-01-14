package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.integration.jei.RagiumJEIPlugin
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient

class HTMaterialInfoCategory(guiHelper: IGuiHelper) :
    HTRecipeCategory<HTMaterialKey>(
        RagiumJEIPlugin.MATERIAL_INFO,
        Component.literal("Material Info"),
        guiHelper.createDrawableItemLike(Items.BOOK),
        18 * 9 + 8,
        18 * 3 + 8,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTMaterialKey, focuses: IFocusGroup) {
        for (prefix: HTTagPrefix in HTTagPrefix.entries) {
            val x: Int = prefix.ordinal % 9
            val y: Int = prefix.ordinal / 9
            builder
                .addSlot(RecipeIngredientRole.CATALYST, getPosition(x), getPosition(y))
                .setStandardSlotBackground()
                .addIngredients(Ingredient.of(prefix.createTag(recipe)))
        }
    }

    override fun getRegistryName(recipe: HTMaterialKey): ResourceLocation = commonId(recipe.name)

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTMaterialKey> = HTMaterialKey.CODEC
}
