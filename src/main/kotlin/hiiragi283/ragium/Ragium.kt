package hiiragi283.ragium

import hiiragi283.core.api.data.pack.HTDynamicDatapack
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.common.storage.energy.HTBasicItemEnergyHandler
import hiiragi283.core.common.storage.fluid.HTBasicItemFluidTank
import hiiragi283.core.support.capability.HTEnergyCapabilities
import hiiragi283.core.support.capability.HTItemCapabilities
import hiiragi283.core.support.storage.energy.HTInfiniteEnergyHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTBatteryBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMiscRegister
import hiiragi283.ragium.support.storage.fluid.HTInfiniteItemFluidTank
import hiiragi283.ragium.support.storage.fluid.HTVoidItemFluidTank
import java.util.function.IntSupplier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(RagiumMiscRegister::register)

        RagiumFluids.register(eventBus)
        RagiumBlockEntityTypes.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        container.registerConfig(ModConfig.Type.SERVER, RagiumConfig.SERVER_SPEC)

        HTDynamicDatapack.addDomain(RagiumAPI.MOD_ID)

        RagiumAPI.LOGGER.info("Ragium loaded")
    }

    override fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
        event.register(RagiumDataMapTypes.MOB_HEAD)

        event.register(RagiumDataMapTypes.COOLANT)
        event.register(RagiumDataMapTypes.MAGMATIC_FUEL)
        event.register(RagiumDataMapTypes.COMBUSTION_FUEL)

        event.register(RagiumDataMapTypes.MATTER_POINT)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumRecipeLookups::init)
    }

    override fun registerCapabilities(helper: CapabilityHelper) {
        blockEntity(helper)

        // Item
        helper.registerItemTank(
            { container: ItemStack -> HTBasicItemFluidTank.create(container, getCapacity(container, RagiumConfig.SERVER.tankCapacity)) },
            RagiumBlocks.TANK,
        )
        helper.registerItemTank(::HTVoidItemFluidTank, RagiumBlocks.VOID_TANK)
        helper.registerItemTank(::HTInfiniteItemFluidTank, RagiumBlocks.CREATIVE_TANK)

        helper.registerItem(
            HTEnergyCapabilities,
            { container: ItemStack -> HTBasicItemEnergyHandler.create(container, getCapacity(container, RagiumConfig.SERVER.batteryCapacity)) },
            RagiumBlocks.BATTERY,
        )
        helper.registerItem(HTEnergyCapabilities, { HTInfiniteEnergyHandler }, RagiumBlocks.CREATIVE_BATTERY)

        helper.registerItem(
            HTEnergyCapabilities,
            { container: ItemStack -> HTBasicItemEnergyHandler.create(container, RagiumConfig.SERVER.electricIgniter.getCapacity()) },
            RagiumItems.ELECTRIC_IGNITER,
        )
        helper.registerItem(
            HTEnergyCapabilities,
            { container: ItemStack -> HTBasicItemEnergyHandler.create(container, 8000) },
            RagiumItems.CRYSTAL_BATTERY,
        )
    }

    @JvmStatic
    private fun blockEntity(helper: CapabilityHelper) {
        fun <BE : HTProcessorBlockEntity.Energized> registerProcessor(type: BlockEntityType<BE>) {
            helper.registerBlockEntity(type)
            helper.registerBlockEntity(HTEnergyCapabilities, type) { processor: BE, _ -> processor.handler }
        }

        fun <BE : HTBatteryBlockEntity<*>> registerBattery(type: BlockEntityType<BE>) {
            helper.registerBlockEntity(type)
            helper.registerBlockEntity(HTEnergyCapabilities, type) { processor: BE, _ -> processor.handler }
        }
        // Generator
        helper.registerBlockEntity(RagiumBlockEntityTypes.BOILER.get())
        // Machine
        registerProcessor(RagiumBlockEntityTypes.ALLOY_SMELTER.get())
        registerProcessor(RagiumBlockEntityTypes.ASSEMBLER.get())
        registerProcessor(RagiumBlockEntityTypes.AUTO_CHISEL.get())
        registerProcessor(RagiumBlockEntityTypes.COMPRESSOR.get())
        registerProcessor(RagiumBlockEntityTypes.CRUSHER.get())
        registerProcessor(RagiumBlockEntityTypes.CUTTING_MACHINE.get())
        registerProcessor(RagiumBlockEntityTypes.ELECTRIC_FURNACE.get())

        registerProcessor(RagiumBlockEntityTypes.FREEZER.get())
        registerProcessor(RagiumBlockEntityTypes.MELTER.get())
        registerProcessor(RagiumBlockEntityTypes.PYROLYZER.get())
        registerProcessor(RagiumBlockEntityTypes.REFINERY.get())

        registerProcessor(RagiumBlockEntityTypes.BREWERY.get())
        registerProcessor(RagiumBlockEntityTypes.MIXER.get())
        registerProcessor(RagiumBlockEntityTypes.WASHER.get())

        registerProcessor(RagiumBlockEntityTypes.PLANTER.get())

        registerProcessor(RagiumBlockEntityTypes.PRINTER.get())

        helper.registerBlockEntity(RagiumBlockEntityTypes.ENCHANTER.get())
        registerProcessor(RagiumBlockEntityTypes.FLUID_DUPLICATOR.get())
        registerProcessor(RagiumBlockEntityTypes.MASS_FABRICATOR.get())
        // Storage
        helper.registerBlockEntity(HTItemCapabilities, RagiumBlockEntityTypes.UNIVERSAL_CHEST.get(), HTUniversalChestBlockEntity::getItemHandler)

        registerBattery(RagiumBlockEntityTypes.BATTERY.get())
        helper.registerBlockEntity(RagiumBlockEntityTypes.CRATE.get())
        helper.registerBlockEntity(RagiumBlockEntityTypes.TANK.get())

        helper.registerBlockEntity(RagiumBlockEntityTypes.VOID_TANK.get())

        registerBattery(RagiumBlockEntityTypes.CREATIVE_BATTERY.get())
        helper.registerBlockEntity(RagiumBlockEntityTypes.CREATIVE_CRATE.get())
        helper.registerBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK.get())
    }

    @JvmStatic
    private fun getCapacity(context: ItemStack, base: IntSupplier): Int = RagiumDataComponents.getCapacity(base, context.getOrDefault(RagiumDataComponents.CAPACITY_SCALE, 1))
}
