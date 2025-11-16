package hiiragi283.ragium.common.integration

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object RagiumCreateAddon : RagiumAddon {
    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val SAND_PAPER_MAP: Map<HTMaterialKey, HTSimpleDeferredItem> = listOf(
        RagiumMaterialKeys.RAGI_CRYSTAL,
        RagiumMaterialKeys.IRIDESCENTIUM,
    ).associateWith { key: HTMaterialKey ->
        ITEM_REGISTER.registerItem("${key.name}_sand_paper", ::SandPaperItem)
    }

    @JvmStatic
    fun getSandPaper(material: HTMaterialLike): HTSimpleDeferredItem =
        SAND_PAPER_MAP[material.asMaterialKey()] ?: error("Unknown sand paper for ${material.asMaterialName()}")

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        ITEM_REGISTER.register(eventBus)
    }

    override fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(getSandPaper(RagiumMaterialKeys.RAGI_CRYSTAL)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.MAX_DAMAGE, 8 * 8)
            builder.set(
                RagiumDataComponents.INTRINSIC_ENCHANTMENT,
                HTIntrinsicEnchantment(Enchantments.MENDING, 1),
            )
        }
        event.modify(getSandPaper(RagiumMaterialKeys.IRIDESCENTIUM)) { builder: DataComponentPatch.Builder ->
            builder.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            builder.set(DataComponents.RARITY, Rarity.EPIC)
            builder.set(DataComponents.UNBREAKABLE, Unbreakable(true))
        }
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.ITEMS) { event: BuildCreativeModeTabContentsEvent ->
            helper.insertAfter(
                event,
                RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
                getSandPaper(RagiumMaterialKeys.RAGI_CRYSTAL),
                getSandPaper(RagiumMaterialKeys.IRIDESCENTIUM),
            )
        }
    }
}
