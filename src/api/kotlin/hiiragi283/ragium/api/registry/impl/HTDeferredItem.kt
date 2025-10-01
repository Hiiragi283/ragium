package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

/**
 * @see [net.neoforged.neoforge.registries.DeferredItem]
 */
class HTDeferredItem<ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTHasTranslationKey,
    HTItemHolderLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ITEM, id)

    override fun asItem(): Item = get()

    override val translationKey: String get() = get().descriptionId
}
