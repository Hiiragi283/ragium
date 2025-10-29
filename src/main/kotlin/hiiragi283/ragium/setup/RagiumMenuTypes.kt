package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getTypedBlockEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuTypeRegister
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTChancedItemOutputBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTCuttingMachineBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMobCapturerBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.inventory.HTAccessConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerRows
import hiiragi283.ragium.common.inventory.container.HTMachineContainerMenu
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment

typealias DeferredBEMenu<BE> = HTDeferredMenuType.WithContext<HTBlockEntityContainerMenu<BE>, BE>

typealias DeferredMachineMenu<BE> = HTDeferredMenuType.WithContext<HTMachineContainerMenu<BE>, BE>

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTDeferredMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val DRUM: DeferredBEMenu<HTDrumBlockEntity> = registerBE("drum")

    @JvmField
    val ACCESS_CONFIG: HTDeferredMenuType.WithContext<HTAccessConfigurationMenu, HTConfigurableBlockEntity> =
        REGISTER.registerType("access_configuration", ::HTAccessConfigurationMenu, ::getBlockEntityFromBuf)

    //    Item    //

    @JvmField
    val POTION_BUNDLE: HTDeferredMenuType.OnHand<HTPotionBundleContainerMenu> =
        REGISTER.registerItemType("potion_bundle", ::HTPotionBundleContainerMenu)

    @JvmField
    val UNIVERSAL_BUNDLE: HTDeferredMenuType.WithContext<HTGenericContainerMenu, HTItemHandler> =
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
    val CHANCED_ITEM_OUTPUT: DeferredMachineMenu<HTChancedItemOutputBlockEntity<*, *>> = registerMachine("chanced_item_output")

    @JvmField
    val COMPRESSOR: DeferredMachineMenu<HTSingleItemInputBlockEntity<*>> = registerMachine("compressor")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = registerBE("energy_network_access")

    @JvmField
    val CUTTING_MACHINE: DeferredMachineMenu<HTCuttingMachineBlockEntity> = registerMachine("cutting_machine")

    @JvmField
    val EXTRACTOR: DeferredMachineMenu<HTSingleItemInputBlockEntity<*>> = registerMachine("extractor")

    @JvmField
    val EXP_COLLECTOR: DeferredBEMenu<HTExpCollectorBlockEntity> = registerBE("exp_collector")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = registerBE("fluid_collector")

    @JvmField
    val ITEM_BUFFER: DeferredBEMenu<HTItemBufferBlockEntity> = registerBE("item_buffer")

    @JvmField
    val MELTER: DeferredMachineMenu<HTMelterBlockEntity> = registerMachine("melter")

    @JvmField
    val MOB_CAPTURER: DeferredBEMenu<HTMobCapturerBlockEntity> = registerBE("mob_capturer")

    @JvmField
    val PULVERIZER: DeferredMachineMenu<HTSingleItemInputBlockEntity<*>> = registerMachine("pulverizer")

    @JvmField
    val REFINERY: DeferredMachineMenu<HTRefineryBlockEntity> = registerMachine("refinery")

    @JvmField
    val SMELTER: DeferredMachineMenu<HTMultiSmelterBlockEntity> = registerMachine("smelter")

    @JvmField
    val SIMULATOR: DeferredMachineMenu<HTSimulatorBlockEntity> = registerMachine("simulator")

    @JvmField
    val SINGLE_ITEM: DeferredMachineMenu<HTBlockBreakerBlockEntity> = registerMachine("single_item")

    @JvmField
    val TELEPAD: DeferredBEMenu<HTTelepadBlockentity> = registerBE("telepad")

    //    Extensions    //

    /**
     * @see mekanism.common.inventory.container.type.MekanismContainerType.getTileFromBuf
     */
    @JvmStatic
    inline fun <reified BE : BlockEntity> getBlockEntityFromBuf(buf: FriendlyByteBuf?): BE {
        checkNotNull(buf)
        check(FMLEnvironment.dist.isClient) { "Only supported on client side" }
        val level: Level = checkNotNull(Minecraft.getInstance().level) { "Failed to find client level" }
        val pos: BlockPos = buf.readBlockPos()
        return checkNotNull(level.getTypedBlockEntity<BE>(pos)) { "No block entity is present at $pos" }
    }

    @JvmStatic
    inline fun <reified BE : HTBlockEntity> registerBE(name: String): DeferredBEMenu<BE> {
        val holder: DeferredBEMenu<BE> = HTDeferredMenuType.WithContext(RagiumAPI.id(name))
        return REGISTER.registerType(
            name,
            { containerId: Int, inventory: Inventory, context: BE -> HTBlockEntityContainerMenu(holder, containerId, inventory, context) },
            ::getBlockEntityFromBuf,
        )
    }

    @JvmStatic
    inline fun <reified BE : HTMachineBlockEntity> registerMachine(name: String): DeferredMachineMenu<BE> {
        val holder: DeferredMachineMenu<BE> = HTDeferredMenuType.WithContext(RagiumAPI.id(name))
        return REGISTER.registerType(
            name,
            { containerId: Int, inventory: Inventory, context: BE -> HTMachineContainerMenu(holder, containerId, inventory, context) },
            ::getBlockEntityFromBuf,
        )
    }
}
