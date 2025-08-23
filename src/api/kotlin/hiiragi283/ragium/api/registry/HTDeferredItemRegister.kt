package hiiragi283.ragium.api.registry

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[DeferredRegister.Items]
 */
class HTDeferredItemRegister(namespace: String) : DeferredRegister.Items(namespace) {
    override fun getEntries(): List<DeferredItem<*>> =
        super.getEntries().map { holder: DeferredHolder<Item, *> -> DeferredItem.createItem<Item>(holder.id) }
}
