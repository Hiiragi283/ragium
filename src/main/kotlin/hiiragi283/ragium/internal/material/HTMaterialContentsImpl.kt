package hiiragi283.ragium.internal.material

import hiiragi283.lib.collection.Table
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.HTPartKey

internal class HTMaterialContentsImpl<out V : Any>(
    table: Table<HTPartKey, HTMaterialKey, V>,
    private val errorFactory: (HTPartKey, HTMaterialKey) -> String,
) : HTMaterialContents<V>,
    Table<HTPartKey, HTMaterialKey, V> by table {
    override fun getErrorMessage(row: HTPartKey, column: HTMaterialKey): String = errorFactory(row, column)
}
