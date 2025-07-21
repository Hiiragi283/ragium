package hiiragi283.ragium.api.registry

import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredItem

interface HTItemSet {
    val itemHolders: List<DeferredItem<*>>

    fun getItemHolderSet(): HolderSet.Direct<Item> = HolderSet.direct(DeferredItem<*>::getDelegate, itemHolders)

    fun getItems(): List<Item> = itemHolders.map(ItemLike::asItem)

    fun init(eventBus: IEventBus)

    fun addItemModels(provider: ItemModelProvider)

    fun addTranslationEn(name: String, provider: LanguageProvider)

    fun addTranslationJp(name: String, provider: LanguageProvider)
}
