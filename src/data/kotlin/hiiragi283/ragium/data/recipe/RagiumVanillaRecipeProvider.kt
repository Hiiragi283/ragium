package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.BlockItemTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

data object RagiumVanillaRecipeProvider : HTRecipeProvider(RagiumAPI.MOD_ID) {
    override fun register() {
        vanilla()
        // decoration()
        machine()
        // storage()
        // upgrade()
        // material()
    }

    //    Vanilla    //

    private fun vanilla() {
        // Prismarine Bricks -> 9x Prismarine Shard
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +Items.PRISMARINE_BRICKS } }

            result {
                +Items.PRISMARINE_SHARD
                count = 9
            }
            category = RecipeCategory.BUILDING_BLOCKS
            group = "prismarine_shard"
            recipeId suffix "_from_bricks"
        }.save(context)
        // Gunpowder
        VanillaRecipeBuilders.shapeless {
            ingredient {
                +setOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
                    .map(CommonTagPrefixes.DUST::itemTagKey)
                    .let(::holderSet)
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            result {
                +Items.GUNPOWDER
                count = 3
            }
        }.save(context)
        // Blaze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.MAGMA_BLOCK } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            result { +Items.BLAZE_ROD }
        }.save(context)
        // Breeze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.ICE } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            result { +Items.BREEZE_ROD }
        }.save(context)
        // Candle
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(Tags.Items.STRINGS) }
            define('B') { items { +RagiumItems.BEESWAX } }
            result { +Items.CANDLE }
            category = RecipeCategory.DECORATIONS
        }.save(context)
        // Torch
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { items { +RagiumItems.TAR } }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 3
            }
            recipeId suffix "_from_tar"
        }.save(context)
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(HTCommonTags.Items.PITCH_COKE) }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 6
            }
            recipeId suffix "_from_pitch_coke"
        }.save(context)
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(HTCommonTags.Items.COAL_COKE) }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 8
            }
            recipeId suffix "_from_coal_coke"
        }.save(context)
        // Fire Charge
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            ingredient { items { +Items.BLAZE_POWDER } }
            ingredient { +holderSet(HTCommonTags.Items.PITCH_COKE) }
            result {
                +Items.FIRE_CHARGE
                count = 4
            }
            recipeId suffix "_from_pitch_coke"
        }.save(context)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            ingredient { items { +Items.BLAZE_POWDER } }
            ingredient { +holderSet(HTCommonTags.Items.COAL_COKE) }
            result {
                +Items.FIRE_CHARGE
                count = 6
            }
            recipeId suffix "_from_coal_coke"
        }.save(context)
        // Hopper
        VanillaRecipeBuilders.shaped {
            +"A A"
            +"ABA"
            +" A "
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +itemSet(Tags.BlockItems.CHESTS_WOODEN) }
            result {
                +Items.HOPPER
                count = 2
            }
            recipeId suffix "_by_sooty_iron"
        }.save(context)
        VanillaRecipeBuilders.shaped {
            +"A A"
            +"ABA"
            +" A "
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            define('B') { +itemSet(Tags.BlockItems.CHESTS_WOODEN) }
            result {
                +Items.HOPPER
                count = 4
            }
            recipeId suffix "_by_black_steel"
        }.save(context)
    }

    //    Machine    //

    private fun machine() {
        // Mechanical
        VanillaRecipeBuilders.shaped {
            layered2()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result { +RagiumItems.getParts(HTMachineType.MECHANICAL) }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
                count = 2
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result { +RagiumItems.getParts(HTMachineType.MECHANICAL) }
        }.save(context)

        mechanical(RagiumBlocks.ASSEMBLER) { items { +Items.CRAFTER } }
        mechanical(RagiumBlocks.CENTRIFUGE) { +itemSet(BlockItemTags.BARS) }
        mechanical(RagiumBlocks.CRUSHER) { items { +Items.GRINDSTONE } }
        mechanical(RagiumBlocks.COMPRESSOR) { +holderSet(ItemTags.ANVIL) }
        mechanical(RagiumBlocks.CUTTING_MACHINE) { items { +Items.STONECUTTER } }
        mechanical(RagiumBlocks.PRESS_MACHINE) { items { +Items.PISTON } }
        // Heat
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.CONSTANTAN) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result {
                +RagiumItems.HEATING_COIL
                count = 3
            }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.CONSTANTAN)
                count = 4
            }
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result {
                +RagiumItems.HEATING_COIL
                count = 6
            }
        }.save(context)

        VanillaRecipeBuilders.shaped {
            layered2()
            define('A') { items { +RagiumItems.HEATING_COIL } }
            define('B') { items { +RagiumItems.getParts(HTMachineType.MECHANICAL) } }
            result { +RagiumItems.getParts(HTMachineType.HEAT) }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.getParts(HTMachineType.MECHANICAL) } }
            ingredient {
                items { +RagiumItems.HEATING_COIL }
                count = 2
            }
            result { +RagiumItems.getParts(HTMachineType.HEAT) }
        }.save(context)

        heat(RagiumBlocks.ALLOY_SMELTER) { items { +Items.BLAST_FURNACE } }
        heat(RagiumBlocks.MELTER) { +holderSet(Tags.Items.BUCKETS_LAVA) }

        heat(RagiumBlocks.SMELTER) { items { +Items.FURNACE } }
        // Chemical
        RagiumRecipeBuilders.alloying {
            ingredient { items { +RagiumItems.getParts(HTMachineType.HEAT) } }
            ingredient { +dustOrIngot(RagiumMaterial.Metal.GOLD) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.GLOWSTONE) }
            result { +RagiumItems.getParts(HTMachineType.CHEMICAL) }
        }.save(context)

        chemical(RagiumBlocks.CHEMICAL_BATH) { items { +Items.CAULDRON } }
        chemical(RagiumBlocks.MIXER) { +holderSet(Tags.Items.BUCKETS_EMPTY) }
        // Resource
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +RagiumItems.getParts(HTMachineType.CHEMICAL) } }
            fluidIngredient {
                +holderSet(RagiumFluids.MOLTEN_PLASTIC)
                amount = 90 * 2
            }
            result { +RagiumItems.getParts(HTMachineType.RESOURCE) }
        }.save(context)

        resource(RagiumBlocks.BREWERY) { items { +Items.BREWING_STAND } }
        resource(RagiumBlocks.PLANTER) { items { +Items.FLOWER_POT } }
        // Electronics
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.getParts(HTMachineType.RESOURCE) } }
            ingredient {
                items { +RagiumItems.ELECTRIC_CIRCUIT }
                count = 2
            }
            result { +RagiumItems.getParts(HTMachineType.ELECTRONICS) }
        }.save(context)

        electronics(RagiumBlocks.BLENDER) { items { +RagiumBlocks.MIXER } }
        electronics(RagiumBlocks.ELECTROLYZER) { +itemSet(BlockItemTags.LIGHTNING_RODS) }
        // Arcane
        arcane(RagiumBlocks.ENCHANTER) { items { +Items.ENCHANTING_TABLE } }
        // Other
        VanillaRecipeBuilders.shaped {
            cross8()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            define('B') { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Metal.COPPER) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON) }
            result { +RagiumBlocks.FREEZER }
        }.save(context)

        // Decoration
        VanillaRecipeBuilders.shaped {
            +"ABA"
            +"B B"
            +"ABA"
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result {
                +RagiumBlocks.MACHINE_CASING
                count = 4
            }
        }.save(context)

        for (block: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINE_CASINGS.values) {
            VanillaRecipeBuilders.stonecutting {
                ingredient { items { +RagiumBlocks.MACHINE_CASING } }
                result { +block }
            }.save(context)
        }
    }

    private inline fun machine(
        machineType: HTMachineType,
        material: HTMaterialLike,
        gear: HTMaterialLike,
        result: HTSimpleDeferredBlockAndItem,
        builderAction: IngredientBuilder.() -> Unit
    ) {
        VanillaRecipeBuilders.shaped {
            +"ABA"
            +"BCB"
            +"ADA"
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, material) }
            define('B') { items { +RagiumItems.getParts(machineType) } }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, gear) }
            define('D', builderAction)
            result { +result }
        }.save(context)
    }

    private inline fun mechanical(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.MECHANICAL,
            RagiumMaterial.Alloy.SOOTY_IRON,
            RagiumMaterial.Metal.COPPER,
            result,
            builderAction
        )
    }

    private inline fun heat(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.HEAT,
            RagiumMaterial.Alloy.SOOTY_IRON,
            RagiumMaterial.Metal.IRON,
            result,
            builderAction
        )
    }

    private inline fun chemical(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.CHEMICAL,
            RagiumMaterial.Alloy.BLACK_STEEL,
            RagiumMaterial.Metal.GOLD,
            result,
            builderAction
        )
    }

    private inline fun resource(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.RESOURCE,
            RagiumMaterial.Alloy.BLACK_STEEL,
            RagiumMaterial.Gem.EMERALD,
            result,
            builderAction
        )
    }

    private inline fun electronics(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.ELECTRONICS,
            RagiumMaterial.Alloy.BLACK_STEEL,
            RagiumMaterial.Gem.DIAMOND,
            result,
            builderAction
        )
    }

    private inline fun arcane(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.ARCANE,
            RagiumMaterial.Alloy.SHADOW_STEEL,
            RagiumMaterial.Alloy.NETHERITE,
            result,
            builderAction
        )
    }
}
