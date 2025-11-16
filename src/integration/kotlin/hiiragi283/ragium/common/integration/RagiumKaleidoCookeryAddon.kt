package hiiragi283.ragium.common.integration

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTKitchenKnifeToolVariant
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumEquipmentMaterials
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import kotlin.collections.iterator

object RagiumKaleidoCookeryAddon : RagiumAddon {
    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    // Knives
    @JvmField
    val KNIFE_MAP: Map<HTMaterialKey, HTDeferredItem<*>> = listOf(
        RagiumEquipmentMaterials.RAGI_ALLOY,
        RagiumEquipmentMaterials.RAGI_CRYSTAL,
    ).associateBy(HTEquipmentMaterial::asMaterialKey)
        .mapValues { (key: HTMaterialKey, material: HTEquipmentMaterial) ->
            HTKitchenKnifeToolVariant.registerItem(ITEM_REGISTER, material)
        }

    @JvmStatic
    fun getKnife(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.IRON -> HTItemHolderLike.fromHolder(ModItems.IRON_KITCHEN_KNIFE)
        VanillaMaterialKeys.GOLD -> HTItemHolderLike.fromHolder(ModItems.GOLD_KITCHEN_KNIFE)
        VanillaMaterialKeys.DIAMOND -> HTItemHolderLike.fromHolder(ModItems.DIAMOND_KITCHEN_KNIFE)
        VanillaMaterialKeys.NETHERITE -> HTItemHolderLike.fromHolder(ModItems.NETHERITE_KITCHEN_KNIFE)
        else -> KNIFE_MAP[key] ?: error("Unknown knife item for ${key.name}")
    }

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        ITEM_REGISTER.register(eventBus)
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.ITEMS) { event: BuildCreativeModeTabContentsEvent ->
            for ((key: HTMaterialKey, knife: HTDeferredItem<*>) in KNIFE_MAP) {
                helper.insertAfter(event, RagiumItems.getHammer(key), knife)
            }
        }
    }
}
