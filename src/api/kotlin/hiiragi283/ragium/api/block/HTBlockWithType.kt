package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.type.HTBlockType

/**
 * [HTBlockType]を保持するブロックを表すインターフェース
 * @see mekanism.common.block.interfaces.ITypeBlock
 */
interface HTBlockWithType {
    fun type(): HTBlockType
}
