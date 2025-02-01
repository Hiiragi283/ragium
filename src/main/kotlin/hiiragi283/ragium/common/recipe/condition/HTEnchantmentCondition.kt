package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asHolderText
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level

class HTEnchantmentCondition(val enchantments: HolderSet<Enchantment>, val minLevel: Int = 1) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTEnchantmentCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    RegistryCodecs
                        .homogeneousList(Registries.ENCHANTMENT)
                        .fieldOf("enchantment")
                        .forGetter(HTEnchantmentCondition::enchantments),
                    Codec.INT.optionalFieldOf("min_level", 1).forGetter(HTEnchantmentCondition::minLevel),
                ).apply(instance, ::HTEnchantmentCondition)
        }
    }

    constructor(enchantment: Holder<Enchantment>, minLevel: Int = 1) : this(HolderSet.direct(enchantment), minLevel)

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC
    override val text: MutableComponent =
        Component
            .translatable(
                RagiumTranslationKeys.ENCHANTMENT_CONDITION,
                enchantments.asHolderText { it.value().description },
            ).withStyle(ChatFormatting.LIGHT_PURPLE)

    override fun test(level: Level, pos: BlockPos): Boolean {
        val machineEnch: ItemEnchantments = level.getMachineEntity(pos)?.enchantments ?: return false
        return enchantments.map(machineEnch::getLevel).any { it > minLevel }
    }
}
