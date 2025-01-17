package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import net.minecraft.resources.ResourceLocation

/**
 * RagiumのAPI
 */
@Suppress("DEPRECATION")
interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        /**
         * 名前空間が`ragium`となる[ResourceLocation]を返します。
         */
        @JvmStatic
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

        /**
         * 指定した[id]の名前空間を`ragium`に変えます。
         */
        @JvmStatic
        fun wrapId(id: ResourceLocation): ResourceLocation = id(id.path)

        /**
         * [RagiumAPI]の単一のインスタンスを返します。
         */
        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI
    }

    /**
     * [RagiumPlugin]の一覧です。
     */
    val plugins: List<RagiumPlugin>

    /**
     * 機械レジストリのインスタンスです。
     */
    val machineRegistry: HTMachineRegistry

    /**
     * 素材レジストリのインスタンスです。
     */
    val materialRegistry: HTMaterialRegistry
}
