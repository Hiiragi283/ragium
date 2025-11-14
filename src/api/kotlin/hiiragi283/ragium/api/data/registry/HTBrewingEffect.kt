package hiiragi283.ragium.api.data.registry

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.text.translatableText
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import java.util.Optional

@JvmRecord
data class HTBrewingEffect(val ingredient: HTItemIngredient, val contents: Either<Holder<Potion>, List<HTMobEffectInstance>>) {
    companion object {
        @JvmField
        val DIRECT_CODEC: Codec<HTBrewingEffect> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.codec
                        .fieldOf("ingredient")
                        .forGetter(HTBrewingEffect::ingredient),
                    Codec
                        .xor(RegistryFixedCodec.create(Registries.POTION), HTMobEffectInstance.CODEC.codec.listOf())
                        .fieldOf("contents")
                        .forGetter(HTBrewingEffect::contents),
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

    constructor(ingredient: HTItemIngredient, potion: Holder<Potion>) : this(ingredient, Either.left(potion))

    constructor(ingredient: HTItemIngredient, builderAction: MutableList<HTMobEffectInstance>.() -> Unit) : this(
        ingredient,
        Either.right(buildList(builderAction)),
    )

    fun getPotionContents(): PotionContents = contents.map(
        ::PotionContents,
    ) { instances: List<HTMobEffectInstance> ->
        PotionContents(
            Optional.empty(),
            Optional.empty(),
            instances.map(HTMobEffectInstance::toVanilla),
        )
    }

    fun getFirstEffect(): MobEffectInstance? = contents
        .map(
            { potion: Holder<Potion> -> potion.value().effects },
            { instances: List<HTMobEffectInstance> -> instances.map(HTMobEffectInstance::toVanilla) },
        ).firstOrNull()

    fun toPotion(): ItemStack {
        val contents: PotionContents = getPotionContents()
        val stack: ItemStack = createItemStack(Items.POTION, DataComponents.POTION_CONTENTS, contents)
        if (contents.potion.isEmpty) {
            val first: MobEffectInstance = contents.allEffects.firstOrNull() ?: return stack
            stack.set(
                DataComponents.ITEM_NAME,
                RagiumTranslation.ITEM_POTION.translate(translatableText(first.descriptionId)),
            )
        }
        return stack
    }
}
