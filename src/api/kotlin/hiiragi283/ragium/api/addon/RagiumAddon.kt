package hiiragi283.ragium.api.addon

import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModList
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import java.util.function.BiConsumer

/**
 * Ragiumのアドオン向けのインターフェース
 * これを実装したクラスは[HTAddon]アノテーションをつける必要があります。
 */
interface RagiumAddon {
    fun priority(): Int = 0

    //    Initialization    //

    /**
     * modのコンストラクタで呼び出されます。
     */
    fun onModConstruct(eventBus: IEventBus, dist: Dist) {}

    /**
     * [FMLCommonSetupEvent]中に呼び出されます。
     */
    fun onCommonSetup(event: FMLCommonSetupEvent) {}

    /**
     * [FMLDedicatedServerSetupEvent]中に呼び出されます。
     */
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {}

    /**
     * [FMLClientSetupEvent]中に呼び出されます。
     */

    fun onClientSetup(event: FMLClientSetupEvent) {}

    //    Extension    //

    fun registerMaterial(consumer: BiConsumer<HTMaterialType, HTMaterialVariant.ItemTag>) {}

    fun modifyComponents(event: ModifyDefaultComponentsEvent) {}

    fun buildCreativeTabs(helper: CreativeTabHelper) {}

    //    Provider    //

    /**
     * これを実装したクラスは[HTAddon]アノテーションをつける必要があります。
     */
    fun interface Provider {
        fun getAddons(modList: ModList): List<RagiumAddon>
    }

    //    CreativeTabHelper    //

    @JvmRecord
    data class CreativeTabHelper(private val event: BuildCreativeModeTabContentsEvent) {
        fun ifMatchTab(key: ResourceKey<CreativeModeTab>, action: (BuildCreativeModeTabContentsEvent) -> Unit) {
            if (event.tabKey == key) action(event)
        }

        fun ifMatchTab(holder: Holder<CreativeModeTab>, action: (BuildCreativeModeTabContentsEvent) -> Unit) {
            if (holder.`is`(event.tabKey)) action(event)
        }

        fun insertAfter(event: BuildCreativeModeTabContentsEvent, vararg items: ItemLike) {
            insertAfter(event, items.toList())
        }

        fun insertAfter(event: BuildCreativeModeTabContentsEvent, items: List<ItemLike>) {
            for (i: Int in items.indices) {
                val item: ItemLike = items[i]
                val nextItem: ItemLike = items.getOrNull(i + 1) ?: continue
                event.insertAfter(
                    ItemStack(item),
                    ItemStack(nextItem),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
    }
}
