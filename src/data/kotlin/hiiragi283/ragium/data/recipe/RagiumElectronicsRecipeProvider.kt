package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class RagiumElectronicsRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        circuitBoard()
        circuit()
        silicon()
        diamond()
    }

    override fun getName(): String = "Electronics Recipes"

    private fun circuitBoard() {
        // Plastic -> Blank Circuit Board
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            result { +RagiumItems.BLANK_CIRCUIT_BOARD }
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(HTCommonTags.Items.PLASTICS)
                count = 2
            }
            ingredient { +holderSet(HTCommonTags.Items.SILICON) }
            result {
                +RagiumItems.BLANK_CIRCUIT_BOARD
                count = 2
            }
            recipeId suffix "_with_crude_silicon"
        }.save(exporter)
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(HTCommonTags.Items.PLASTICS)
                count = 4
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICON) }
            result {
                +RagiumItems.BLANK_CIRCUIT_BOARD
                count = 4
            }
            recipeId suffix "_with_silicon"
        }.save(exporter)

        // Blank + Copper -> Basic
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.BLANK_CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.COPPER)
                count = 3
            }
            result { +RagiumItems.BASIC_CIRCUIT_BOARD }
        }.save(exporter)
        // Blank + Gold -> Advanced
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.BLANK_CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.GOLD)
                count = 3
            }
            result { +RagiumItems.ADVANCED_CIRCUIT_BOARD }
        }.save(exporter)
        // Blank + Silver -> Elite
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.BLANK_CIRCUIT_BOARD } }
            ingredient {
                +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.SILVER)
                count = 3
            }
            result { +RagiumItems.ELITE_CIRCUIT_BOARD }
        }.save(exporter)
    }

    private fun circuit() {
        // Basic
        VanillaRecipeBuilders.shaped {
            +"AAA"
            +"BCB"
            +"AAA"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER) }
            define('B') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('C') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result { +RagiumItems.BASIC_CIRCUIT }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                items { +RagiumItems.BASIC_CIRCUIT_BOARD }
                count = 4
            }
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE)
                count = 4
            }
            result {
                +RagiumItems.BASIC_CIRCUIT
                count = 4
            }
        }.save(exporter)
        // Basic -> Advanced
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.LAPIS) }
            define('B') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.GLOWSTONE) }
            define('D') { items { +RagiumItems.BASIC_CIRCUIT } }
            result { +RagiumItems.ADVANCED_CIRCUIT }
        }.save(exporter)
        // Advanced
        RagiumRecipeBuilders.assembling {
            ingredient {
                items { +RagiumItems.ADVANCED_CIRCUIT_BOARD }
                count = 2
            }
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.GLOWSTONE)
                count = 2
            }
            result {
                +RagiumItems.ADVANCED_CIRCUIT
                count = 2
            }
        }.save(exporter)
        // Elite
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.ELITE_CIRCUIT_BOARD } }
            ingredient { items { +RagiumItems.SILICON_WAFER } }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.DIAMOND) }
            result { +RagiumItems.ELITE_CIRCUIT }
        }.save(exporter)
    }

    private fun silicon() {
        // SiO2 + C -> Si + CO2
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            ingredient {
                +materialTags(
                    nelOf(
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.COAL_COKE),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.PITCH_COKE),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Chemical.CARBON),
                        RagiumTags.Items.COKES,
                    ),
                )
            }
            result { +RagiumItems.CRUDE_SILICON }
        }.save(exporter)
        // Crude Si + HCl -> Si Dust
        RagiumRecipeBuilders.reacting {
            itemIngredient { +holderSet(HTCommonTags.Items.SILICON) }
            fluidIngredient {
                +holderSet(RagiumFluids.HYDROCHLORIC_ACID)
                amount = 125
            }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICON) }
            fluidResult {
                +RagiumFluids.HYDROCHLORIC_ACID
                amount = 100
            }
        }.save(exporter)
        // Si Dust + Redstone -> Silicon Boule
        RagiumRecipeBuilders.alloying {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICON)
                count = 8
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.RAGINITE) }
            result { +RagiumItems.SILICON_BOULE }
            time *= 16
        }.save(exporter)
        // Silicon Boule -> Silicon Wafer
        RagiumRecipeBuilders.cutting {
            ingredient { items { +RagiumItems.SILICON_BOULE } }
            result {
                +RagiumItems.SILICON_WAFER
                count = 12
            }
        }.save(exporter)
    }

    private fun diamond() {
        // Carbon -> Diamond Wafer
        // TODO: CVD法
        RagiumRecipeBuilders.engraving {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.CARBON)
                count = 8
            }
            catalyst { items { +RagiumItems.DIAMOND_LENS } }
            result { +RagiumItems.DIAMOND_WAFER }
        }.save(exporter)
    }
}
