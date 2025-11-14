package hiiragi283.ragium.api.data.registry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTPotionBuilder
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.translatableText
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

@JvmRecord
data class HTBrewingEffect(val ingredient: HTItemIngredient, val content: PotionContents) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTBrewingEffect> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.codec
                        .fieldOf("ingredient")
                        .forGetter(HTBrewingEffect::ingredient),
                    PotionContents.CODEC.fieldOf("content").forGetter(HTBrewingEffect::content),
                ).apply(instance, ::HTBrewingEffect)
        }

        @JvmField
        val HOLDER_CODEC: Codec<Holder<HTBrewingEffect>> = RegistryFixedCodec.create(RagiumAPI.BREWING_EFFECT_KEY)

        @JvmStatic
        fun getPotion(lookup: HolderLookup<HTBrewingEffect>, stack: ItemStack): ItemStack = lookup
            .listElements()
            .filter { holder: Holder.Reference<HTBrewingEffect> -> holder.value().ingredient.test(stack) }
            .map(Holder.Reference<HTBrewingEffect>::value)
            .findFirst()
            .map(HTBrewingEffect::toPotion)
            .orElse(ItemStack.EMPTY)
    }

    constructor(ingredient: HTItemIngredient, potion: Holder<Potion>) : this(ingredient, PotionContents(potion))

    constructor(ingredient: HTItemIngredient, builderAction: HTPotionBuilder.() -> Unit) : this(
        ingredient,
        HTPotionBuilder.create(builderAction),
    )

    fun toPotion(): ItemStack {
        val stack: ItemStack = createItemStack(Items.POTION, DataComponents.POTION_CONTENTS, content)
        if (content.potion.isEmpty) {
            val first: MobEffectInstance = content.allEffects.firstOrNull() ?: return stack
            stack.set(
                DataComponents.ITEM_NAME,
                RagiumTranslation.ITEM_POTION.translate(translatableText(first.descriptionId)),
            )
        }
        return stack
    }
}
