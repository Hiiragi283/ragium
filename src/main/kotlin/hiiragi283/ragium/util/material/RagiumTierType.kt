package hiiragi283.ragium.util.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.material.HTMaterialType

enum class RagiumTierType : HTMaterialType {
    BASIC {
        override fun getEnergyRate(): Int = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Basic"
            HTLanguageType.JA_JP -> "基本"
        }
    },
    ADVANCED {
        override fun getEnergyRate(): Int = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Advanced"
            HTLanguageType.JA_JP -> "発展"
        }
    },
    ELITE {
        override fun getEnergyRate(): Int = 0

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Elite"
            HTLanguageType.JA_JP -> "精鋭"
        }
    },
    ULTIMATE {
        override fun getEnergyRate(): Int = 0

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Ultimate"
            HTLanguageType.JA_JP -> "究極"
        }
    },
    ;

    abstract fun getEnergyRate(): Int

    override fun getSerializedName(): String = name.lowercase()
}
