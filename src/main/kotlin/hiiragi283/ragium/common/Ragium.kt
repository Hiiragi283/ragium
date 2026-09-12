package hiiragi283.ragium.common

import hiiragi283.lib.HTConstants
import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.gui.sync.HTFluidSyncPayload
import hiiragi283.lib.gui.sync.HTIntSyncPayload
import hiiragi283.lib.gui.sync.HTItemSyncPayload
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.item.HTCreativeModeTabHelper
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.display.HTPotionSlotDisplay
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.recipe.HTOreSlurryData
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.data.recipe.RagiumOreSlurryData
import hiiragi283.ragium.common.effect.RagiumMobEffects
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.ragium.common.gui.widget.RagiumWidgetTypes
import hiiragi283.ragium.common.item.HTOreSlurryBucketItem
import hiiragi283.ragium.common.item.HTPotionBucketItem
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.alchemy.RagiumPotions
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.network.HTUpdateMenuPacket
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::register)
        eventBus.addListener(::modifyDefaultComponents)

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
                HTCreativeModeTabHelper.createSimpleTab(
                    RagiumTranslation.RAGIUM,
                    Items.RED_DYE
                ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
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
            helper.register(RagiumAPI.id("ore_slurry_data"), RagiumDataComponents.ORE_SLURRY_DATA)
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

    private fun modifyDefaultComponents(event: ModifyDefaultComponentsEvent) {
        // Block
        setOf(
            RagiumMaterial.Metal.BLACK_STEEL to Rarity.UNCOMMON,
            RagiumMaterial.Metal.VOID_METAL to Rarity.RARE
        ).forEach { (material: RagiumMaterial, rarity: Rarity) ->
            for (part: HTBlockPart in HTBlockPart.entries) {
                val block: ItemLike = RagiumBlocks.MATERIAL_BLOCKS[part, material] ?: continue
                event.modify(block) { builder: DataComponentMap.Builder, _, _ ->
                    builder.set(DataComponents.RARITY, rarity)
                }
            }
        }
        event.modify(RagiumBlocks.CREATIVE_BATTERY) { builder: DataComponentMap.Builder, _, _ ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
        // Item
        event.modify(Items.RAW_COPPER) { builder, provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.COPPER))
        }
        event.modify(Items.RAW_IRON) { builder, provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.IRON))
        }
        event.modify(Items.RAW_GOLD) { builder, provider, _ ->
            builder.set(RagiumDataComponents.ORE_SLURRY_DATA, provider.getOrThrow(RagiumOreSlurryData.GOLD))
        }
    }

    override fun registerDynamicRegistries(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(RagiumRegistries.Keys.ORE_SLURRY_DATA, HTOreSlurryData.CODEC, HTOreSlurryData.CODEC)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        RagiumCommon.initialize(event)
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        registerBlockEntities(helper)

        helper.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            RagiumFluids.POTION.bucketHolder
        )
        helper.registerItem(
            HTFluidCapabilities.item,
            { _, access: ItemAccess -> HTOreSlurryBucketItem.BucketHandler(access) },
            RagiumFluids.ORE_SLURRY.bucketHolder
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
        registerProcessor(RagiumBlockEntityTypes.ASSEMBLER.get())
        registerProcessor(RagiumBlockEntityTypes.CRUSHER.get())
        registerProcessor(RagiumBlockEntityTypes.COMPRESSOR.get())
        registerProcessor(RagiumBlockEntityTypes.CUTTING_MACHINE.get())

        registerProcessor(RagiumBlockEntityTypes.FREEZER.get())
        registerProcessor(RagiumBlockEntityTypes.MELTER.get())
        registerProcessor(RagiumBlockEntityTypes.SMELTER.get())

        registerProcessor(RagiumBlockEntityTypes.CHEMICAL_BATH.get())

        registerProcessor(RagiumBlockEntityTypes.BREWERY.get())
        // Storage
        helper.registerBlockEntity(
            HTEnergyCapabilities.block,
            RagiumBlockEntityTypes.CREATIVE_BATTERY.get()
        ) { _, _ -> InfiniteEnergyHandler.INSTANCE }
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
