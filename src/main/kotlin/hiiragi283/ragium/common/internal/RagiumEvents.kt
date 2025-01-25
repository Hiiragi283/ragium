package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.common.init.*
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import org.slf4j.Logger
import java.util.function.Supplier

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
internal object RagiumEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun createRegistry(event: NewRegistryEvent) {
        event.register(RagiumAPI.Registries.MULTIBLOCK_COMPONENT_TYPE)
        event.register(RagiumAPI.Registries.RECIPE_CONDITION)

        LOGGER.info("Registered new registries!")
    }

    @SubscribeEvent
    fun addBlockToBlockEntity(event: BlockEntityTypeAddBlocksEvent) {
        fun bindMachine(type: Supplier<out BlockEntityType<*>>, machine: HTMachineKey) {
            val content: HTBlockContent = machine.getBlockOrNull() ?: return
            event.modify(type.get(), content.get())
        }

        fun bindAllMachines(type: Supplier<out BlockEntityType<*>>) {
            HTMachineKey.allKeys.forEach { machine: HTMachineKey -> bindMachine(type, machine) }
        }

        bindAllMachines(RagiumBlockEntityTypes.DEFAULT_GENERATOR)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.COMBUSTION_GENERATOR)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.GAS_TURBINE)
        bindMachine(RagiumBlockEntityTypes.FLUID_GENERATOR, RagiumMachineKeys.THERMAL_GENERATOR)

        bindAllMachines(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        bindAllMachines(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        bindMachine(RagiumBlockEntityTypes.DISTILLATION_TOWER, RagiumMachineKeys.DISTILLATION_TOWER)
        bindMachine(RagiumBlockEntityTypes.MULTI_SMELTER, RagiumMachineKeys.MULTI_SMELTER)

        LOGGER.info("Added external blocks to BlockEntityType!")
    }

    @SubscribeEvent
    fun registerBlockCapabilities(event: RegisterCapabilitiesEvent) {
        fun <T : Any, C> staticProvider(value: T?): IBlockCapabilityProvider<T, C> =
            IBlockCapabilityProvider { _: Level, _: BlockPos, _: BlockState, _: BlockEntity?, _: C? -> value }

        // All Blocks
        fun <T : Any, C> registerForBlocks(capability: BlockCapability<T, C>, provider: IBlockCapabilityProvider<T, C>) {
            for (block: Block in BuiltInRegistries.BLOCK) {
                event.registerBlock(
                    capability,
                    provider,
                    block,
                )
            }
        }

        registerForBlocks(
            RagiumAPI.BlockCapabilities.CONTROLLER_HOLDER,
        ) { _: Level, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Direction -> blockEntity as? HTControllerHolder }

        fun registerTier(contents: Iterable<HTBlockContent>) {
            event.registerBlock(
                RagiumAPI.BlockCapabilities.MACHINE_TIER,
                { _: Level, _: BlockPos, state: BlockState, _: BlockEntity?, _: Void? ->
                    state.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER)
                },
                *contents.map(HTBlockContent::get).toTypedArray(),
            )
        }

        registerTier(RagiumBlocks.Grates.entries)
        registerTier(RagiumBlocks.Casings.entries)
        registerTier(RagiumBlocks.Hulls.entries)
        registerTier(RagiumBlocks.Coils.entries)
        registerTier(RagiumBlocks.Burners.entries)

        registerTier(RagiumBlocks.Drums.entries)

        // from HTBlockEntityHandlerProvider
        fun <T> registerHandlers(supplier: Supplier<BlockEntityType<T>>) where T : BlockEntity, T : HTBlockEntityHandlerProvider {
            val type: BlockEntityType<T> = supplier.get()
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getItemHandler,
            )
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getFluidHandler,
            )
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                HTBlockEntityHandlerProvider::getEnergyStorage,
            )
        }

        registerHandlers(RagiumBlockEntityTypes.MANUAL_GRINDER)

        registerHandlers(RagiumBlockEntityTypes.DEFAULT_GENERATOR)
        registerHandlers(RagiumBlockEntityTypes.FLUID_GENERATOR)

        registerHandlers(RagiumBlockEntityTypes.DEFAULT_PROCESSOR)
        registerHandlers(RagiumBlockEntityTypes.LARGE_PROCESSOR)
        registerHandlers(RagiumBlockEntityTypes.DISTILLATION_TOWER)
        registerHandlers(RagiumBlockEntityTypes.MULTI_SMELTER)

        registerHandlers(RagiumBlockEntityTypes.DRUM)

        // Other
        event.registerBlock(
            Capabilities.EnergyStorage.BLOCK,
            { level: Level, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction ->
                level.getEnergyNetwork().getOrNull()
            },
            RagiumBlocks.ENERGY_NETWORK_INTERFACE.get(),
        )

        LOGGER.info("Registered Block Capabilities!")
    }

    @SubscribeEvent
    fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerItem(
            Capabilities.FluidHandler.ITEM,
            { stack: ItemStack, _: Void? ->
                val tier: HTMachineTier =
                    stack.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: return@registerItem null
                FluidHandlerItemStack.SwapEmpty(
                    RagiumComponentTypes.FLUID_CONTENT,
                    stack,
                    ItemStack(stack.item),
                    tier.tankCapacity,
                )
            },
            *RagiumBlocks.Drums.entries.toTypedArray(),
        )

        LOGGER.info("Registered Item Capabilities!")
    }

    @SubscribeEvent
    fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumAPI.DataMapTypes.MACHINE_KEY)
        event.register(RagiumAPI.DataMapTypes.MACHINE_TIER)
        event.register(RagiumAPI.DataMapTypes.MATERIAL)

        event.register(RagiumAPI.DataMapTypes.TEMP_TIER)

        LOGGER.info("Registered Data Map Types!")
    }

    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : ItemLike> modifyAll(items: Collection<T>, patch: (DataComponentPatch.Builder, T) -> Unit) {
            items.forEach { itemLike: T ->
                event.modify(itemLike.asItem()) { builder: DataComponentPatch.Builder -> patch(builder, itemLike) }
            }
        }

        fun tieredText(translationKey: String): (DataComponentPatch.Builder, HTMachineTierProvider) -> Unit =
            { builder: DataComponentPatch.Builder, provider: HTMachineTierProvider ->
                builder.name(provider.machineTier.createPrefixedText(translationKey))
            }

        val materialText: (DataComponentPatch.Builder, HTMaterialProvider) -> Unit =
            { builder: DataComponentPatch.Builder, provider: HTMaterialProvider ->
                builder.name(provider.prefixedText)
                if (provider.material in RagiumMaterialKeys.END_CONTENTS) {
                    builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                }
            }
        // Block
        modifyAll(RagiumBlocks.StorageBlocks.entries, materialText)
        modifyAll(RagiumBlocks.Grates.entries, tieredText(RagiumTranslationKeys.GRATE))
        modifyAll(RagiumBlocks.Casings.entries, tieredText(RagiumTranslationKeys.CASING))
        modifyAll(RagiumBlocks.Hulls.entries, tieredText(RagiumTranslationKeys.HULL))
        modifyAll(RagiumBlocks.Coils.entries, tieredText(RagiumTranslationKeys.COIL))
        modifyAll(RagiumBlocks.Burners.entries, tieredText(RagiumTranslationKeys.BURNER))

        modifyAll(RagiumBlocks.Drums.entries, tieredText(RagiumTranslationKeys.DRUM))
        // Item
        modifyAll(RagiumItems.MATERIALS, materialText)

        modifyAll(RagiumItems.Circuits.entries, tieredText(RagiumTranslationKeys.CIRCUIT))
        modifyAll(RagiumItems.Plastics.entries, tieredText(RagiumTranslationKeys.PLASTIC))

        modifyAll(RagiumItems.Radioactives.entries) { builder: DataComponentPatch.Builder, radioactive: RagiumItems.Radioactives ->
            builder.set(RagiumComponentTypes.RADIOACTIVE, radioactive.level)
        }

        LOGGER.info("Modified item components!")
    }
}
