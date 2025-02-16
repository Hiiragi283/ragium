package hiiragi283.ragium.data.server.integration

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTGrowthChamberRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTIERecipeProvider : RagiumRecipeProvider.ModChild("immersiveengineering") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Treated Wood
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(ItemTags.PLANKS)
            .fluidInput(RagiumFluidTags.CREOSOTE, FluidType.BUCKET_VOLUME / 8)
            .itemOutput(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!)
            .save(output)

        // Iron Mechanical Component
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(HTTagPrefix.PLATE, VanillaMaterials.IRON, 2)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemOutput(IEItems.Ingredients.COMPONENT_IRON)
            .save(output)
        // Steel Mechanical Component
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(HTTagPrefix.PLATE, CommonMaterials.STEEL, 2)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemOutput(IEItems.Ingredients.COMPONENT_STEEL)
            .save(output)

        // Coal Coke Dust
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(RagiumItemTags.COAL_COKE)
            .itemOutput(IETags.coalCokeDust)
            .save(output)

        // Insulating Glass
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(Tags.Items.GLASS_BLOCKS, 2)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.IRON)
            .itemOutput(IEBlocks.StoneDecoration.INSULATING_GLASS, 2)
            .save(output)

        // Industrial Hemp
        HTGrowthChamberRecipeBuilder()
            .itemInput(IETags.seedsHemp)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(IETags.fiberHemp, 2)
            .save(output)
    }
}
