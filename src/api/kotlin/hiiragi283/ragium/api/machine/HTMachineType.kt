package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Consumer

enum class HTMachineType(soundEvent: SoundEvent?, particleHandler: HTMachineParticleHandler?, val altName: String? = null) :
    ItemLike,
    StringRepresentable {
    // Consumer
    FISHER(SoundEvents.FISHING_BOBBER_SPLASH, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE)),

    BEDROCK_MINER(SoundEvents.STONE_BREAK, null),

    // Generator
    STIRLING_GENERATOR(SoundEvents.FIRE_EXTINGUISH, HTMachineParticleHandler.ofSimple(ParticleTypes.LARGE_SMOKE)),

    COMBUSTION_GENERATOR(SoundEvents.FIRE_EXTINGUISH, HTMachineParticleHandler.ofSimple(ParticleTypes.SMOKE)),
    THERMAL_GENERATOR(SoundEvents.LAVA_POP, HTMachineParticleHandler.ofTop(ParticleTypes.LAVA)),

    SOLAR_GENERATOR(null, HTMachineParticleHandler.ofTop(ParticleTypes.ELECTRIC_SPARK)),
    ENCH_GENERATOR(
        SoundEvents.ENCHANTMENT_TABLE_USE,
        HTMachineParticleHandler.ofTop(ParticleTypes.ENCHANT),
        "enchantment_generator",
    ),

    // Processor - Basic
    ALLOY_FURNACE,
    ASSEMBLER,
    AUTO_CHISEL,
    COMPRESSOR,
    ELECTRIC_FURNACE,
    GRINDER,

    // Processor - Heating

    // Processor - Chemical
    EXTRACTOR,
    GROWTH_CHAMBER,
    INFUSER,
    MIXER,
    REFINERY,
    SOLIDIFIER,

    // Processor - Precision
    BREWERY(SoundEvents.BREWING_STAND_BREW, null, "alchemical_brewery"),
    ENCHANTER(
        SoundEvents.ENCHANTMENT_TABLE_USE,
        HTMachineParticleHandler.ofSimple(ParticleTypes.ENCHANT),
        "arcane_enchanter",
    ),
    LASER_ASSEMBLY,
    MULTI_SMELTER,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> = StringRepresentable.fromEnum(HTMachineType::values)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineType> = CODEC.fieldOf("machine_type")

        @JvmStatic
        fun getBlocks(): List<DeferredBlock<*>> = HTMachineType.entries.map(HTMachineType::holder)
    }

    constructor() : this(null, null)

    /**
     * 機械の名前の翻訳キー
     */
    val translationKey: String = "machine_type.$serializedName"

    /**
     * 機械の名前を保持する[MutableComponent]
     */
    val text: MutableComponent
        get() = Component.translatable(translationKey)

    /**
     * 機械の説明文の翻訳キー
     */
    val descriptionKey = "$translationKey.description"

    /**
     * 機械の説明文を保持する[MutableComponent]
     */
    val descriptionText: MutableComponent
        get() = Component.translatable(descriptionKey).withStyle(ChatFormatting.AQUA)

    val holder: DeferredBlock<*> = DeferredBlock.createBlock<Block>(RagiumAPI.id(serializedName))

    fun appendTooltip(consumer: Consumer<Component>, allowDescription: Boolean = true) {
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_NAME,
                    text.withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
        if (allowDescription) {
            consumer.accept(descriptionText)
        }
    }

    override fun asItem(): Item = holder.asItem()

    override fun getSerializedName(): String = altName ?: name.lowercase()
}
