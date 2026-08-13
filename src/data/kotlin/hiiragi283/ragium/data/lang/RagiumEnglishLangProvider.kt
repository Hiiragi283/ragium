package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.fluid.RagiumFluids
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.EN_US),
    RagiumLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)

        // Fluid
        addFluid(RagiumFluids.HONEY, "Honey")
        // add(RagiumFluids.POTION.getFluidType().descriptionId, "Invalid Potion Bucket")
        // add(RagiumFluids.POTION.bucketHolder, $$"%1$s Bucket")
        addFluid(RagiumFluids.OMINOUS_FLUX, "Ominous Flux")
        addFluid(RagiumFluids.MOLTEN_GLASS, "Molten Glass")
        addFluid(RagiumFluids.MOLTEN_ENDER, "Resonant Ender")
        addFluid(RagiumFluids.MOLTEN_BLAZE, "Blaze Blood")

        // Text
        add(RagiumTranslation.RAGIUM, "Ragium")
    }
}
