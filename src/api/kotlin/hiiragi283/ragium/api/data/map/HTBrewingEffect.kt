package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.item.component.HTPotionBuilder
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

@JvmRecord
data class HTBrewingEffect(val content: PotionContents) {
    companion object {
        @JvmField
        val CODEC: Codec<HTBrewingEffect> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    PotionContents.CODEC.fieldOf("content").forGetter(HTBrewingEffect::content),
                ).apply(instance, ::HTBrewingEffect)
        }
    }

    constructor(potion: Holder<Potion>) : this(PotionContents(potion))

    constructor(builderAction: HTPotionBuilder.() -> Unit) : this(HTPotionBuilder.create(builderAction))

    fun toPotion(): ItemStack {
        val stack: ItemStack = createItemStack(Items.POTION, DataComponents.POTION_CONTENTS, content)
        if (content.potion.isEmpty) {
            content.allEffects
                .firstOrNull()
                ?.let(MobEffectInstance::getDescriptionId)
                ?.let { id: String -> RagiumTranslation.ITEM_POTION.getComponent(Component.translatable(id)) }
                ?.let { name: Component -> stack.set(DataComponents.ITEM_NAME, name) }
        }
        return stack
    }
}
