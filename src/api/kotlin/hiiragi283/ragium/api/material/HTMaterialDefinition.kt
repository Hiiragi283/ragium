package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.attribute.HTMaterialAttribute

interface HTMaterialDefinition {
    operator fun contains(clazz: Class<out HTMaterialAttribute>): Boolean

    operator fun <T : HTMaterialAttribute> get(clazz: Class<T>): T?

    fun getAllAttributes(): Collection<HTMaterialAttribute>

    interface Builder {
        operator fun <T : HTMaterialAttribute> get(clazz: Class<T>): T?

        fun add(vararg attributes: HTMaterialAttribute)

        fun set(vararg attributes: HTMaterialAttribute)

        fun remove(vararg classes: Class<out HTMaterialAttribute>)
    }

    data object Empty : HTMaterialDefinition {
        override fun contains(clazz: Class<out HTMaterialAttribute>): Boolean = false

        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = null

        override fun getAllAttributes(): Collection<HTMaterialAttribute> = listOf()
    }
}
