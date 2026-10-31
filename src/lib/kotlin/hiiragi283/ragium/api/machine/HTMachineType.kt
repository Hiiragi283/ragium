package hiiragi283.ragium.api.machine

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI

/**
 * Ragiumで追加される機械のカテゴリを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTMachineType :
    HTMaterialLike,
    HTLangName {
    companion object {
        @JvmField
        val entries: Sequence<HTMachineType> = HTMachineType::class.sealedSubclasses
            .asSequence()
            .mapNotNull { it.objectInstance }

        @JvmField
        val COMPARATOR: Comparator<HTMachineType> = compareBy(HTMachineType::materialName)

        @JvmField
        val DYNAMO_PREFIX = HTTagPrefix("dynamos", "dynamos/%s", RagiumAPI.MOD_ID)

        @JvmField
        val MACHINE_PREFIX = HTTagPrefix("machines", "machines/%s", RagiumAPI.MOD_ID)

        @JvmField
        val MECHANICAL: HTMachineType = Mechanical

        @JvmField
        val HEAT: HTMachineType = Heat

        @JvmField
        val CHEMICAL: HTMachineType = Chemical

        @JvmField
        val RESOURCE: HTMachineType = Resource

        @JvmField
        val ELECTRONICS: HTMachineType = Electronics

        @JvmField
        val ARCANE: HTMachineType = Arcane
    }

    data object Mechanical : HTMachineType, HTLangName by HTLangName("Mechanical", "機械加工") {
        override val materialName: String = "mechanical"
    }

    data object Heat : HTMachineType, HTLangName by HTLangName("Heat", "熱") {
        override val materialName: String = "heat"
    }

    data object Chemical : HTMachineType, HTLangName by HTLangName("Chemical", "化学") {
        override val materialName: String = "chemical"
    }

    data object Resource : HTMachineType, HTLangName by HTLangName("Resource", "資源") {
        override val materialName: String = "resource"
    }

    data object Electronics : HTMachineType, HTLangName by HTLangName("Electronics", "電子") {
        override val materialName: String = "electronics"
    }

    data object Arcane : HTMachineType, HTLangName by HTLangName("Arcane", "神秘") {
        override val materialName: String = "arcane"
    }

    class Custom(override val materialName: String, langName: HTLangName) :
        HTMachineType,
        HTLangName by langName {
        override fun hashCode(): Int = materialName.hashCode()

        override fun equals(other: Any?): Boolean = other is Custom && other.materialName == this.materialName

        override fun toString(): String = "Other(materialName=$materialName)"
    }
}
