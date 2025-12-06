package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuTypeRegister
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTFluidCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.block.entity.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.base.HTItemGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMobCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCombinerBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTItemWithCatalystBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.inventory.container.HTAccessConfigurationMenu
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerMenu
import hiiragi283.ragium.common.inventory.container.HTGenericContainerRows
import hiiragi283.ragium.common.inventory.container.HTPotionBundleContainerMenu
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.fml.loading.FMLEnvironment

typealias DeferredBEMenu<BE> = HTDeferredMenuType.WithContext<HTBlockEntityContainerMenu<BE>, BE>

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
    val ITEM_GENERATOR: DeferredBEMenu<HTItemGeneratorBlockEntity> = registerBE("item_generator")

    @JvmField
    val COMBUSTION_GENERATOR: DeferredBEMenu<HTCombustionGeneratorBlockEntity> = registerBE("combustion_generator")

    //    Machine    //

    @JvmField
    val COMBINER: DeferredBEMenu<HTAbstractCombinerBlockEntity> = registerBE("combiner")

    @JvmField
    val ENERGY_NETWORK_ACCESS: DeferredBEMenu<HTEnergyNetworkAccessBlockEntity> = registerBE("energy_network_access")

    @JvmField
    val FLUID_COLLECTOR: DeferredBEMenu<HTFluidCollectorBlockEntity> = registerBE("fluid_collector")

    @JvmField
    val ITEM_COLLECTOR: DeferredBEMenu<HTItemCollectorBlockEntity> = registerBE("item_collector")

    @JvmField
    val ITEM_WITH_CATALYST: DeferredBEMenu<HTItemWithCatalystBlockEntity> =
        registerBE("item_with_catalyst")

    @JvmField
    val MELTER: DeferredBEMenu<HTMelterBlockEntity> = registerBE("melter")

    @JvmField
    val MIXER: DeferredBEMenu<HTMixerBlockEntity> = registerBE("mixer")

    @JvmField
    val MOB_CRUSHER: DeferredBEMenu<HTMobCrusherBlockEntity> = registerBE("mob_crusher")

    @JvmField
    val PROCESSOR: DeferredBEMenu<HTProcessorBlockEntity<*, *>> = registerBE("processor")

    @JvmField
    val REFINERY: DeferredBEMenu<HTRefineryBlockEntity> = registerBE("refinery")

    @JvmField
    val SINGLE_ITEM_WITH_FLUID: DeferredBEMenu<HTSingleItemInputBlockEntity.CachedWithTank<*>> =
        registerBE("single_item_with_fluid")

    @JvmField
    val SMELTER: DeferredBEMenu<HTAbstractSmelterBlockEntity> = registerBE("smelter")

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
}
