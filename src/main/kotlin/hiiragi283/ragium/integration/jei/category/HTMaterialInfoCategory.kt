package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.HTTypedMaterial
import hiiragi283.ragium.integration.jei.RagiumJEIPlugin
import hiiragi283.ragium.integration.jei.createEmptyMaterialStack
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTMaterialInfoCategory(guiHelper: IGuiHelper) :
    AbstractRecipeCategory<HTTypedMaterial>(
        RagiumJEIPlugin.MATERIAL_INFO,
        Component.literal("Material Info"),
        guiHelper.createDrawableItemLike(Items.BOOK),
        18 * 9 + 8,
        18 * 3 + 8,
    ),
    HTRecipeCategory<HTTypedMaterial> {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTTypedMaterial, focuses: IFocusGroup) {
        val (_: HTMaterialType, key: HTMaterialKey) = recipe
        for (prefix: HTTagPrefix in HTTagPrefix.entries) {
            val stacks: List<ItemStack> = RagiumAPI.materialRegistry
                .getItems(prefix, key)
                .map(::ItemStack)
                .takeIf(List<ItemStack>::isNotEmpty)
                ?: listOf(createEmptyMaterialStack(prefix, key))
            val x: Int = prefix.ordinal % 9
            val y: Int = prefix.ordinal / 9
            builder
                .addSlot(RecipeIngredientRole.CATALYST, getPosition(x), getPosition(y))
                .setStandardSlotBackground()
                .addItemStacks(stacks)
        }
    }

    override fun getRegistryName(recipe: HTTypedMaterial): ResourceLocation = commonId(recipe.material.name)

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTTypedMaterial> =
        RagiumAPI.materialRegistry.createTypedCodec()
}
