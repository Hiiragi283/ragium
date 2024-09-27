package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTBaseMachineBlock
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.recipe.HTRecipeBase
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util

interface HTMachineType<T : HTRecipeBase<*>> {
    companion object {
        @JvmField
        val REGISTRY_KEY: RegistryKey<Registry<HTMachineType<*>>> = RegistryKey.ofRegistry(Ragium.id("machine"))

        @JvmField
        val REGISTRY: Registry<HTMachineType<*>> = FabricRegistryBuilder
            .createSimple(REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister()

        @JvmField
        val CODEC: Codec<HTMachineType<*>> = REGISTRY.codec

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType<*>> = PacketCodecs.codec(CODEC)

        @JvmStatic
        fun <T : HTRecipeBase<*>> register(type: HTMachineType<T>): HTMachineType<T> = Registry.register(REGISTRY, type.id, type)

        @JvmStatic
        fun <T : HTRecipeBase<*>> register(id: Identifier, recipeType: RecipeType<T>): HTMachineType<T> =
            register(object : HTMachineType<T> {
                override val id: Identifier = id
                override val recipeType: RecipeType<T> = recipeType
            })

        init {
            register(Default)
        }
    }

    val id: Identifier
    val frontTexId: Identifier
        get() = id.withPath { "block/${it}_front" }

    val recipeType: RecipeType<T>

    val translationKey: String
        get() = Util.createTranslationKey("machine_type", id)
    val text: MutableText
        get() = Text.translatable(translationKey)
    val nameText: MutableText
        get() = Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text).formatted(Formatting.WHITE)

    fun appendTooltip(
        stack: ItemStack,
        lookup: RegistryWrapper.WrapperLookup?,
        consumer: (Text) -> Unit,
        tier: HTMachineTier,
    ) {
        consumer(nameText)
        consumer(tier.tierText)
        consumer(tier.recipeCostText)
        consumer(tier.energyCapacityText)
    }

    fun getBlock(tier: HTMachineTier): HTBaseMachineBlock? = HTMachineBlockRegistry.get(this, tier)

    fun getBlockOrThrow(tier: HTMachineTier): HTBaseMachineBlock = HTMachineBlockRegistry.getOrThrow(this, tier)

    fun createConvertible(): ItemConvertible = ItemConvertible { getBlock(HTMachineTier.PRIMITIVE)?.asItem() ?: Items.AIR }

    //    Default    //

    data object Default : HTMachineType<HTRecipeBase<*>> {
        override val id: Identifier = Ragium.id("default")
        override val recipeType: RecipeType<HTRecipeBase<*>>
            get() = throw IllegalAccessException("Default HTMachineType does not support RecipeType!")
    }
}
