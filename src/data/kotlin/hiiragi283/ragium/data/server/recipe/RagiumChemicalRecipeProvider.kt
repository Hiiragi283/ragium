package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Dirt + Water -> Mud
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.DIRT)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.MUD)
            time = 20 * 5
        }
        // Gravel + Water -> Flint
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Tags.Items.GRAVELS)
            fluidIngredients += inputCreator.water(250)
            result += resultCreator.create(Items.FLINT)
            time = 20 * 5
        }

        // Water + Lava -> Obsidian
        HTMixingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.water(1000)
            fluidIngredients += inputCreator.lava(1000)
            result += resultCreator.create(Items.OBSIDIAN)
        }

        // Eldritch Flux
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            result += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }

        // Diamond + Raginite -> Ragi-Crystal
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            fluidIngredient = inputCreator.create(RagiumFluids.MOLTEN_RAGINITE, 100 * 8)
            result = resultCreator.material(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }

        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 500)
            recipeId suffix "_from_creosote_with_redstone"
        }
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 750)
            recipeId suffix "_from_creosote_with_raginite"
        }
        // Ethanol + Plant Oil -> Biofuel
        HTMixingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.ETHANOL, 750)
            fluidIngredients += inputCreator.create(RagiumFluids.PLANT_OIL, 1000)
            result += resultCreator.create(RagiumFluids.BIOFUEL, 500)
            recipeId suffix "_from_ethanol"
        }
    }
}
