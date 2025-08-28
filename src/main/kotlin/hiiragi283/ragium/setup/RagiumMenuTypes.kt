package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTDeferredMenuTypeRegister
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
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.inventory.container.HTPotionBundleMenu
import hiiragi283.ragium.common.inventory.container.HTUniversalBundleMenu
import hiiragi283.ragium.common.storage.item.HTUniversalBundleManager
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

typealias DeferredBEMenu<BE> = HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE>

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

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
    inline fun <reified BE : HTBlockEntity> register(name: String): HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE> {
        val holder: HTDeferredMenuType<HTBlockEntityContainerMenu<BE>, BE> =
            HTDeferredMenuType.createType(RagiumAPI.id(name))
        return REGISTER.registerType(name, HTBlockEntityContainerMenu.create(holder), ::getBlockEntityFromBuf)
    }

    @JvmField
    val DRUM: DeferredBEMenu<HTDrumBlockEntity> = register("drum")

    @JvmField
    val POTION_BUNDLE: HTDeferredMenuType<HTPotionBundleMenu, IItemHandler> =
        REGISTER.registerType("potion_bundle", ::HTPotionBundleMenu) { buf: RegistryFriendlyByteBuf? ->
            checkNotNull(buf)
            val stack: ItemStack = ItemStack.STREAM_CODEC.decode(buf)
            checkNotNull(stack.getCapability(Capabilities.ItemHandler.ITEM)) { "Failed to get potion bundle container" }
        }

    @JvmField
    val SLOT_CONFIG: HTDeferredMenuType<HTSlotConfigurationMenu, HTMachineBlockEntity> =
        REGISTER.registerType("slot_configuration", ::HTSlotConfigurationMenu, ::getBlockEntityFromBuf)

    @JvmField
    val UNIVERSAL_BUNDLE: HTDeferredMenuType<HTUniversalBundleMenu, IItemHandler> =
        REGISTER.registerType("universal_backpack", ::HTUniversalBundleMenu) {
            HTUniversalBundleManager.emptyHandler()
        }

    //    Generator    //

    @JvmField
    val FUEL_GENERATOR: DeferredBEMenu<HTFuelGeneratorBlockEntity> = register("fuel_generator")

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: DeferredBEMenu<HTAlloySmelterBlockEntity> = register("alloy_smelter")

    @JvmField
    val COMPRESSOR: DeferredBEMenu<HTCompressorBlockEntity> = register("compressor")

    @JvmField
    val CRUSHER: DeferredBEMenu<HTCrusherBlockEntity> = register("crusher")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = register("energy_network_access")

    @JvmField
    val ENGRAVER: DeferredBEMenu<HTEngraverBlockEntity> = register("engraver")

    @JvmField
    val EXTRACTOR: DeferredBEMenu<HTExtractorBlockEntity> = register("extractor")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = register("fluid_collector")

    @JvmField
    val ITEM_BUFFER: DeferredBEMenu<HTItemBufferBlockEntity> = register("item_buffer")

    @JvmField
    val MELTER: DeferredBEMenu<HTMelterBlockEntity> = register("melter")

    @JvmField
    val PULVERIZER: DeferredBEMenu<HTPulverizerBlockEntity> = register("pulverizer")

    @JvmField
    val REFINERY: DeferredBEMenu<HTRefineryBlockEntity> = register("refinery")

    @JvmField
    val SMELTER: DeferredBEMenu<HTSmelterBlockEntity> = register("smelter")

    @JvmField
    val SINGLE_ITEM: DeferredBEMenu<HTBlockBreakerBlockEntity> = register("single_item")

    @JvmField
    val SOLIDIFIER: DeferredBEMenu<HTSolidifierBlockEntity> = register("solidifier")

    @JvmField
    val TELEPAD: DeferredBEMenu<HTTelepadBlockentity> = register("telepad")
}
