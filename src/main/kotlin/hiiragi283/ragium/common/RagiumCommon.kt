package hiiragi283.ragium.common

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.collection.asSequence
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.getOrPut
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.registry.getOrNull
import hiiragi283.lib.resource.modifyPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.recipe.RTLingeringBrewingRecipe
import hiiragi283.ragium.common.recipe.RTSplashBrewingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.fluids.FluidInteractionRegistry
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

internal data object RagiumCommon {
    fun initialize(event: FMLCommonSetupEvent) {
        event.enqueueWork(::initFluidInteractions)
        event.enqueueWork(::initRecipeLookups)

        event.enqueueWork {
            HTPotionFluidManager.register(RagiumFluids.POTION.getOrThrow(), HTPotionFluidManager.Handler.DEFAULT)
        }
    }

    private fun initFluidInteractions() {
        // Convert lava source/flow into concrete by dye liquid
        // FlowingFluid#isRandomlyTicking を true に
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            FluidInteractionRegistry.addInteraction(
                NeoForgeMod.LAVA_TYPE.value(),
                FluidInteractionRegistry.InteractionInformation(
                    RagiumFluids.DYES[color].getFluidType()
                ) { state: FluidState ->
                    when (state.isSource) {
                        true -> Blocks.OBSIDIAN.defaultBlockState()
                        false -> VanillaColoredCollections.CONCRETE[color].block.defaultState
                    }
                }
            )
        }
    }

    private fun initRecipeLookups() {
        // from recipe types
        RagiumRecipeLookups.ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        RagiumRecipeLookups.COMPRESSING.fromRecipeType(RagiumRecipeTypes.COMPRESSING, identity())
        RagiumRecipeLookups.CRUSHING.fromRecipeType(RagiumRecipeTypes.CRUSHING, identity())
        RagiumRecipeLookups.CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())
        RagiumRecipeLookups.DRAINING.fromRecipeType(RagiumRecipeTypes.DRAINING, identity())
        RagiumRecipeLookups.FILLING.fromRecipeType(RagiumRecipeTypes.FILLING, identity())

        RagiumRecipeLookups.ALLOYING.fromRecipeType(RagiumRecipeTypes.ALLOYING, identity())
        RagiumRecipeLookups.FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        RagiumRecipeLookups.MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
        RagiumRecipeLookups.PYROLYZING.fromRecipeType(RagiumRecipeTypes.PYROLYZING, identity())

        RagiumRecipeLookups.BATHING.fromRecipeType(RagiumRecipeTypes.BATHING, identity())
        RagiumRecipeLookups.MIXING.fromRecipeType(RagiumRecipeTypes.MIXING, identity())
        RagiumRecipeLookups.REACTING.fromRecipeType(RagiumRecipeTypes.REACTING, identity())

        RagiumRecipeLookups.BREWING.fromRecipeType(RagiumRecipeTypes.BREWING, identity())
        RagiumRecipeLookups.PLANTING.fromRecipeType(RagiumRecipeTypes.PLANTING, identity())
        // runtime recipes
        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val oxygen: HolderSet.Named<Fluid> =
                registries.getOrNull(RagiumFluids.OXYGEN.fluidTag) ?: return@addSubLookup sequenceOf()
            BuiltInRegistries.BLOCK
                .getDataMap(NeoForgeDataMaps.OXIDIZABLES)
                .asSequence()
                .map { (key: ResourceKey<Block>, value: Oxidizable) ->
                    RagiumRecipeBuilders.bathing {
                        itemIngredient { items { +BuiltInRegistries.BLOCK.getValueOrThrow(key).asItem() } }
                        fluidIngredient {
                            +oxygen
                            amount = 250
                        }
                        result { +value.nextOxidationStage().asItem() }
                        recipeId prefix "oxidization/"
                    }.buildSynthetic()
                }
        }
        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val hydrogen: HolderSet.Named<Fluid> =
                registries.getOrNull(RagiumFluids.HYDROGEN.fluidTag) ?: return@addSubLookup sequenceOf()
            BuiltInRegistries.BLOCK
                .getDataMap(NeoForgeDataMaps.OXIDIZABLES)
                .asSequence()
                .map { (key: ResourceKey<Block>, value: Oxidizable) ->
                    RagiumRecipeBuilders.bathing {
                        itemIngredient { items { +value.nextOxidationStage().asItem() } }
                        fluidIngredient {
                            +hydrogen
                            amount = 250
                        }
                        result { +BuiltInRegistries.BLOCK.getValueOrThrow(key).asItem() }
                        recipeId prefix "reduction/"
                    }.buildSynthetic()
                }
        }
        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val antiRustOil: HolderSet.Named<Fluid> =
                registries.getOrNull(RagiumFluids.ANTI_RUST_OIL.fluidTag) ?: return@addSubLookup sequenceOf()
            BuiltInRegistries.BLOCK
                .getDataMap(NeoForgeDataMaps.WAXABLES)
                .asSequence()
                .map { (key: ResourceKey<Block>, value: Waxable) ->
                    RagiumRecipeBuilders.bathing {
                        itemIngredient { items { +BuiltInRegistries.BLOCK.getValueOrThrow(key).asItem() } }
                        fluidIngredient {
                            +antiRustOil
                            amount = 125
                        }
                        result { +value.waxed().asItem() }
                        recipeId prefix "rustproof/"
                    }.buildSynthetic()
                }
        }

        RagiumRecipeLookups.BREWING.addSubLookup { _ ->
            val mixes: List<PotionBrewing.Mix<Potion>> = HTPhysicalSideHelper.getPotionBrewing()
                ?.let(PotionBrewing::potionMixes)
                ?: return@addSubLookup sequenceOf()
            // PotionBrewingから醸造レシピを集める
            buildTable {
                for (mix: PotionBrewing.Mix<Potion> in mixes) {
                    val potionFrom: Holder<Potion> = mix.from().delegate
                    val ingredient: Ingredient = mix.ingredient()
                    val potionTo: Holder<Potion> = mix.to().delegate
                    this.getOrPut(potionFrom, potionTo, ::ObjectArrayList) += ingredient
                }
            }
                .asSequence()
                .map { (potionFrom: Holder<Potion>, potionTo: Holder<Potion>, ingredients: List<Ingredient>) ->
                    // 集めた醸造レシピから RTBrewingRecipe に変換する
                    RagiumRecipeBuilders.brewing {
                        itemIngredient { +CompoundIngredient(ingredients) }
                        fluidIngredient { +HTPotionFluidIngredient(potionFrom) }
                        result { +potionTo }
                        recipeId replace potionTo.getKeyOrThrow()
                            .identifier()
                            .modifyPath { "${it}_from_${potionFrom.getKeyOrThrow().identifier().toDebugFileName()}" }
                    }.build()
                }
        }
        RagiumRecipeLookups.BREWING.addRecipes(
            RecipeKey(vanillaId("/${RagiumConstants.BREWING}/splash_potion")) to RTSplashBrewingRecipe,
            RecipeKey(vanillaId("/${RagiumConstants.BREWING}/lingering_potion")) to RTLingeringBrewingRecipe
        )
    }
}
