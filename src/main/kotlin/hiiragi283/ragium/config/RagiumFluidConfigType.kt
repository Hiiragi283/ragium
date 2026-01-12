package hiiragi283.ragium.config

import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.util.StringRepresentable

enum class RagiumFluidConfigType :
    HTHasTranslationKey,
    StringRepresentable {
    FIRST_INPUT,
    SECOND_INPUT,
    FIRST_OUTPUT,
    SECOND_OUTPUT,
    THIRD_OUTPUT,
    ;

    override val translationKey: String by lazy {
        when (this) {
            FIRST_INPUT -> RagiumTranslation.CONFIG_FLUID_FIRST_INPUT
            SECOND_INPUT -> RagiumTranslation.CONFIG_FLUID_SECOND_INPUT
            FIRST_OUTPUT -> RagiumTranslation.CONFIG_FLUID_FIRST_OUTPUT
            SECOND_OUTPUT -> RagiumTranslation.CONFIG_FLUID_SECOND_OUTPUT
            THIRD_OUTPUT -> RagiumTranslation.CONFIG_FLUID_THIRD_OUTPUT
        }.translationKey
    }

    override fun getSerializedName(): String = name.lowercase()
}
