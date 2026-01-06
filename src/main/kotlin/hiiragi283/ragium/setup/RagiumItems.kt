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
import hiiragi283.ragium.common.item.HTLocationTicketItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.item.HTPotionDropItem
import hiiragi283.ragium.common.item.HTUpgradeItem
import hiiragi283.ragium.common.item.HTUpgradeType
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import hiiragi283.ragium.common.storge.energy.HTComponentEnergyBattery
import hiiragi283.ragium.common.storge.energy.HTComponentEnergyHandler
import hiiragi283.ragium.common.storge.fluid.HTComponentFluidHandler
import hiiragi283.ragium.common.storge.fluid.HTComponentFluidTank
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.ChatFormatting
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
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
    val TAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("tar")

    //    Foods    //

    @JvmField
    val MEAT_DUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_dust")

    @JvmField
    val MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_ingot") { it.food(Foods.BEEF) }

    @JvmField
    val COOKED_MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cooked_meat_ingot") { it.food(Foods.COOKED_BEEF) }

    @JvmStatic
    private fun registerCan(name: String, nutrition: Int, saturation: Float): HTSimpleDeferredItem =
        REGISTER.registerSimpleItem("${name}_can") {
            it.food(
                FoodProperties
                    .Builder()
                    .nutrition(nutrition)
                    .saturationModifier(saturation)
                    .fast()
                    .usingConvertsTo(Items.IRON_NUGGET)
                    .build(),
            )
        }

    /**
     * @see Foods.COOKED_COD
     */
    @JvmField
    val FISH_CAN: HTSimpleDeferredItem = registerCan("fish", 5, 0.6f)

    /**
     * @see Foods.APPLE
     */
    @JvmField
    val FRUIT_CAN: HTSimpleDeferredItem = registerCan("fruit", 4, 0.3f)

    /**
     * @see Foods.COOKED_BEEF
     */
    @JvmField
    val MEAT_CAN: HTSimpleDeferredItem = registerCan("meat", 8, 0.8f)

    /**
     * @see Foods.BEETROOT_SOUP
     */
    @JvmField
    val SOUP_CAN: HTSimpleDeferredItem = registerCan("soup", 6, 0.6f)

    //    Molds    //

    @JvmField
    val MOLDS: Map<HTMoldType, HTSimpleDeferredItem> = HTMoldType.entries.associateWith { moldType: HTMoldType ->
        REGISTER.registerSimpleItem("${moldType.serializedName}_mold")
    }

    //    Utilities    //

    @JvmField
    val BLANK_DISC: HTSimpleDeferredItem = REGISTER.registerSimpleItem("blank_disc")

    @JvmField
    val LOCATION_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("location_ticket", ::HTLocationTicketItem)

    @JvmField
    val LOOT_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    @JvmField
    val POTION_DROP: HTSimpleDeferredItem = REGISTER.registerItem("potion_drop", ::HTPotionDropItem)

    //   Upgrades    //

    @JvmField
    val UPGRADES: Map<HTUpgradeType, HTSimpleDeferredItem> = HTUpgradeType.entries.associateWith { type: HTUpgradeType ->
        val color: ChatFormatting = when (type.group) {
            HTUpgradeType.Group.CREATIVE -> ChatFormatting.RED
            HTUpgradeType.Group.GENERATOR -> ChatFormatting.LIGHT_PURPLE
            HTUpgradeType.Group.PROCESSOR -> ChatFormatting.AQUA
            HTUpgradeType.Group.DEVICE -> ChatFormatting.YELLOW
            HTUpgradeType.Group.STORAGE -> ChatFormatting.GREEN
        }
        REGISTER.registerItemWith("${type.serializedName}_upgrade", color, ::HTUpgradeItem)
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
