package hiiragi283.ragium.common

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.collection.asSequence
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.getOrPut
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.registry.getOrNull
import hiiragi283.lib.resource.modifyPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.chemical.HTChemical
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.recipe.RTOreSlurryWashingRecipe
import hiiragi283.ragium.common.recipe.RTOreSolvingRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleDrainingRecipe
import hiiragi283.ragium.common.recipe.RTPotionBottleFillingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.DyeColor
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
        event.enqueueWork(::initFluidComponents)
        event.enqueueWork(::initRecipeLookups)
    }

    private fun initFluidInteractions() {
        // Convert lava source/flow into concrete by dye liquid
        // FlowingFluid#isRandomlyTicking を true に
        for (color: DyeColor in DyeColor.entries) {
            FluidInteractionRegistry.addInteraction(
                NeoForgeMod.LAVA_TYPE.value(),
                FluidInteractionRegistry.InteractionInformation(
                    RagiumFluids.DYES[color].getFluidType()
                ) { state: FluidState ->
                    when (state.isSource) {
                        true -> Blocks.OBSIDIAN.defaultBlockState()
                        false -> VanillaColoredCollections.CONCRETE[color].defaultState
                    }
                }
            )
        }
    }

    private fun initFluidComponents() {
        fun setChemical(content: HTFluidContent, key: ResourceKey<HTChemical>) {
            BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(
                content.keyOrThrow
            ) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
                builder.set(RagiumDataComponents.CHEMICAL, provider.getOrThrow(key))
            }
        }

        BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(
            Registries.FLUID.createKey(vanillaId("water"))
        ) { builder: DataComponentMap.Builder, provider: HolderLookup.Provider, _ ->
            builder.set(RagiumDataComponents.CHEMICAL, provider.getOrThrow(RagiumChemicals.WATER))
        }

        setChemical(RagiumFluids.MOLTEN_GLASS, RagiumChemicals.SILICON_DIOXIDE)

        setChemical(RagiumFluids.HYDROGEN, RagiumChemicals.HYDROGEN)
        setChemical(RagiumFluids.OXYGEN, RagiumChemicals.OXYGEN)
        setChemical(RagiumFluids.CHLORINE, RagiumChemicals.CHLORINE)

        setChemical(RagiumFluids.NITRIC_ACID, RagiumChemicals.NITRIC_ACID)
        setChemical(RagiumFluids.HYDROGEN_FLUORIDE, RagiumChemicals.HYDROGEN_FLUORIDE)
        setChemical(RagiumFluids.HYDROFLUORIC_ACID, RagiumChemicals.HYDROGEN_FLUORIDE)

        setChemical(RagiumFluids.SALT_WATER, RagiumChemicals.SODIUM_CHLORIDE)
        setChemical(RagiumFluids.NAOH_SOLUTION, RagiumChemicals.SODIUM_HYDROXIDE)
        setChemical(RagiumFluids.ALUMINA_SOLUTION, RagiumChemicals.ALUMINA)
        setChemical(RagiumFluids.SULFUR_DIOXIDE, RagiumChemicals.SULFUR_DIOXIDE)
        setChemical(RagiumFluids.SULFUR_TRIOXIDE, RagiumChemicals.SULFUR_TRIOXIDE)
        setChemical(RagiumFluids.SULFURIC_ACID, RagiumChemicals.SULFURIC_ACID)
        setChemical(RagiumFluids.HYDROGEN_CHLORIDE, RagiumChemicals.HYDROGEN_CHLORIDE)
        setChemical(RagiumFluids.HYDROCHLORIC_ACID, RagiumChemicals.HYDROGEN_CHLORIDE)
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
        RagiumRecipeLookups.CENTRIFUGING.fromRecipeType(RagiumRecipeTypes.CENTRIFUGING, identity())
        RagiumRecipeLookups.MIXING.fromRecipeType(RagiumRecipeTypes.MIXING, identity())
        RagiumRecipeLookups.REACTING.fromRecipeType(RagiumRecipeTypes.REACTING, identity())

        RagiumRecipeLookups.BREWING.fromRecipeType(RagiumRecipeTypes.BREWING, identity())
        RagiumRecipeLookups.PLANTING.fromRecipeType(RagiumRecipeTypes.PLANTING, identity())
        // runtime recipes
        RagiumRecipeLookups.DRAINING.addRecipes(
            HTBottleType.entries.associate { bottleType: HTBottleType ->
                Pair(
                    RecipeKey(RagiumAPI.id("/${RagiumConstants.DRAINING}/potion_bottle/${bottleType.serializedName}")),
                    RTPotionBottleDrainingRecipe(bottleType)
                )
            }
        )
        RagiumRecipeLookups.FILLING.addRecipes(
            HTBottleType.entries.associate { bottleType: HTBottleType ->
                Pair(
                    RecipeKey(RagiumAPI.id("/${RagiumConstants.FILLING}/potion_bottle/${bottleType.serializedName}")),
                    RTPotionBottleFillingRecipe(bottleType)
                )
            }
        )

        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val oxygen: HolderSet<Fluid> = registries.getOrNull(RagiumFluids.OXYGEN) ?: return@addSubLookup sequenceOf()
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
            val hydrogen: HolderSet<Fluid> =
                registries.getOrNull(RagiumFluids.HYDROGEN) ?: return@addSubLookup sequenceOf()
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
            val antiRustOil: HolderSet<Fluid> =
                registries.getOrNull(RagiumFluids.ANTI_RUST_OIL) ?: return@addSubLookup sequenceOf()
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

        RagiumRecipeLookups.MIXING.addRecipes(
            RecipeKey(RagiumAPI.id("/${RagiumConstants.MIXING}", "ore_solvation")) to RTOreSolvingRecipe
        )
        RagiumRecipeLookups.REACTING.addRecipes(
            RecipeKey(RagiumAPI.id("/${RagiumConstants.REACTING}", "ore_slurry_washing")) to RTOreSlurryWashingRecipe
        )
    }

    private fun HolderGetter.Provider.getOrNull(content: HTFluidContent): HolderSet<Fluid>? =
        this.getOrNull(content.fluidTag)
}
