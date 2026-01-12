package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.math.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeKey
import hiiragi283.ragium.api.upgrade.HTUpgradePropertyMap
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction
import java.util.Optional
import java.util.function.Consumer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

/**
 * @see net.minecraft.world.item.enchantment.Enchantment
 */
@ConsistentCopyVisibility
data class HTUpgradeData private constructor(
    val propertyMap: HTUpgradePropertyMap,
    private val targetSet: Optional<HTItemIngredient>,
    val exclusiveSet: Optional<HTItemIngredient>,
) : Map<HTUpgradeKey, Fraction> by propertyMap {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTUpgradeData> = BiCodec.composite(
            HTUpgradePropertyMap.CODEC.fieldOf("properties").forGetter(HTUpgradeData::propertyMap),
            HTItemIngredient.UNSIZED_CODEC.optionalFieldOf("target_set").forGetter(HTUpgradeData::targetSet),
            HTItemIngredient.UNSIZED_CODEC.optionalFieldOf("exclusive_set").forGetter(HTUpgradeData::exclusiveSet),
            ::HTUpgradeData,
        )

        /**
         * @see net.minecraft.world.item.enchantment.Enchantment.areCompatible
         */
        @JvmStatic
        fun areCompatible(first: HTItemResourceType, second: HTItemResourceType): Boolean {
            val firstData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(first) ?: return false
            val secondData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(second) ?: return false
            val bool1: Boolean = !firstData.exclusiveSet.map { it.testOnlyType(second) }.orElse(false)
            val bool2: Boolean = !secondData.exclusiveSet.map { it.testOnlyType(first) }.orElse(false)
            return bool1 && bool2
        }

        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTUpgradeData = Builder().apply(builderAction).build()
    }

    fun isTarget(stack: ItemStack): Boolean = targetSet.map { it.test(stack) }.orElse(true)

    fun appendTooltips(consumer: Consumer<Component>) {
        // Properties
        for ((key: HTUpgradeKey, property: Fraction) in propertyMap) {
            consumer.accept(key.translateColored(HTDefaultColor.GRAY, HTUpgradeHelper.getPropertyColor(key, property), property))
        }
        // Target Set
        targetSet
            .map(HTItemIngredient::getText)
            .map { RagiumTranslation.TOOLTIP_UPGRADE_TARGET.translateColored(HTDefaultColor.BLUE, HTDefaultColor.GRAY, it) }
            .ifPresent(consumer)
        // Exclusive Set
        exclusiveSet
            .map(HTItemIngredient::getText)
            .map { RagiumTranslation.TOOLTIP_UPGRADE_EXCLUSIVE.translateColored(HTDefaultColor.RED, HTDefaultColor.GRAY, it) }
            .ifPresent(consumer)
    }

    //    Builder    //

    class Builder {
        private var propertyMap: MutableMap<HTUpgradeKey, Fraction> = mutableMapOf()
        private var targetSet: HTItemIngredient? = null
        private var exclusiveSet: HTItemIngredient? = null

        operator fun set(key: HTUpgradeKey, value: Fraction) {
            propertyMap[key] = value
        }

        operator fun set(key: HTUpgradeKey, value: Int) {
            set(key, fraction(value))
        }

        fun targetSet(targetSet: HTItemIngredient?): Builder = apply {
            this.targetSet = targetSet
        }

        fun exclusiveSet(exclusiveSet: HTItemIngredient?): Builder = apply {
            this.exclusiveSet = exclusiveSet
        }

        fun build(): HTUpgradeData = HTUpgradeData(
            HTUpgradePropertyMap.create(propertyMap),
            Optional.ofNullable(targetSet),
            Optional.ofNullable(exclusiveSet),
        )
    }
}
