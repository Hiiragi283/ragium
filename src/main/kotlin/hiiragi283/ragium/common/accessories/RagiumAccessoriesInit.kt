package hiiragi283.ragium.common.accessories

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessories.HTDefaultAccessorySlots
import hiiragi283.ragium.api.accessories.HTEmptyAccessory
import hiiragi283.ragium.api.accessories.HTStatusEffectAccessory
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.util.isModLoaded
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.components.AccessoriesDataComponents
import io.wispforest.accessories.api.components.AccessorySlotValidationComponent
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.ComponentMap
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items

object RagiumAccessoriesInit {
    private val slotCache: MutableMap<Item, Set<String>> = mutableMapOf()

    @JvmStatic
    fun init() {
        if (!isModLoaded("accessories")) return

        registerAccessory(
            RagiumContents.DIVING_GOGGLES,
            HTStatusEffectAccessory.Provider(StatusEffects.WATER_BREATHING),
            HTDefaultAccessorySlots.FACE,
        )
        registerAccessory(
            RagiumContents.NIGHT_VISION_GOGGLES,
            HTStatusEffectAccessory.Provider(StatusEffects.NIGHT_VISION),
            HTDefaultAccessorySlots.FACE,
        )
        registerAccessory(
            RagiumContents.PISTON_BOOTS,
            HTStatusEffectAccessory.Provider(StatusEffects.JUMP_BOOST),
            HTDefaultAccessorySlots.SHOES,
        )
        registerAccessory(
            RagiumContents.PARACHUTE,
            HTStatusEffectAccessory.Provider(StatusEffects.SLOW_FALLING),
            HTDefaultAccessorySlots.CAPE,
        )

        registerAccessory(
            Items.CONDUIT,
            HTStatusEffectAccessory.Provider(StatusEffects.CONDUIT_POWER),
            HTDefaultAccessorySlots.NECKLACE,
        )

        registerAccessory(RagiumContents.BACKPACK, HTEmptyAccessory, HTDefaultAccessorySlots.BACK)
        registerAccessory(RagiumContents.LARGE_BACKPACK, HTEmptyAccessory, HTDefaultAccessorySlots.BACK)
        registerAccessory(RagiumContents.ENDER_BACKPACK, HTEmptyAccessory, HTDefaultAccessorySlots.BACK)
        // pendant
        registerAccessory(
            RagiumContents.Element.RAGIUM.pendantItem,
            HTStatusEffectAccessory.Provider(StatusEffects.FIRE_RESISTANCE),
            HTDefaultAccessorySlots.NECKLACE,
        )
        registerAccessory(
            RagiumContents.Element.RIGIUM.pendantItem,
            HTStatusEffectAccessory.Remover(
                StatusEffects.NAUSEA,
                StatusEffects.HUNGER,
                StatusEffects.POISON,
            ),
            HTDefaultAccessorySlots.NECKLACE,
        )
        registerAccessory(
            RagiumContents.Element.RUGIUM.pendantItem,
            HTStatusEffectAccessory.Provider(StatusEffects.LUCK, level = 7),
            HTDefaultAccessorySlots.NECKLACE,
        )
        registerAccessory(
            RagiumContents.Element.REGIUM.pendantItem,
            HTStatusEffectAccessory.Provider(StatusEffects.CONDUIT_POWER),
            HTDefaultAccessorySlots.NECKLACE,
        )
        registerAccessory(
            RagiumContents.Element.ROGIUM.pendantItem,
            HTStatusEffectAccessory.Remover(
                StatusEffects.SLOWNESS,
                StatusEffects.MINING_FATIGUE,
                StatusEffects.WEAKNESS,
            ),
            HTDefaultAccessorySlots.NECKLACE,
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
