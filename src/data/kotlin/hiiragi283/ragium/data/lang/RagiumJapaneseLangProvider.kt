package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.init.RagiumFluids
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.JA_JP),
    RagiumLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)

        // Fluid
        addFluid(RagiumFluids.HONEY, "ハチミツ")
        // add(RagiumFluids.POTION.getFluidType().descriptionId, "無効なポーション入りバケツ")
        // add(RagiumFluids.POTION.bucketHolder, $$"%1$s入りバケツ")
        addFluid(RagiumFluids.OMINOUS_FLUX, "不吉な流動体")
        addFluid(RagiumFluids.MOLTEN_GLASS, "溶融ガラス")
        addFluid(RagiumFluids.MOLTEN_ENDER, "共振エンダー")
        addFluid(RagiumFluids.MOLTEN_BLAZE, "ブレイズの血液")
    }
}
