package hiiragi283.ragium.api.material

/**
 * [HTMaterialType]が紐づいた[HTMaterialKey]
 * @see [HTMaterialRegistry.getTypedMaterial]
 */
sealed interface HTTypedMaterial {
    val type: HTMaterialType
    val material: HTMaterialKey

    operator fun component1(): HTMaterialType = type

    operator fun component2(): HTMaterialKey = material
}
