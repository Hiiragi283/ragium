package hiiragi283.ragium.integration.mekanism

import mekanism.common.base.IChemicalConstant
import java.awt.Color

enum class RagiumChemicalConstants(
    private val color: Color,
    private val temp: Float = 300f,
    private val density: Float = 1f,
    private val light: Int = 0,
) : IChemicalConstant {
    // Sap
    CRIMSON_SAP(Color(0x660000)),
    WARPED_SAP(Color(0x006666)),
    ;

    override fun getName(): String = name.lowercase()

    override fun getColor(): Int = color.rgb

    override fun getTemperature(): Float = temp

    override fun getDensity(): Float = density

    override fun getLightLevel(): Int = light
}
