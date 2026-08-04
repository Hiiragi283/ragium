package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block

/**
 * シンプルな[HTDeferredBlock]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTSimpleDeferredBlock = HTDeferredBlock<Block>

/**
 * [ブロック][Block]向けの[HTDeferredHolder]の拡張クラスです。
 * @param BLOCK ブロックのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlock<out BLOCK : Block> :
    HTDeferredHolder<Block, BLOCK>,
    HTKeyLike.Translatable<Block> {
    constructor(key: ResourceKey<Block>) : super(key)

    constructor(id: Identifier) : super(Registries.BLOCK.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().name
}
