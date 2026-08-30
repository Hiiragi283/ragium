package hiiragi283.lib.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTPart

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTMaterialAccess {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialAccess = RagiumAPI.getService()
    }

    fun getMaterialManager(): HTMaterialManager

    fun getExistingContents(): HTMaterialContents.Provider

    fun getRegisteredContents(): HTMaterialContents.Provider

    fun getMaterialBlock(part: HTPart, key: HTMaterialKey): HTMaterialContents.BlockEntry? = getExistingContents().blocks[part, key] ?: getRegisteredContents().blocks[part, key]

    fun getMaterialItem(part: HTPart, key: HTMaterialKey): HTMaterialContents.ItemEntry? = getExistingContents().items[part, key] ?: getRegisteredContents().items[part, key]

    fun getMaterialBlockOrItem(part: HTPart, key: HTMaterialKey): HTMaterialContents.ItemEntry? = getExistingContents().getBlockOrItem(part, key) ?: getRegisteredContents().getBlockOrItem(part, key)
}
