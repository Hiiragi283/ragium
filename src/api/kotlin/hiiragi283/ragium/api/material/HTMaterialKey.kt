package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

/**
 * 素材の種類を管理するキー
 */
class HTMaterialKey private constructor(override val materialName: String) :
    HTMaterial,
    Comparable<HTMaterialKey> {
        companion object {
            private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

            /**
             * 指定された[name]から単一のインスタンスを返します。
             */
            @JvmStatic
            fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
        }

        /**
         * 素材の名前の翻訳キー
         */
        @JvmField
        val translationKey: String = "material.${RagiumAPI.MOD_ID}.$materialName"

        /**
         * 素材の名前の[MutableComponent]
         */
        val text: MutableComponent
            get() = Component.translatable(translationKey)

        //    Comparable    //

        override fun compareTo(other: HTMaterialKey): Int = materialName.compareTo(other.materialName)

        override fun equals(other: Any?): Boolean = (other as? HTMaterial)?.materialName == materialName

        override fun hashCode(): Int = materialName.hashCode()

        override fun toString(): String = "HTMaterialKey[$materialName]"
    }
