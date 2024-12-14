package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.storage.HTFluidVariantStack

fun interface HTScreenFluidProvider {
    fun getFluidsToSync(): Map<Int, HTFluidVariantStack>
}
