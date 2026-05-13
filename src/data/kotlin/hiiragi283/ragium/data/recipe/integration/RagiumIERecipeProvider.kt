package hiiragi283.ragium.data.recipe.integration

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEFluids
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.data.recipe.HTChemicalReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

data object RagiumIERecipeProvider : HTSubRecipeProvider.Integration(RagiumAPI.MOD_ID, HCIConstants.IMMERSIVE) {
    override fun buildRecipeInternal() {
        // Insulating Glass
        HTCombiningRecipeBuilder.alloying(output) {
            result = resultCreator.create(IEBlocks.StoneDecoration.INSULATING_GLASS, 2)
            ingredients += inputCreator.create(Tags.Items.GLASS_BLOCKS, 2)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
        }
        // Duroplast
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(IETags.fluidResin, 4000)
            catalyst = HTBluePrintIngredient(0).toVanilla()
            result = resultCreator.create(IEBlocks.StoneDecoration.DUROPLAST)
        }
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(IETags.fluidResin)
            catalyst = HTBluePrintIngredient(1).toVanilla()
            result = resultCreator.create(IEItems.Ingredients.DUROPLAST_PLATE)
        }
        // HOP Graphite
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8)
            result = resultCreator.material(CommonParts.DUST, HCIntegrationMaterialKeys.HOP_GRAPHITE)
        }
        RagiumRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, HCIntegrationMaterialKeys.HOP_GRAPHITE)
            fluidIngredient = inputCreator.create(RagiumFluids.CREOSOTE, 250)
            result = resultCreator.create(IEItems.Ingredients.PLATE_HOP_GRAPHITE)
        }
        // TODO: 100 % Carbon Electrode

        // Redstone Acid
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            fluidIngredient = inputCreator.water(250)
            result += resultCreator.create(IEFluids.REDSTONE_ACID.still, 250)
        }
        // Acetaldehyde
        HTRefiningRecipeBuilder.create(output) {
            ingredient = inputCreator.create(IETags.fluidEthanol, 250)
            catalyst = itemCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.SILVER)
            fluidResults += resultCreator.create(IEFluids.ACETALDEHYDE.still, 250)
        }

        // Phenolic Resin
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(IETags.fluidAcetaldehyde, 300)
            ingredients += inputCreator.create(RagiumFluids.CREOSOTE, 200)
            fluidResults += resultCreator.create(IEFluids.PHENOLIC_RESIN.still, 200)
        }
        // Biodiesel
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(IETags.fluidPlantoil, 500)
            ingredients += inputCreator.create(IETags.fluidEthanol, 500)
            fluidResults += resultCreator.create(IEFluids.BIODIESEL.still)
        }
        // High-Cetane Diesel
        HTChemicalReactingRecipeBuilder.create(output) {
            ingredients += inputCreator.create(RagiumTags.Fluids.BIODIESEL, 950)
            ingredients += inputCreator.create(HTPotionFluidIngredient(Potions.STRENGTH), 50)
            fluidResults += resultCreator.create(IEFluids.HIGH_POWER_BIODIESEL.still)
        }
    }
}
