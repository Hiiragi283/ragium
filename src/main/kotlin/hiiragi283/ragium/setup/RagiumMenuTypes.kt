package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuTypeRegister
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.inventory.HTAccessConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerRows
import hiiragi283.ragium.common.inventory.container.HTMachineContainerMenu
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment

typealias DeferredBEMenu<BE> = HTDeferredMenuType<HTBlockEntityContainerMenu<BE>>

typealias DeferredMachineMenu<BE> = HTDeferredMenuType<HTMachineContainerMenu<BE>>

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val DRUM: DeferredBEMenu<HTDrumBlockEntity> = registerBE("drum")

    @JvmField
    val ACCESS_CONFIG: HTDeferredMenuType<HTAccessConfigurationMenu> =
        REGISTER.registerType("access_configuration", ::HTAccessConfigurationMenu, ::getBlockEntityFromBuf)

    //    Item    //

    @JvmField
    val POTION_BUNDLE: HTDeferredMenuType<HTPotionBundleContainerMenu> =
        REGISTER.registerItemType("potion_bundle", ::HTPotionBundleContainerMenu)

    @JvmField
    val UNIVERSAL_BUNDLE: HTDeferredMenuType<HTGenericContainerMenu> =
        REGISTER.registerType("universal_bundle", HTGenericContainerMenu::threeRow) {
            HTGenericContainerRows.createHandler(3)
        }

    //    Generator    //

    @JvmField
    val FUEL_GENERATOR: DeferredMachineMenu<HTFuelGeneratorBlockEntity> = registerMachine("fuel_generator")

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: DeferredMachineMenu<HTAlloySmelterBlockEntity> = registerMachine("alloy_smelter")

    @JvmField
    val COMPRESSOR: DeferredMachineMenu<HTCompressorBlockEntity> = registerMachine("compressor")

    @JvmField
    val CRUSHER: DeferredMachineMenu<HTCrusherBlockEntity> = registerMachine("crusher")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = registerBE("energy_network_access")

    @JvmField
    val ENGRAVER: DeferredMachineMenu<HTEngraverBlockEntity> = registerMachine("engraver")

    @JvmField
    val EXTRACTOR: DeferredMachineMenu<HTExtractorBlockEntity> = registerMachine("extractor")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = registerBE("fluid_collector")

    @JvmField
    val ITEM_BUFFER: DeferredBEMenu<HTItemBufferBlockEntity> = registerBE("item_buffer")

    @JvmField
    val MELTER: DeferredMachineMenu<HTMelterBlockEntity> = registerMachine("melter")

    @JvmField
    val PULVERIZER: DeferredMachineMenu<HTPulverizerBlockEntity> = registerMachine("pulverizer")

    @JvmField
    val REFINERY: DeferredMachineMenu<HTRefineryBlockEntity> = registerMachine("refinery")

    @JvmField
    val SMELTER: DeferredMachineMenu<HTSingleItemInputBlockEntity<*>> = registerMachine("smelter")

    @JvmField
    val SIMULATOR: DeferredMachineMenu<HTSimulatorBlockEntity> = registerMachine("simulator")

    @JvmField
    val SINGLE_ITEM: DeferredMachineMenu<HTBlockBreakerBlockEntity> = registerMachine("single_item")

    @JvmField
    val TELEPAD: DeferredBEMenu<HTTelepadBlockentity> = registerBE("telepad")

    //    Extensions    //

    /**
     * @see [mekanism.common.inventory.container.type.MekanismContainerType.getTileFromBuf]
     */
    @JvmStatic
    inline fun <reified BE : BlockEntity> getBlockEntityFromBuf(buf: FriendlyByteBuf?): BE {
        checkNotNull(buf)
        check(FMLEnvironment.dist.isClient) { "Only supported on client side" }
        return Minecraft.getInstance().level?.getBlockEntity(buf.readBlockPos()) as? BE
            ?: error("Failed to find block entity on client side")
    }

    @JvmStatic
    inline fun <reified BE : HTBlockEntity> registerBE(name: String): DeferredBEMenu<BE> {
        val holder: DeferredBEMenu<BE> = HTDeferredMenuType.createType(RagiumAPI.id(name))
        return REGISTER.registerType(
            name,
            { containerId: Int, inventory: Inventory, context: BE -> HTBlockEntityContainerMenu(holder, containerId, inventory, context) },
            ::getBlockEntityFromBuf,
        )
    }

    @JvmStatic
    inline fun <reified BE : HTMachineBlockEntity> registerMachine(name: String): DeferredMachineMenu<BE> {
        val holder: DeferredMachineMenu<BE> = HTDeferredMenuType.createType(RagiumAPI.id(name))
        return REGISTER.registerType(
            name,
            { containerId: Int, inventory: Inventory, context: BE -> HTMachineContainerMenu(holder, containerId, inventory, context) },
            ::getBlockEntityFromBuf,
        )
    }
}
