package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.block.RagiumBlocks
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class RagiumHeatRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        freezing()
        melting()
        pyrolyzing()
    }

    private fun freezing() {
        // Water -> Snowball
        RagiumRecipeBuilders.freezing {
            catalyst { items { +RagiumItems.BALL_SHAPE_PATTERN } }
            fluidIngredient { +waterSet() }
            result {
                +Items.SNOWBALL
                count = 4
            }
        }.save(exporter)
        // Water -> Ice
        RagiumRecipeBuilders.freezing {
            catalyst { items { +RagiumItems.BLOCK_SHAPE_PATTERN } }
            fluidIngredient { +waterSet() }
            result { +Items.ICE }
        }.save(exporter)

        // Lava -> Obsidian
        RagiumRecipeBuilders.freezing {
            catalyst { items { +RagiumItems.BLOCK_SHAPE_PATTERN } }
            fluidIngredient { +lavaSet() }
            result { +Items.OBSIDIAN }
        }.save(exporter)

        // Honey
        RagiumRecipeBuilders.freezing {
            catalyst { items { +RagiumItems.BLOCK_SHAPE_PATTERN } }
            fluidIngredient { +holderSet(RagiumFluids.HONEY) }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Glass
        RagiumRecipeBuilders.freezing {
            catalyst { items { +RagiumItems.BLOCK_SHAPE_PATTERN } }
            fluidIngredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            result { +Items.GLASS }
        }.save(exporter)
        RagiumRecipeBuilders.freezing {
            catalyst { +holderSet(ItemTags.BARS) }
            fluidIngredient {
                +holderSet(RagiumFluids.MOLTEN_GLASS)
                amount = 375
            }
            result { +Items.GLASS_PANE }
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
        (OminousBottleAmplifier.MIN_AMPLIFIER..OminousBottleAmplifier.MAX_AMPLIFIER)
            .forEach { amplifier: Int ->
                RagiumRecipeBuilders.melting {
                    ingredient {
                        +DataComponentIngredient.of(
                            false,
                            DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
                            OminousBottleAmplifier(amplifier),
                            Items.OMINOUS_BOTTLE,
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
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
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
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Mineral.REDSTONE) }
            result {
                +RagiumFluids.MOLTEN_REDSTONE
                amount = 90
            }
        }.save(exporter)
        // Molten Glowstone
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Mineral.GLOWSTONE) }
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
        // Molten Blaze
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.RODS_BLAZE) }
            result {
                +RagiumFluids.MOLTEN_BLAZE
                amount = 180
            }
            recipeId suffix "_from_rod"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.BLAZE_POWDER } }
            result {
                +RagiumFluids.MOLTEN_BLAZE
                amount = 90
            }
            recipeId suffix "_from_powder"
        }.save(exporter)
    }

    private fun pyrolyzing() {
        // Log -> Charcoal + Creosote
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(ItemTags.LOGS_THAT_BURN) }
            itemResult { +Items.CHARCOAL }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 250
            }
            time /= 2
            recipeId suffix "_from_logs"
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Other.WOOD) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Fuel.CHARCOAL) }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 250
            }
            time /= 2
        }.save(exporter)
        // Coal -> Coal Coke + Creosote
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { items { +Items.COAL } }
            itemResult { +RagiumItems.COAL_COKE }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.DUST, HTMaterial.Fuel.COAL) }
            itemResult { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Fuel.COAL_COKE) }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500
            }
            time /= 2
        }.save(exporter)
        RagiumRecipeBuilders.pyrolyzing {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, HTMaterial.Fuel.COAL) }
            itemResult { +RagiumBlocks.getOrThrow(HTBlockPart.STORAGE_BLOCK, HTMaterial.Fuel.COAL_COKE) }
            fluidResult {
                +RagiumFluids.CREOSOTE
                amount = 500 * 9
            }
            time /= 2
            time *= 9
        }.save(exporter)
    }

    override fun getName(): String = "Heat Recipes"
}
