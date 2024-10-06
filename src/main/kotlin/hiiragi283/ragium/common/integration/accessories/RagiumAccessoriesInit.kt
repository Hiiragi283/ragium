package hiiragi283.ragium.common.integration.accessories

import hiiragi283.ragium.api.RagiumAPI
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.components.AccessoriesDataComponents
import io.wispforest.accessories.api.components.AccessorySlotValidationComponent
import io.wispforest.accessories.api.slot.SlotReference
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.component.ComponentMap
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

object RagiumAccessoriesInit {
    private val slotCache: MutableMap<Item, Set<String>> = mutableMapOf()

    @JvmStatic
    fun init() {
        if (!FabricLoader.getInstance().isModLoaded("accessories")) return

        registerAccessory(
            Items.CONDUIT,
            object : Accessory {
                override fun onEquip(stack: ItemStack, reference: SlotReference) {
                    reference.entity().addStatusEffect(StatusEffectInstance(StatusEffects.CONDUIT_POWER, -1, 0))
                }

                override fun onUnequip(stack: ItemStack, reference: SlotReference) {
                    reference.entity().removeStatusEffect(StatusEffects.CONDUIT_POWER)
                }
            },
            "necklace",
        )

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
    private fun registerAccessory(item: ItemConvertible, accessory: Accessory, vararg slots: String) {
        AccessoriesAPI.registerAccessory(item.asItem(), accessory)
        slotCache[item.asItem()] = setOf(*slots)
    }
}
