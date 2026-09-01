package hiiragi283.lib.data.lang

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTPotionContent
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.HTHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.LanguageProvider

/**
 * Hiiragi Seriesで使用される[LanguageProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTLangProvider(output: PackOutput, modId: String, val langType: HTLangType) : LanguageProvider(output, modId, langType.name.lowercase()) {
    companion object {
        @JvmField
        val BUCKET_PATTERN = HTLangPatternProvider("%s Bucket", "%s入りバケツ")

        @JvmField
        val POTION_PATTERN = HTLangPatternProvider("Potion of %s", "%sのポーション")

        @JvmField
        val SPLASH_POTION_PATTERN = HTLangPatternProvider("Splash Potion of %s", "%sのスプラッシュポーション")

        @JvmField
        val LINGERING_POTION_PATTERN = HTLangPatternProvider("Lingering Potion of %s", "%sの残留ポーション")

        @JvmField
        val TIPPED_ARROW_PATTERN = HTLangPatternProvider("Arrow of %s", "%sの矢")
    }

    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    fun add(translatable: HTHasTranslationKey, name: HTLangName) {
        add(translatable, name.getTranslatedName(langType))
    }

    fun add(translatable: HTHasTranslationKey, pattern: HTLangPatternProvider, name: HTLangName) {
        add(translatable, pattern.translate(langType, name))
    }

    /**
     * エンチャントの翻訳名を追加します。
     * @param value エンチャントの翻訳名
     * @param desc エンチャントの説明
     */
    @JvmName("addEnchantment")
    protected fun add(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toLanguageKey(), value)
        add(key.toLanguageKey("desc"), desc)
    }

    /**
     * 液体の翻訳名を登録します。
     */
    fun addFluid(content: HTFluidContent, value: String) {
        add(content.typeHolder, value)
        add(content.fluidTag, value)

        val bucketName: String = BUCKET_PATTERN.translate(langType, value)
        add(content.bucketHolder, bucketName)
        add(content.bucketTag, bucketName)
    }

    fun addPotion(content: HTPotionContent, value: String) {
        addCustomPotion(content.getOrThrow().name(), value)
    }

    fun addCustomPotion(name: String, value: String) {
        add("item.minecraft.potion.effect.$name", POTION_PATTERN.translate(langType, value))
        add("item.minecraft.splash_potion.effect.$name", SPLASH_POTION_PATTERN.translate(langType, value))
        add("item.minecraft.lingering_potion.effect.$name", LINGERING_POTION_PATTERN.translate(langType, value))
        add("item.minecraft.tipped_arrow.effect.$name", TIPPED_ARROW_PATTERN.translate(langType, value))
    }
}
