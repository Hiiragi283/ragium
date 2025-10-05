package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.item.component.HTPotionBuilder
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

data class HTBrewingEffect(val content: PotionContents) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTBrewingEffect> = BiCodec.composite(
            BiCodecs.POTION.fieldOf("content"),
            HTBrewingEffect::content,
            ::HTBrewingEffect,
        )
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
