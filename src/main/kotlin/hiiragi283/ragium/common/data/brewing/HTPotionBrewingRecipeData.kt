package hiiragi283.ragium.common.data.brewing

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import java.util.Optional

class HTPotionBrewingRecipeData(
    private val ingredient: HTItemIngredient,
    private val base: Holder<Potion>,
    private val long: Optional<Holder<Potion>>,
    private val strong: Optional<Holder<Potion>>,
) : HTBrewingRecipeData {
    companion object {
        @JvmField
        val BI_CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTPotionBrewingRecipeData> = MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(HTPotionBrewingRecipeData::getIngredient),
            VanillaBiCodecs.holder(Registries.POTION).fieldOf("base").forGetter(HTPotionBrewingRecipeData::base),
            VanillaBiCodecs.holder(Registries.POTION).optionalFieldOf("long").forGetter(HTPotionBrewingRecipeData::long),
            VanillaBiCodecs.holder(Registries.POTION).optionalFieldOf("strong").forGetter(HTPotionBrewingRecipeData::strong),
            ::HTPotionBrewingRecipeData,
        )

        @JvmField
        val CODEC: MapCodec<HTPotionBrewingRecipeData> = BI_CODEC.codec
    }

    override fun type(): MapCodec<HTPotionBrewingRecipeData> = CODEC

    override fun getIngredient(): HTItemIngredient = ingredient

    override fun getBasePotion(): PotionContents = PotionContents(base)

    override fun getLongPotion(): PotionContents = PotionContents(long, Optional.empty(), listOf())

    override fun getStrongPotion(): PotionContents = PotionContents(strong, Optional.empty(), listOf())
}
