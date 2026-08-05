package hiiragi283.ragium.data.recipe.integration

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEFluids
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.data.recipe.HTAlloyingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTChemicalReactingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTRefiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

class RagiumIERecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider.Integration(packOutput, future, RagiumAPI.MOD_ID, HCIConstants.IMMERSIVE) {
    override fun buildRecipes() {
        // Insulating Glass
        HTAlloyingRecipeBuilder.create {
            result {
                +IEBlocks.StoneDecoration.INSULATING_GLASS
                count = 2
            }
            ingredient {
                +Tags.Items.GLASS_BLOCKS
                count = 2
            }
            ingredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON) }
            condition { +condition }
        }.save(exporter)
        // Duroplast
        RagiumRecipeBuilder.freezing {
            fluidIngredient {
                +IETags.fluidResin
                amount = 4000
            }
            itemIngredient { +HTBluePrintIngredient(0) }
            result { +IEBlocks.StoneDecoration.DUROPLAST }
            condition { +condition }
        }.save(exporter)
        RagiumRecipeBuilder.freezing {
            fluidIngredient { +IETags.fluidResin }
            itemIngredient { +HTBluePrintIngredient(1) }
            result { +IEItems.Ingredients.DUROPLAST_PLATE }
            condition { +condition }
        }.save(exporter)
        // HOP Graphite
        RagiumRecipeBuilder.compressing {
            ingredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE)
                count = 8
            }
            result { +HTItemResult.MaterialPartEntry(CommonParts.DUST, HCIntegrationMaterialKeys.HOP_GRAPHITE) }
            condition { +condition }
        }.save(exporter)
        RagiumRecipeBuilder.bathing {
            itemIngredient { +tag(CommonTagPrefixes.DUST, HCIntegrationMaterialKeys.HOP_GRAPHITE) }
            fluidIngredient {
                +RagiumFluids.CREOSOTE
                amount = 250
            }
            result { +IEItems.Ingredients.PLATE_HOP_GRAPHITE }
            condition { +condition }
        }.save(exporter)
        // TODO: 100 % Carbon Electrode

        // Redstone Acid
        HTMixingRecipeBuilder.create {
            itemIngredient { +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE) }
            fluidIngredient {
                water()
                amount = 250
            }
            fluidResult {
                +IEFluids.REDSTONE_ACID.still
                amount = 250
            }
            condition { +condition }
        }.save(exporter)
        // Acetaldehyde
        HTRefiningRecipeBuilder.create {
            fluidIngredient {
                +IETags.fluidEthanol
                amount = 250
            }
            catalyst { +tag(CommonTagPrefixes.PLATE, CommonMaterialKeys.SILVER) }
            fluidResult {
                +IEFluids.ACETALDEHYDE.still
                amount = 250
            }
            condition { +condition }
        }.save(exporter)

        // Phenolic Resin
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +IETags.fluidAcetaldehyde
                amount = 300
            }
            ingredient {
                +RagiumFluids.CREOSOTE
                amount = 200
            }
            fluidResult {
                +IEFluids.PHENOLIC_RESIN.still
                amount = 200
            }
            condition { +condition }
        }.save(exporter)
        // Biodiesel
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +IETags.fluidPlantoil
                amount = 500
            }
            ingredient {
                +IETags.fluidEthanol
                amount = 500
            }
            fluidResult {
                +IEFluids.BIODIESEL.still
            }
            condition { +condition }
        }.save(exporter)
        // High-Cetane Diesel
        HTChemicalReactingRecipeBuilder.create {
            ingredient {
                +RagiumTags.Fluids.BIODIESEL
                amount = 950
            }
            ingredient {
                +HTPotionFluidIngredient(Potions.STRENGTH)
                amount = 50
            }
            fluidResult {
                +IEFluids.HIGH_POWER_BIODIESEL.still
            }
            condition { +condition }
        }.save(exporter)
    }

    override fun getName(): String = "IE Recipes"
}
