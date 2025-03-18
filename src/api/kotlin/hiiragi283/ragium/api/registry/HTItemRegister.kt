package hiiragi283.ragium.api.registry

import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

class HTItemRegister(namespace: String) : DeferredRegister.Items(namespace) {
    override fun getEntries(): List<DeferredItem<out Item>> =
        super.getEntries().map { holder: DeferredHolder<Item, out Item> -> DeferredItem.createItem<Item>(holder.id) }
}
