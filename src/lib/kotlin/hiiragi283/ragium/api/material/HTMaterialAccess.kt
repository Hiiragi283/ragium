package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTMaterialAccess {
    companion object {
        @JvmField
        val INSTANCE: HTMaterialAccess = RagiumAPI.getService()
    }

    val existing: HTMaterialContents.Provider

    val registered: HTMaterialContents.Provider

    fun getMaterialBlock(part: HTBlockPart, key: RagiumMaterial): HTMaterialContents.BlockEntry? =
        existing.blocks[part, key] ?: registered.blocks[part, key]

    fun getMaterialItem(part: HTItemPart, key: RagiumMaterial): HTMaterialContents.ItemEntry? =
        existing.items[part, key] ?: registered.items[part, key]

    fun getMaterialBlockOrItem(part: HTPart, key: RagiumMaterial): HTMaterialContents.ItemEntry? =
        existing.getBlockOrItem(part, key) ?: registered.getBlockOrItem(part, key)
}
