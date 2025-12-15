package hiiragi283.ragium.common.data.brewing

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.PotionContents
import java.util.Optional

class HTEffectBrewingRecipeData(
    private val ingredient: HTItemIngredient,
    private val effect: Holder<MobEffect>,
    private val baseTime: Int,
    private val longTime: Int,
    private val strongTime: Int,
) : HTBrewingRecipeData {
    companion object {
        @JvmField
        val BI_CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTEffectBrewingRecipeData> = MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(HTEffectBrewingRecipeData::getIngredient),
            VanillaBiCodecs.holder(Registries.MOB_EFFECT).fieldOf("effect").forGetter(HTEffectBrewingRecipeData::effect),
            BiCodecs.POSITIVE_INT.fieldOf("base_time").forGetter(HTEffectBrewingRecipeData::baseTime),
            BiCodecs.POSITIVE_INT.fieldOf("long_time").forGetter(HTEffectBrewingRecipeData::longTime),
            BiCodecs.POSITIVE_INT.fieldOf("strong_time").forGetter(HTEffectBrewingRecipeData::strongTime),
            ::HTEffectBrewingRecipeData,
        )

        @JvmField
        val CODEC: MapCodec<HTEffectBrewingRecipeData> = BI_CODEC.codec

        @JvmStatic
        fun benefit(ingredient: HTItemIngredient, effect: Holder<MobEffect>): HTEffectBrewingRecipeData =
            HTEffectBrewingRecipeData(ingredient, effect, 3600, 9600, 1800)

        @JvmStatic
        fun harmful(ingredient: HTItemIngredient, effect: Holder<MobEffect>): HTEffectBrewingRecipeData =
            HTEffectBrewingRecipeData(ingredient, effect, 900, 1800, 432)
    }

    override fun type(): MapCodec<HTEffectBrewingRecipeData> = CODEC

    override fun getIngredient(): HTItemIngredient = ingredient

    private fun createContent(instance: MobEffectInstance): PotionContents =
        PotionContents(Optional.empty(), Optional.empty(), listOf(instance))

    override fun getBasePotion(): PotionContents = createContent(MobEffectInstance(effect, baseTime))

    override fun getLongPotion(): PotionContents = createContent(MobEffectInstance(effect, longTime))

    override fun getStrongPotion(): PotionContents = createContent(MobEffectInstance(effect, strongTime, 1))
}
