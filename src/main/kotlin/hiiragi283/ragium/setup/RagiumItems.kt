package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTDeferredItemRegister
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.common.item.endgame.HTCreativeItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTAgarMediumItem
import hiiragi283.ragium.common.item.HTBatteryItem
import hiiragi283.ragium.common.item.HTElectricIgniterItem
import hiiragi283.ragium.common.item.HTLocationTicketItem
import hiiragi283.ragium.common.item.HTLootTicketItem
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

/**
 * @see hiiragi283.core.setup.HCItems
 */
data object RagiumItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
    }

    //    Materials    //

    // Overworld
    @JvmField
    val RAGI_ALLOY_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ragi_alloy_compound")

    @JvmField
    val CRYO_CHARGE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cryo_charge")

    @JvmField
    val AGAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("agar")

    @JvmField
    val AGAR_MEDIUM: HTSimpleDeferredItem = REGISTER.registerItem("agar_medium", ::HTAgarMediumItem)

    // Nether
    @JvmField
    val CRUDE_SILICON: HTSimpleDeferredItem = REGISTER.registerSimpleItem("crude_silicon")

    @JvmField
    val GLYCEROL_DROP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("glycerol_drop")

    @JvmField
    val NITROGLYCERIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("nitroglycerin")

    @JvmField
    val NITROCELLULOSE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("nitrocellulose")

    @JvmField
    val SMOKELESS_POWDER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("smokeless_powder")

    //    Parts    //

    // Basic

    // Advanced
    @JvmField
    val MERCURY_BOTTLE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("mercury_bottle")

    @JvmField
    val THERMOMETER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("thermometer") { it.rarity(Rarity.UNCOMMON) }

    // Elite
    @JvmField
    val SILICON_WAFER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("silicon_wafer")

    @JvmField
    val CIRCUIT_CHIP: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_chip")

    @JvmField
    val CIRCUIT_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("circuit_board")

    @JvmField
    val ELECTRIC_CIRCUIT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("electric_circuit") { it.rarity(Rarity.RARE) }

    // Ultimate
    @JvmField
    val ARTIFICIAL_ARTIFACT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("artificial_artifact") { it.rarity(Rarity.EPIC) }

    //    Foods    //

    @JvmField
    val MINCED_MEAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_dust")

    @JvmField
    val MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("meat_ingot") { it.food(Foods.BEEF) }

    @JvmField
    val COOKED_MEAT_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cooked_meat_ingot") { it.food(Foods.COOKED_BEEF) }

    @JvmField
    val CANNED_COOKED_MEAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("canned_cooked_meat") { it.food(RagiumFoods.CANNED_COOKED_MEAT) }

    //    Utilities    //

    // Basic
    @JvmField
    val BLANK_DISC: HTSimpleDeferredItem = REGISTER.registerSimpleItem("blank_disc")

    @JvmField
    val ELECTRIC_IGNITER: HTSimpleDeferredItem = REGISTER.registerItem("electric_igniter", ::HTElectricIgniterItem)

    // Advanced
    @JvmField
    val LOCATION_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("location_ticket", ::HTLocationTicketItem)

    // Elite
    @JvmField
    val CRYSTAL_BATTERY: HTSimpleDeferredItem = REGISTER.registerItem("crystal_battery", ::HTBatteryItem)

    @JvmField
    val DYNAMITE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("dynamite")

    // Ultimate

    //    End Game    //

    @JvmField
    val RAGI_MATTER: HTSimpleDeferredItem = REGISTER.registerItem("ragi_matter", ::HTCreativeItem)

    @JvmField
    val RAGI_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("ragi_ticket", ::HTLootTicketItem)

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }
    }
}
