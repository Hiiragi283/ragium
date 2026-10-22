package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

data object RagiumHeatRecipeProvider : HTRecipeProvider(RagiumAPI.MOD_ID) {
    override fun register() {
        alloying()
        freezing()
        melting()
    }

    private fun alloying() {
        // Gold + Netherite Scrap -> Netherite
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrIngot(RagiumMaterial.Metal.GOLD)
                count = 2
            }
            ingredient {
                items { +Items.NETHERITE_SCRAP }
                count = 2
            }
            result { +Items.NETHERITE_INGOT }
        }.save(context)
        // Clay + Sand -> Brick
        RagiumRecipeBuilders.alloying {
            ingredient { items { +Items.CLAY_BALL } }
            ingredient { +itemSet(Tags.BlockItems.SANDS) }
            result {
                +Items.BRICK
                count = 2
            }
        }.save(context)
        // Netherrack + Soul Sand -> Nether Brick
        RagiumRecipeBuilders.alloying {
            ingredient { +itemSet(Tags.BlockItems.NETHERRACKS) }
            ingredient { +holderSet(ItemTags.SOUL_FIRE_BASE_BLOCKS) }
            result {
                +Items.NETHER_BRICK
                count = 2
            }
        }.save(context)

        // Copper + Nickel -> Constantan
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.COPPER) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Alloy.CONSTANTAN)
                count = 2
            }
        }.save(context)
        RagiumRecipeBuilders.alloying {
            ingredient { +dustOrIngot(RagiumMaterial.Metal.COPPER) }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.NICKEL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.CONSTANTAN)
                count = 2
            }
        }.save(context)

        // Iron + Coals -> Sooty Iron
        RagiumRecipeBuilders.alloying {
            ingredient { +dustOrIngot(RagiumMaterial.Metal.IRON) }
            ingredient {
                +materialTags(
                    nelOf(
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.COAL),
                        CommonTagPrefixes.DUST.itemTagKey(RagiumMaterial.Fuel.CHARCOAL),
                        RagiumTags.Items.COALS
                    )
                )
            }
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            recipeId suffix "_by_coals"
        }.save(context)
        // 2x Iron + Cokes -> 2x Sooty Iron
        RagiumRecipeBuilders.alloying {
            ingredient {
                +dustOrIngot(RagiumMaterial.Metal.IRON)
                count = 2
            }
            ingredient { +holderSet(RagiumTags.Items.COKES) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
                count = 2
            }
            recipeId suffix "_by_cokes"
        }.save(context)
        // Sooty Iron + Nickel + Obsidian Dust -> Black Steel
        RagiumRecipeBuilders.alloying {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.NICKEL) }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.OBSIDIAN)
                count = 2
            }
            result { +RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
        }.save(context)
    }

    private fun freezing() {
        // Water -> Snowball
        RagiumRecipeBuilders.freezing {
            ingredient {
                +waterSet()
                amount /= 4
            }
            catalyst { items { +RagiumItems.BALL_MOLD } }
            result { +Items.SNOWBALL }
        }.save(context)
        // Water -> Snow Block
        RagiumRecipeBuilders.freezing {
            ingredient { +waterSet() }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.SNOW_BLOCK }
        }.save(context)
        // Lava -> Obsidian
        RagiumRecipeBuilders.freezing {
            ingredient { +lavaSet() }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.OBSIDIAN }
        }.save(context)
        // Honey
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.HONEY) }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.HONEY_BLOCK }
        }.save(context)

        // Sticky Ball
        RagiumRecipeBuilders.freezing {
            ingredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 4
            }
            catalyst { items { +RagiumItems.BALL_MOLD } }
            result { +RagiumItems.STICKY_BALL }
        }.save(context)
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
        }.save(context)
        RagiumRecipeBuilders.melting {
            ingredient {
                items {
                    +Items.SNOW_BLOCK
                    +Items.ICE
                }
            }
            result { water() }
        }.save(context)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.PACKED_ICE } }
            result {
                water()
                amount *= 9
            }
            time *= 6
            recipeId suffix "_from_packed_ice"
        }.save(context)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.BLUE_ICE } }
            result {
                water()
                amount *= 81
            }
            time *= 36
            recipeId suffix "_from_blue_ice"
        }.save(context)

        // Stone -> Lava
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(setOf(Tags.BlockItems.STONES, Tags.BlockItems.COBBLESTONES).map(BlockItemTagId::item))
            }
            result {
                lava()
                amount /= 8
            }
            recipeId suffix "_from_stone"
        }
        // Magma block -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.MAGMA_BLOCK } }
            result {
                lava()
                amount /= 2
            }
            recipeId suffix "_from_magma_block"
        }.save(context)

        // Honey
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.HONEY_BLOCK } }
            result { +RagiumFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(context)
        // Resin
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.CLUMPS_RESIN) }
            result {
                +RagiumFluids.RESIN
                amount = 90
            }
            recipeId suffix "_from_clump"
        }.save(context)
        RagiumRecipeBuilders.melting {
            ingredient { +itemSet(Tags.BlockItems.STORAGE_BLOCKS_RESIN) }
            result {
                +RagiumFluids.RESIN
                amount = 90 * 9
            }
            recipeId suffix "_from_block"
        }.save(context)

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
            }.save(context)
        }
        // Molten Redstone
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +RagiumFluids.MOLTEN_REDSTONE
                amount = 90
            }
        }.save(context)
        // Molten Glowstone
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.GLOWSTONE) }
            result {
                +RagiumFluids.MOLTEN_GLOWSTONE
                amount = 90
            }
        }.save(context)
        // Molten Ender
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.ENDER_PEARLS) }
            result {
                +RagiumFluids.MOLTEN_ENDER
                amount = 90
            }
        }.save(context)
    }
}
