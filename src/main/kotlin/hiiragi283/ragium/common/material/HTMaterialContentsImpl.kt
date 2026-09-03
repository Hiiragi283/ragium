package hiiragi283.ragium.common.material

import hiiragi283.lib.collection.Table
import hiiragi283.ragium.api.material.HTMaterialContents
import hiiragi283.ragium.api.material.HTPart
import hiiragi283.ragium.api.material.RagiumMaterial

internal class HTMaterialContentsImpl<R : HTPart, out V : Any>(
    table: Table<R, RagiumMaterial, V>,
    private val errorFactory: (R, RagiumMaterial) -> String
) : HTMaterialContents<R, V>,
    Table<R, RagiumMaterial, V> by table {
    override fun getErrorMessage(row: R, column: RagiumMaterial): String = errorFactory(row, column)
}
