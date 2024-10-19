package hiiragi283.ragium.client.integration.accessories

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.extension.isModLoaded
import hiiragi283.ragium.common.RagiumContents
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.components.AccessoriesDataComponents
import io.wispforest.accessories.api.components.AccessorySlotValidationComponent
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.ComponentMap
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

object RagiumAccessoriesInit {
    private val slotCache: MutableMap<Item, Set<String>> = mutableMapOf()

    @JvmStatic
    fun init() {
        if (!isModLoaded("accessories")) return

        HTAccessoryRegistry.slotTypes.forEach { (item: ItemConvertible, slot: HTAccessorySlotTypes) ->
            registerAccessory(item, HTWrappedAccessory, slot)
        }

        registerAccessory(RagiumContents.Accessories.BACKPACK, HTEmptyAccessory, HTAccessorySlotTypes.BACK)

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            context.modify(slotCache::containsKey) { builder: ComponentMap.Builder, item: Item ->
                val slots: Set<String> = slotCache.getOrDefault(item, setOf())
                builder.add(
                    AccessoriesDataComponents.SLOT_VALIDATION,
                    AccessorySlotValidationComponent(slots, setOf()),
                )
            }
        }

        RagiumAPI.log { info("Accessories integration loaded!") }
    }

    @JvmStatic
    private fun registerAccessory(item: ItemConvertible, accessory: Accessory, vararg slots: HTAccessorySlotTypes) {
        AccessoriesAPI.registerAccessory(item.asItem(), accessory)
        slotCache[item.asItem()] = slots.map(HTAccessorySlotTypes::asString).toSet()
    }
}
