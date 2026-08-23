package hiiragi283.lib.material

import hiiragi283.lib.collection.ListMultiMap
import net.minecraft.resources.Identifier

interface HTMaterialManager {
    companion object {
        @JvmStatic
        fun create(materials: Collection<HTMaterial>): HTMaterialManager = object : HTMaterialManager {
            private val idMap: Map<Identifier, HTMaterial> by lazy { materials.associateBy(HTMaterial::getId) }
            private val categoryMap: ListMultiMap<HTMaterialCategory, HTMaterial> by lazy { ListMultiMap.copyOf(materials.groupBy(HTMaterial::category)) }

            override val materials: Collection<HTMaterial> = materials

            override fun get(id: Identifier): HTMaterial? = idMap[id]

            override fun get(category: HTMaterialCategory): Collection<HTMaterial> = categoryMap[category]
        }
    }

    val materials: Collection<HTMaterial>

    operator fun get(id: Identifier): HTMaterial?

    operator fun get(category: HTMaterialCategory): Collection<HTMaterial>
}
