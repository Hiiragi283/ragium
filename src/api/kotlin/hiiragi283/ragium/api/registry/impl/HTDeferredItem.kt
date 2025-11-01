package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

typealias HTSimpleDeferredItem = HTDeferredItem<Item>

/**
 * @see net.neoforged.neoforge.registries.DeferredItem
 * @see mekanism.common.registration.impl.ItemRegistryObject
 */
class HTDeferredItem<ITEM : Item> :
    HTDeferredHolder<Item, ITEM>,
    HTHasTranslationKey,
    HTHasText,
    HTItemHolderLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.ITEM, id)

    override fun asItem(): Item = get()

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().description
}
