package hiiragi283.lib.item.alchemy

import com.mojang.serialization.Codec
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.fluid.createOrEmpty
import hiiragi283.lib.item.createOrEmpty
import hiiragi283.lib.registry.VanillaFluidContents
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.data.RagiumDataComponents
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * ポーションの中身と瓶の形状を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class BottledPotionContents(val contents: PotionContents, val bottleType: HTBottleType) : HTHasText {
    companion object {
        @JvmField
        val CODEC: Codec<BottledPotionContents> = HTCodecs.record { instance ->
            instance
                .group(
                    PotionContents.CODEC.fieldOf("contents").forGetter(BottledPotionContents::contents),
                    HTBottleType.CODEC.optionalFieldOf("bottle_type", HTBottleType.DEFAULT).forGetter(BottledPotionContents::bottleType),
                ).apply(instance, ::BottledPotionContents)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BottledPotionContents> = StreamCodec.composite(
            PotionContents.STREAM_CODEC,
            BottledPotionContents::contents,
            HTBottleType.STREAM_CODEC,
            BottledPotionContents::bottleType,
            ::BottledPotionContents,
        )
    }

    @JvmOverloads
    constructor(potion: Holder<Potion>, bottleType: HTBottleType = HTBottleType.DEFAULT) : this(PotionContents(potion), bottleType)

    /**
     * ポーションの値
     */
    val potion: Holder<Potion>? get() = contents.potion().getOrNull()

    /**
     * カスタム色の値
     */
    val customColor: Int? get() = contents.customColor().getOrNull()

    /**
     * カスタムエフェクトの一覧
     */
    val customEffects: List<MobEffectInstance> get() = contents.customEffects()

    /**
     * ポーションも含めたすべてのエフェクトの一覧
     */
    val allEffects: Iterable<MobEffectInstance> get() = contents.allEffects

    /**
     * 保持しているエフェクトが空かどうか
     */
    val isEmpty: Boolean get() = contents == PotionContents.EMPTY || allEffects.none()

    /**
     * 保持しているエフェクトが水に一致するかどうか
     */
    val isWater: Boolean get() = potion == Potions.WATER && bottleType == HTBottleType.DEFAULT

    fun toFluidTemplate(amount: Int = FluidType.BUCKET_VOLUME): FluidStackTemplate? = when (this.isWater) {
        true -> VanillaFluidContents.WATER.toTemplate(amount)
        false -> HTPotionFluidAccess.INSTANCE.fluidContent.toTemplate(
            amount,
            buildDataPatch {
                set(DataComponents.POTION_CONTENTS, this@BottledPotionContents.contents)
                set(RagiumDataComponents.BOTTLE_TYPE, this@BottledPotionContents.bottleType)
            },
        )
    }

    fun toFluidStack(amount: Int = FluidType.BUCKET_VOLUME): FluidStack = toFluidTemplate(amount).createOrEmpty()

    fun toBucketTemplate(): ItemStackTemplate? = when (this.isWater) {
        true -> VanillaFluidContents.WATER.bucketHolder.toTemplate()
        false -> HTPotionFluidAccess.INSTANCE.fluidContent.bucketHolder.toTemplate(
            patch = buildDataPatch {
                set(DataComponents.POTION_CONTENTS, this@BottledPotionContents.contents)
                set(RagiumDataComponents.BOTTLE_TYPE, this@BottledPotionContents.bottleType)
            },
        )
    }

    fun toBucketStack(): ItemStack = toBucketTemplate().createOrEmpty()

    override fun getText(): Text = contents.getName("${bottleType.asItem().descriptionId}.effect.")
}
