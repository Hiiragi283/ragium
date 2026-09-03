package hiiragi283.ragium.common

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.gui.sync.HTFluidSyncPayload
import hiiragi283.lib.gui.sync.HTIntSyncPayload
import hiiragi283.lib.gui.sync.HTItemSyncPayload
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.item.HTCreativeModeTabHelper
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.display.HTPotionSlotDisplay
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.getOrNull
import hiiragi283.lib.resource.modifyPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.effect.RagiumMobEffects
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.ragium.common.gui.widget.RagiumWidgetTypes
import hiiragi283.ragium.common.item.HTPotionBucketItem
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import hiiragi283.ragium.common.recipe.RTLingeringBrewingRecipe
import hiiragi283.ragium.common.recipe.RTSplashBrewingRecipe
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.transfer.access.ItemAccess

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::register)

        RagiumFluids.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumBlockEntityTypes.register(eventBus)
        RagiumMobEffects.register(eventBus)
        RagiumPotions.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.SERVER, RagiumConfig.SERVER_SPEC)
    }

    private fun register(event: RegisterEvent) {
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                RagiumAPI.id("common"),
                HTCreativeModeTabHelper.createSimpleTab(RagiumTranslation.RAGIUM, Items.RED_DYE) {
                        parameters: CreativeModeTab.ItemDisplayParameters,
                        output: CreativeModeTab.Output
                    ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumItems.REGISTER.asSequence())
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        items = RagiumBlocks.REGISTER.asItemSequence()
                    )
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        items = RagiumFluids.REGISTER.asItemSequence()
                    )
                }
            )
        }
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(RagiumAPI.id(HTConstants.BOTTLE_TYPE), RagiumDataComponents.BOTTLE_TYPE)
            helper.register(RagiumAPI.id(HTConstants.ENERGY), RagiumDataComponents.ENERGY)
            helper.register(RagiumAPI.id(HTConstants.FLUID), RagiumDataComponents.FLUID)
            helper.register(RagiumAPI.id("memory_disc_data"), RagiumDataComponents.MEMORY_DISC_DATA)
        }
        event.register(Registries.MENU) { helper ->
            helper.register(
                HTBlockWidgetHolderContext.MENU_TYPE.id,
                IMenuTypeExtension.create(HTBlockWidgetHolderContext::create)
            )
            // helper.register(HTItemWidgetHolderContext.MENU_TYPE.getId(), IMenuTypeExtension.create(HTItemWidgetHolderContext::create)) TODO
        }
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            RagiumRecipeSerializers.allSerializers.forEach(helper::register)
        }
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in RagiumRecipeTypes.allTypes) {
                helper.register(recipeType.keyOrThrow, recipeType)
            }
        }
        event.register(Registries.SLOT_DISPLAY) { helper ->
            helper.register(RagiumAPI.id(HTConstants.POTION), HTPotionSlotDisplay.TYPE)
        }

        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(RagiumAPI.id(HTConstants.POTION), HTPotionFluidIngredient.TYPE)
        }

        event.register(RagiumRegistries.Keys.FLUID_RESULT_TYPE) { helper ->
            helper.register(RagiumAPI.id("simple"), HTFluidResult.SimpleEntry.TYPE)
            helper.register(RagiumAPI.id(HTConstants.POTION), HTFluidResult.PotionEntry.TYPE)
        }
        event.register(RagiumRegistries.Keys.ITEM_RESULT_TYPE) { helper ->
            helper.register(RagiumAPI.id("simple"), HTItemResult.SimpleEntry.TYPE)
            helper.register(RagiumAPI.id(HTConstants.TAG), HTItemResult.TagEntry.TYPE)
        }
        event.register(RagiumRegistries.Keys.SYNCABLE_SLOT_TYPE) { helper ->
            helper.register(RagiumAPI.id("integer"), HTIntSyncPayload.TYPE)
            helper.register(RagiumAPI.id(HTConstants.ITEM), HTItemSyncPayload.TYPE)
            helper.register(RagiumAPI.id(HTConstants.FLUID), HTFluidSyncPayload.TYPE)
        }
        event.register(RagiumRegistries.Keys.WIDGET_TYPE) { helper ->
            for (widgetType: HTWidgetType<*> in RagiumWidgetTypes.allTypes) {
                helper.register(widgetType.keyOrThrow, widgetType)
            }
        }
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(::initRecipeLookups)
        event.enqueueWork {
            HTPotionFluidManager.register(RagiumFluids.POTION.getOrThrow(), HTPotionFluidManager.Handler.DEFAULT)
        }
    }

    private fun initRecipeLookups() {
        RagiumRecipeLookups.ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        RagiumRecipeLookups.COMPRESSING.fromRecipeType(RagiumRecipeTypes.COMPRESSING, identity())
        RagiumRecipeLookups.CRUSHING.fromRecipeType(RagiumRecipeTypes.CRUSHING, identity())
        RagiumRecipeLookups.CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())
        RagiumRecipeLookups.DRAINING.fromRecipeType(RagiumRecipeTypes.DRAINING, identity())
        RagiumRecipeLookups.FILLING.fromRecipeType(RagiumRecipeTypes.FILLING, identity())

        RagiumRecipeLookups.FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        RagiumRecipeLookups.MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
        RagiumRecipeLookups.PYROLYZING.fromRecipeType(RagiumRecipeTypes.PYROLYZING, identity())

        RagiumRecipeLookups.BATHING.fromRecipeType(RagiumRecipeTypes.BATHING, identity())
        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val oxygen: HolderSet.Named<Fluid> =
                registries.getOrNull(RagiumFluids.OXYGEN.fluidTag) ?: return@addSubLookup sequenceOf()
            BuiltInRegistries.BLOCK
                .getDataMap(NeoForgeDataMaps.OXIDIZABLES)
                .asSequence()
                .map { (key: ResourceKey<Block>, value: Oxidizable) ->
                    val base: Item = BuiltInRegistries.BLOCK.getValueOrThrow(key).asItem()
                    val oxidized: Item = value.nextOxidationStage().asItem()
                    RagiumRecipeBuilders.bathing {
                        itemIngredient { items { +base } }
                        fluidIngredient {
                            +oxygen
                            amount = 250
                        }
                        result { +oxidized }
                        recipeId prefix "oxidization/"
                    }.build()
                }
        }
        RagiumRecipeLookups.BATHING.addSubLookup { (_, registries: RegistryAccess) ->
            val hydrogen: HolderSet.Named<Fluid> =
                registries.getOrNull(RagiumFluids.HYDROGEN.fluidTag) ?: return@addSubLookup sequenceOf()
            BuiltInRegistries.BLOCK
                .getDataMap(NeoForgeDataMaps.OXIDIZABLES)
                .asSequence()
                .map { (key: ResourceKey<Block>, value: Oxidizable) ->
                    val base: Item = BuiltInRegistries.BLOCK.getValueOrThrow(key).asItem()
                    val oxidized: Item = value.nextOxidationStage().asItem()
                    RagiumRecipeBuilders.bathing {
                        itemIngredient { items { +oxidized } }
                        fluidIngredient {
                            +hydrogen
                            amount = 250
                        }
                        result { +base }
                        recipeId prefix "reduction/"
                    }.build()
                }
        }

        RagiumRecipeLookups.BREWING.fromRecipeType(RagiumRecipeTypes.BREWING, identity())
        RagiumRecipeLookups.BREWING.addSubLookup {
            val multiMap: ListMultiMap<Identifier, RTBrewingRecipe> = buildListMultiMap {
                HTPhysicalSideHelper.getPotionBrewing()
                    ?.let(PotionBrewing::potionMixes)
                    ?.asSequence()
                    ?.forEach { mix: PotionBrewing.Mix<Potion> ->
                        val (_, recipe: RTBrewingRecipe) = RagiumRecipeBuilders.brewing {
                            itemIngredient { +mix.ingredient }
                            fluidIngredient { +HTPotionFluidIngredient(mix.from()) }
                            result { +mix.to() }
                        }.build()
                        val id: Identifier =
                            mix.to().key?.identifier()?.modifyPath { "/${RagiumConstants.BREWING}/$it" }
                                ?: return@forEach
                        put(id, recipe)
                    }
            }
            sequence {
                for ((potionTo: Identifier, recipes: Collection<RTBrewingRecipe>) in multiMap.entries) {
                    recipes.forEachIndexed { index: Int, recipe: RTBrewingRecipe ->
                        yield(RecipeKey(potionTo.withSuffix("_$index")) to recipe)
                    }
                }
                // Custom
                yield(RecipeKey(vanillaId("/${RagiumConstants.BREWING}/splash_potion")) to RTSplashBrewingRecipe)
                yield(RecipeKey(vanillaId("/${RagiumConstants.BREWING}/lingering_potion")) to RTLingeringBrewingRecipe)
            }
        }
        RagiumRecipeLookups.PLANTING.fromRecipeType(RagiumRecipeTypes.PLANTING, identity())
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        registerBlockEntities(helper)

        helper.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            RagiumFluids.POTION.bucketHolder
        )
    }

    private fun registerBlockEntities(helper: CapabilityHelper) {
        fun <BE : HTProcessorBlockEntity.Energized> registerProcessor(type: BlockEntityType<BE>) {
            helper.registerBlockEntity(type)
            helper.registerBlockEntity(HTEnergyCapabilities.block, type) { processor: BE, _ ->
                processor.handler.asForge()
            }
        }

        // Machine
        registerProcessor(RagiumBlockEntityTypes.CRUSHER.get())
        registerProcessor(RagiumBlockEntityTypes.CUTTING_MACHINE.get())

        registerProcessor(RagiumBlockEntityTypes.FREEZER.get())
        registerProcessor(RagiumBlockEntityTypes.MELTER.get())

        registerProcessor(RagiumBlockEntityTypes.BREWERY.get())
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC)
        registrar.playBidirectional(
            HTUpdateMenuPacket.TYPE,
            HTUpdateMenuPacket.STREAM_CODEC,
            HTPayloadHandlers::handleC2S
        )
    }
}
