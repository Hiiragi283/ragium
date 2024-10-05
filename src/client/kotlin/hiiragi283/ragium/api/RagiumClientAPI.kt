package hiiragi283.ragium.api

import hiiragi283.ragium.client.InternalRagiumClientAPI
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import org.jetbrains.annotations.ApiStatus

@ApiStatus.NonExtendable
interface RagiumClientAPI {
    companion object {
        @JvmStatic
        fun getInstance(): RagiumClientAPI = InternalRagiumClientAPI
    }

    fun <T> registerMultiblockRenderer(type: BlockEntityType<T>) where T : BlockEntity, T : HTMultiblockController
}
