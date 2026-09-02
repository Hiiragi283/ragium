package hiiragi283.lib.registry

import hiiragi283.lib.item.HTItemInstanceLike
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.lib.text.Text
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
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
    HTDeferredHolder<Item, ITEM>,
    HTHasTranslationKey,
    HTHasText,
    ItemLike,
    HTItemInstanceLike {
    constructor(key: ResourceKey<Item>) : super(key)

    constructor(id: Identifier) : super(Registries.ITEM.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = this.toStack().itemName

    override fun asItem(): ITEM = get()

    override fun toTemplate(count: Int, patch: DataComponentPatch): ItemStackTemplate? = asOptional().map { ItemStackTemplate(it, count, patch) }.getOrNull()

    override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = when {
        this.isBound -> ItemStack(this, count, patch)
        else -> ItemStack.EMPTY
    }
}
