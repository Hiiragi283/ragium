package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.storage.HTFluidVariantStack

/**
 * 液体の同期を行うインターフェース
 */
fun interface HTScreenFluidProvider {
    /**
     * 同期するスロットと液体の組を返します。
     */
    fun getFluidsToSync(): Map<Int, HTFluidVariantStack>
}
