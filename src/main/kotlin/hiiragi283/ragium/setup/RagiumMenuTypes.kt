package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTDeferredMenuTypeRegister
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
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
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericItemContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment

typealias DeferredBEMenu<BE> = HTDeferredMenuType<HTBlockEntityContainerMenu<BE>>

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val DRUM: DeferredBEMenu<HTDrumBlockEntity> = registerBE("drum")

    @JvmField
    val SLOT_CONFIG: HTDeferredMenuType<HTSlotConfigurationMenu> =
        REGISTER.registerType("slot_configuration", ::HTSlotConfigurationMenu, ::getBlockEntityFromBuf)

    //    Generic    //

    @JvmField
    val GENERIC_9x1: HTDeferredMenuType<HTGenericItemContainerMenu> =
        REGISTER.registerItemType("generic_9x1", HTGenericItemContainerMenu::oneRow)

    @JvmField
    val GENERIC_9x3: HTDeferredMenuType<HTGenericContainerMenu> =
        REGISTER.registerType("generic_9x3", HTGenericContainerMenu::threeRow) {
            HTGenericItemContainerMenu.createSlots(3)
        }

    //    Generator    //

    @JvmField
    val FUEL_GENERATOR: DeferredBEMenu<HTFuelGeneratorBlockEntity> = registerBE("fuel_generator")

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: DeferredBEMenu<HTAlloySmelterBlockEntity> = registerBE("alloy_smelter")

    @JvmField
    val COMPRESSOR: DeferredBEMenu<HTCompressorBlockEntity> = registerBE("compressor")

    @JvmField
    val CRUSHER: DeferredBEMenu<HTCrusherBlockEntity> = registerBE("crusher")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = registerBE("energy_network_access")

    @JvmField
    val ENGRAVER: DeferredBEMenu<HTEngraverBlockEntity> = registerBE("engraver")

    @JvmField
    val EXTRACTOR: DeferredBEMenu<HTExtractorBlockEntity> = registerBE("extractor")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = registerBE("fluid_collector")

    @JvmField
    val ITEM_BUFFER: DeferredBEMenu<HTItemBufferBlockEntity> = registerBE("item_buffer")

    @JvmField
    val MELTER: DeferredBEMenu<HTMelterBlockEntity> = registerBE("melter")

    @JvmField
    val PULVERIZER: DeferredBEMenu<HTPulverizerBlockEntity> = registerBE("pulverizer")

    @JvmField
    val REFINERY: DeferredBEMenu<HTRefineryBlockEntity> = registerBE("refinery")

    @JvmField
    val SMELTER: DeferredBEMenu<HTSmelterBlockEntity> = registerBE("smelter")

    @JvmField
    val SIMULATOR: DeferredBEMenu<HTSimulatorBlockEntity> = registerBE("simulator")

    @JvmField
    val SINGLE_ITEM: DeferredBEMenu<HTBlockBreakerBlockEntity> = registerBE("single_item")

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
    inline fun <reified BE : HTBlockEntity> registerBE(name: String): HTDeferredMenuType<HTBlockEntityContainerMenu<BE>> {
        val holder: HTDeferredMenuType<HTBlockEntityContainerMenu<BE>> =
            HTDeferredMenuType.createType(RagiumAPI.id(name))
        return REGISTER.registerType(name, HTBlockEntityContainerMenu.create(holder), ::getBlockEntityFromBuf)
    }
}
