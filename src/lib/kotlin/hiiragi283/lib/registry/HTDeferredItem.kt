package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.text.Text
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

/**
 * シンプルな[HTDeferredItem]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTSimpleDeferredItem = HTDeferredItem<Item>

/**
 * [アイテム][Item]向けの[HTDeferredHolder]の拡張クラスです。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredItem<out ITEM : Item> :
    HTDeferredHolder.Translatable<Item, ITEM>,
    ItemLike,
    HTItemInstanceLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: Identifier) : super(Registries.ITEM.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = this.asStackOrEmpty().itemName

    override fun asItem(): ITEM = get()

    override fun asTemplate(count: Int, patch: DataComponentPatch): Result<ItemStackTemplate> = when {
        this.isAir -> Result.failure(IllegalStateException("Could not create ItemStackTemplate with air item"))
        else -> asResult().map { ItemStackTemplate(it, count, patch) }
    }
}
