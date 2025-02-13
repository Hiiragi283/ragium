package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidStack
import hiiragi283.ragium.integration.jei.entry.HTStirlingFuelEntry
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

class HTStirlingFuelCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTStirlingFuelEntry> {
    override fun getRecipeType(): RecipeType<HTStirlingFuelEntry> = RagiumJEIRecipeTypes.STIRLING

    override fun getTitle(): Component = HTMachineType.STIRLING_GENERATOR.text

    override fun getIcon(): IDrawable? = guiHelper.createDrawableItemLike(HTMachineType.STIRLING_GENERATOR)

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTStirlingFuelEntry, focuses: IFocusGroup) {
        val burnTime: Int = recipe.burnTime
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(ItemStack(recipe.input))
        // Fluid Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addFluidStack(FluidStack(Fluids.WATER, burnTime / 10))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ASH).toStack(burnTime / 200))
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTStirlingFuelEntry, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(2.5), getPosition(0))
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTStirlingFuelEntry> = HTStirlingFuelEntry.CODEC
}
