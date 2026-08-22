package hiiragi283.ragium

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.sync.HTFluidSyncPayload
import hiiragi283.lib.gui.sync.HTIntSyncPayload
import hiiragi283.lib.gui.sync.HTItemSyncPayload
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.item.HTCreativeModeTabHelper
import hiiragi283.lib.item.alchemy.HTPotionFluidManager
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.network.HTPayloadHandlers
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.block.RagiumBlocks
import hiiragi283.ragium.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.ragium.gui.widget.RagiumWidgetTypes
import hiiragi283.ragium.item.HTPotionBucketItem
import hiiragi283.ragium.item.RagiumItems
import hiiragi283.ragium.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.network.HTUpdateMenuPacket
import hiiragi283.ragium.recipe.RagiumRecipeLookups
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.transfer.access.ItemAccess

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::register)

        RagiumFluids.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumBlockEntityTypes.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.SERVER, RagiumConfig.SERVER_SPEC)
    }

    private fun register(event: RegisterEvent) {
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                RagiumAPI.id("common"),
                HTCreativeModeTabHelper.createSimpleTab(RagiumTranslation.RAGIUM, Items.IRON_INGOT) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumItems.REGISTER.asSequence())
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumBlocks.REGISTER.asItemSequence())
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumFluids.REGISTER.asItemSequence())
                },
            )
        }
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(RagiumAPI.id("bottle_type"), RagiumDataComponents.BOTTLE_TYPE)
            helper.register(RagiumAPI.id(HTConstants.FLUID), RagiumDataComponents.FLUID)
        }
        event.register(Registries.MENU) { helper ->
            helper.register(HTBlockWidgetHolderContext.MENU_TYPE.getId(), IMenuTypeExtension.create(HTBlockWidgetHolderContext::create))
            // helper.register(HTItemWidgetHolderContext.MENU_TYPE.getId(), IMenuTypeExtension.create(HTItemWidgetHolderContext::create)) TODO
        }
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(RagiumAPI.id(RagiumConstants.ASSEMBLING), RagiumRecipeSerializers.ASSEMBLING)
            helper.register(RagiumAPI.id(RagiumConstants.CRUSHING), RagiumRecipeSerializers.CRUSHING)
            helper.register(RagiumAPI.id(RagiumConstants.CUTTING), RagiumRecipeSerializers.CUTTING)

            helper.register(RagiumAPI.id(RagiumConstants.FREEZING), RagiumRecipeSerializers.FREEZING)
            helper.register(RagiumAPI.id(RagiumConstants.MELTING), RagiumRecipeSerializers.MELTING)
            helper.register(RagiumAPI.id(HTConstants.SMELTING), RagiumRecipeSerializers.SMELTING)
            helper.register(RagiumAPI.id(RagiumConstants.PYROLYZING), RagiumRecipeSerializers.PYROLYZING)

            helper.register(RagiumAPI.id(RagiumConstants.BATHING), RagiumRecipeSerializers.BATHING)
            helper.register(RagiumAPI.id(RagiumConstants.ELECTROLYZING), RagiumRecipeSerializers.ELECTROLYZING)

            helper.register(RagiumAPI.id(RagiumConstants.BREWING), RagiumRecipeSerializers.BREWING)
        }
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in RagiumRecipeTypes.allTypes) {
                helper.register(recipeType.getId(), recipeType)
            }
        }

        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(RagiumAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        event.register(RagiumRegistries.Keys.FLUID_RESULT_TYPE) { helper ->
            helper.register(RagiumAPI.id("simple"), HTFluidResult.SimpleEntry.TYPE)
            helper.register(RagiumAPI.id("potion"), HTFluidResult.PotionEntry.TYPE)
        }
        event.register(RagiumRegistries.Keys.ITEM_RESULT_TYPE) { helper ->
            helper.register(RagiumAPI.id("simple"), HTItemResult.SimpleEntry.TYPE)
            helper.register(RagiumAPI.id("tag"), HTItemResult.TagEntry.TYPE)
        }
        event.register(RagiumRegistries.Keys.SYNCABLE_SLOT_TYPE) { helper ->
            helper.register(RagiumAPI.id("integer"), HTIntSyncPayload.TYPE)
            helper.register(RagiumAPI.id(HTConstants.ITEM), HTItemSyncPayload.TYPE)
            helper.register(RagiumAPI.id(HTConstants.FLUID), HTFluidSyncPayload.TYPE)
        }
        event.register(RagiumRegistries.Keys.WIDGET_TYPE) { helper ->
            for (widgetType: HTWidgetType<*> in RagiumWidgetTypes.allTypes) {
                helper.register(widgetType.getId(), widgetType)
            }
        }
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumRecipeLookups::init)
        event.enqueueWork {
            HTPotionFluidManager.register(RagiumFluids.POTION.get(), HTPotionFluidManager.Handler.DEFAULT)
        }
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        registerBlockEntities(helper)

        helper.registerItem(
            Capabilities.Fluid.ITEM,
            { _, access: ItemAccess -> HTPotionBucketItem.BucketHandler(access) },
            RagiumFluids.POTION.bucketHolder,
        )
    }

    private fun registerBlockEntities(helper: CapabilityHelper) {
        fun <BE : HTProcessorBlockEntity.Energized> registerProcessor(type: BlockEntityType<BE>) {
            helper.registerBlockEntity(type)
            helper.registerBlockEntity(Capabilities.Energy.BLOCK, type) { processor: BE, _ -> HTEnergyHandler.Wrapper(processor.handler) }
        }

        // Machine
        registerProcessor(RagiumBlockEntityTypes.MELTER.get())
    }

    override fun registerPayload(registrar: PayloadRegistrar) {
        registrar.playToClient(HTUpdateBlockEntityPacket.TYPE, HTUpdateBlockEntityPacket.STREAM_CODEC)
        registrar.playBidirectional(HTUpdateMenuPacket.TYPE, HTUpdateMenuPacket.STREAM_CODEC, HTPayloadHandlers::handleC2S)
    }
}
