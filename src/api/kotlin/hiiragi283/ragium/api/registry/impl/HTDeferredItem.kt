package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.util.translate.HTHasComponent
import hiiragi283.ragium.api.util.translate.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see [net.neoforged.neoforge.registries.DeferredItem]
 */
class HTDeferredItem<ITEM : Item>(key: ResourceKey<Item>) :
    HTDeferredHolder<Item, ITEM>(key),
    ItemLike,
    HTHasTranslationKey,
    HTHasComponent {
    constructor(id: ResourceLocation) : this(ResourceKey.create(Registries.ITEM, id))

    fun toStack(count: Int = 1): ItemStack {
        val stack: ItemStack = asItem().defaultInstance
        check(!stack.isEmpty) { "Obtained empty item stack; incorrect getDefaultInstance() call?" }
        stack.count = count
        return stack
    }

    override fun asItem(): Item = get()

    override val translationKey: String get() = get().descriptionId

    override fun getComponent(): Component = get().description
}
