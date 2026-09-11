package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import java.util.concurrent.CompletableFuture

class RagiumHeatRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        alloying()
        freezing()
        melting()
    }

    private fun alloying() {
        // Blackstone + Gold -> Gilded Blackstone
        RagiumRecipeBuilders.alloying {
            primary { items { +Items.BLACKSTONE } }
            secondary {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.GILDED_BLACKSTONE }
        }.save(exporter)
        // Gold + Netherite Scrap -> Netherite
        RagiumRecipeBuilders.alloying {
            primary {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.GOLD)
                count = 2
            }
            secondary {
                items { +Items.NETHERITE_SCRAP }
                count = 2
            }
            result { +Items.NETHERITE_INGOT }
        }.save(exporter)
        // Gold Block + Apple -> Enchanted Golden Apple
        RagiumRecipeBuilders.alloying {
            primary { items { +Items.APPLE } }
            secondary {
                +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.ENCHANTED_GOLDEN_APPLE }
            time *= 8
        }.save(exporter)

        // Machine Casing
        RagiumRecipeBuilders.alloying {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.BLACK_STEEL)
                count = 2
            }
            secondary { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.getCasing(HTMachineType.CHEMICAL) }
        }.save(exporter)
        // Sooty Iron + Obsidian Dust -> Black Steel
        RagiumRecipeBuilders.alloying {
            primary { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            secondary {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.OBSIDIAN)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.BLACK_STEEL) }
        }.save(exporter)
    }

    private fun freezing() {
        // Water -> Snow Block
        RagiumRecipeBuilders.freezing {
            ingredient { +waterSet() }
            result { +Items.SNOW_BLOCK }
        }.save(exporter)
        // Lava -> Obsidian
        RagiumRecipeBuilders.freezing {
            ingredient { +lavaSet() }
            result { +Items.OBSIDIAN }
        }.save(exporter)
        // Honey
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.HONEY) }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)
        // Glass
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            result { +Items.GLASS }
        }.save(exporter)
    }

    private fun melting() {
        // Snow / Ice -> Water
        RagiumRecipeBuilders.melting {
            ingredient {
                items { +Items.SNOWBALL }
                count = 4
            }
            result { water() }
            time /= 2
            recipeId suffix "_from_snowball"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                items {
                    +Items.SNOW_BLOCK
                    +Items.ICE
                }
            }
            result { water() }
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.PACKED_ICE } }
            result {
                water()
                amount *= 9
            }
            time *= 6
            recipeId suffix "_from_packed_ice"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.BLUE_ICE } }
            result {
                water()
                amount *= 81
            }
            time *= 36
            recipeId suffix "_from_blue_ice"
        }.save(exporter)

        // Stone -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.STONES, Tags.Items.COBBLESTONES) }
            result {
                lava()
                amount /= 8
            }
            recipeId suffix "_from_stone"
        }.save(exporter)
        // Magma block -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.MAGMA_BLOCK } }
            result {
                lava()
                amount /= 2
            }
            recipeId suffix "_from_magma_block"
        }.save(exporter)

        // Honey
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.HONEY_BLOCK } }
            result { +RagiumFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(exporter)

        // Ominous Bottle -> Ominous Flux
        (OminousBottleAmplifier.MIN_AMPLIFIER..OminousBottleAmplifier.MAX_AMPLIFIER).forEach { amplifier: Int ->
            RagiumRecipeBuilders.melting {
                ingredient {
                    +DataComponentIngredient.of(
                        false,
                        DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
                        OminousBottleAmplifier(amplifier),
                        Items.OMINOUS_BOTTLE
                    )
                }
                result {
                    +RagiumFluids.OMINOUS_FLUX
                    amount = 250 * (amplifier + 1)
                }
                recipeId prefix "${amplifier}x_"
            }.save(exporter)
        }
        // Molten Glass
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(Tags.Items.GLASS_BLOCKS, CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Other.GLASS))
            }
            result { +RagiumFluids.MOLTEN_GLASS }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_PANES) }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount = 375
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount *= 3
            }
            time *= 8
            recipeId suffix "_from_panes"
        }.save(exporter)
        // Molten Redstone
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +RagiumFluids.MOLTEN_REDSTONE
                amount = 90
            }
        }.save(exporter)
        // Molten Glowstone
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.GLOWSTONE) }
            result {
                +RagiumFluids.MOLTEN_GLOWSTONE
                amount = 90
            }
        }.save(exporter)
        // Molten Ender
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.ENDER_PEARLS) }
            result {
                +RagiumFluids.MOLTEN_ENDER
                amount = 90
            }
        }.save(exporter)

        // Sooty Iron -> Molten Steel
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            result {
                +RagiumFluids.MOLTEN_STEEL
                amount = 90
            }
            recipeId suffix "_from_sooty_iron"
        }.save(exporter)
    }

    override fun getName(): String = "Heat Recipes"
}
