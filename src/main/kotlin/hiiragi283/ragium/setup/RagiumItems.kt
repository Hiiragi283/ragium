package hiiragi283.ragium.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.capability.HTEnergyCapabilities
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTPotionDropItem
import hiiragi283.ragium.common.item.HTTraderCatalogItem
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import hiiragi283.ragium.common.storge.energy.HTComponentEnergyBattery
import hiiragi283.ragium.common.storge.energy.HTComponentEnergyHandler
import hiiragi283.ragium.common.storge.fluid.HTComponentFluidHandler
import hiiragi283.ragium.common.storge.fluid.HTComponentFluidTank
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import org.slf4j.Logger

/**
 * @see hiiragi283.core.setup.HCItems
 */
object RagiumItems {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        eventBus.addListener(::registerItemCapabilities)

        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredItem> = buildTable {
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            for (prefix: HTMaterialPrefix in material.getItemPrefixesToGenerate()) {
                this[prefix, material.asMaterialKey()] = REGISTER.registerSimpleItem(prefix.createPath(material))
            }
        }
    }.let(::HTMaterialTable)

    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val RAGIUM_POWDER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragium_powder")

    //    Utilities    //

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val POTION_DROP: HTSimpleDeferredItem = REGISTER.registerItem("potion_drop", ::HTPotionDropItem)

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover") {
        it.description(RagiumTranslation.SLOT_COVER)
    }

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem) {
        it.description(RagiumTranslation.TRADER_CATALOG)
    }

    //    Event    //

    @JvmStatic
    private fun registerItemCapabilities(event: RegisterCapabilitiesEvent) {
        // Fluid
        registerFluid(
            event,
            { context: HTComponentHandler.ContainerContext ->
                val capacity: Int = HTUpgradeHelper.getFluidCapacity(context.attachedTo, RagiumConfig.COMMON.tankCapacity.asInt)
                HTComponentFluidTank.create(context, capacity)
            },
            RagiumBlocks.TANK,
        )

        // Energy
        registerEnergy(
            event,
            { context: HTComponentHandler.ContainerContext ->
                val capacity: Int = HTUpgradeHelper.getEnergyCapacity(context.attachedTo, RagiumConfig.COMMON.batteryCapacity.asInt)
                HTComponentEnergyBattery.create(context, capacity)
            },
            RagiumBlocks.BATTERY,
        )

        LOGGER.info("Registered Item Capabilities!")
    }

    @JvmStatic
    fun registerFluid(
        event: RegisterCapabilitiesEvent,
        factory: HTComponentHandler.ContainerFactory<HTFluidTank>,
        vararg items: ItemLike,
    ) {
        event.registerItem(
            HTFluidCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentFluidHandler(stack, 1, factory) },
            *items,
        )
    }

    @JvmStatic
    fun registerEnergy(
        event: RegisterCapabilitiesEvent,
        factory: HTComponentHandler.ContainerFactory<HTEnergyBattery>,
        vararg items: ItemLike,
    ) {
        event.registerItem(
            HTEnergyCapabilities.item,
            { stack: ItemStack, _: Void? -> HTComponentEnergyHandler(stack, 1, factory) },
            *items,
        )
    }

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(RagiumDataComponents.DESCRIPTION, translation)
}
