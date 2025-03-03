package hiiragi283.ragium.data.server.integration

import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

object HTIERecipeProvider : HTRecipeProvider.Modded(IntegrationMods.IE) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Treated Wood
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(ItemTags.PLANKS)
            .fluidInput(RagiumFluidTags.CREOSOTE, 125)
            .itemOutput(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!)
            .save(output)

        // Iron Mechanical Component
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(HTTagPrefix.PLATE, VanillaMaterials.IRON, 2)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemOutput(IEItems.Ingredients.COMPONENT_IRON)
            .save(output)
        // Steel Mechanical Component
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(HTTagPrefix.PLATE, CommonMaterials.STEEL, 2)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemOutput(IEItems.Ingredients.COMPONENT_STEEL)
            .save(output)

        // Insulating Glass
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(Tags.Items.GLASS_BLOCKS, 2)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .itemOutput(IEBlocks.StoneDecoration.INSULATING_GLASS, 2)
            .save(output)

        // Industrial Hemp
        output.accept(
            id("growth/hemp_fiber"),
            HTGrowthChamberRecipe(
                IEItems.Misc.HEMP_SEEDS,
                RagiumItemTags.DIRT_SOILS,
                IEItems.Ingredients.HEMP_FIBER,
            ),
            null,
        )
    }
}
