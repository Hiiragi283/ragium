package hiiragi283.ragium.api.block.entity

import net.minecraft.world.level.block.entity.BlockEntity

/**
 * エラーメッセージを保持する[BlockEntity]
 */
fun interface HTErrorHoldingBlockEntity {
    fun getErrorMessage(): String?
}
