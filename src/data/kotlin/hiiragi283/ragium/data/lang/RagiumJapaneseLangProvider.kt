package hiiragi283.ragium.data.lang

import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems
import net.minecraft.data.PackOutput

class RagiumJapaneseLangProvider(output: PackOutput) :
    HTLangProvider(output, RagiumAPI.MOD_ID, HTLangTypes.JA_JP),
    RagiumLangProvider {
    override fun addTranslations() {
        addPatternTranslations(this)

        // Fluid
        addFluid(RagiumFluids.HONEY, "ハチミツ")
        add(RagiumFluids.POTION.getFluidType().descriptionId, "無効なポーション入りバケツ")
        add(RagiumFluids.POTION.bucketHolder, $$"%1$s入りバケツ")
        addFluid(RagiumFluids.OMINOUS_FLUX, "不吉な流動体")
        addFluid(RagiumFluids.MOLTEN_GLASS, "溶融ガラス")
        addFluid(RagiumFluids.MOLTEN_REDSTONE, "励起レッドストーン")
        addFluid(RagiumFluids.MOLTEN_GLOWSTONE, "活性グロウストーン")
        addFluid(RagiumFluids.MOLTEN_ENDER, "共振エンダー")
        addFluid(RagiumFluids.MOLTEN_BLAZE, "ブレイズの血液")

        // Item
        add(RagiumItems.BAMBOO_CHARCOAL, "竹炭")
        add(RagiumItems.PARTICLE_BOARD, "パーティクルボード")
        add(RagiumItems.SYNTHETIC_FEATHER, "合成羽")
        add(RagiumItems.SYNTHETIC_FIBER, "合成繊維")
        add(RagiumItems.SYNTHETIC_LEATHER, "合成牛皮")
        add(RagiumItems.ELDER_HEART, "エルダーの心臓")
        add(RagiumItems.WITHER_DOLL, "ウィザー人形")
        add(RagiumItems.WITHER_STAR, "ウィザースター")

        // Recipe Type
        add(RagiumRecipeTypes.ASSEMBLING, "組立")
        add(RagiumRecipeTypes.CRUSHING, "粉砕")

        add(RagiumRecipeTypes.FREEZING, "冷凍")
        add(RagiumRecipeTypes.MELTING, "溶融")
        // Text
        add(RagiumTranslation.RAGIUM, "ラギウム")
    }
}
