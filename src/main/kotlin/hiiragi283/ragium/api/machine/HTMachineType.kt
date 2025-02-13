package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.CampfireBlock
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Consumer

enum class HTMachineType(val soundEvent: SoundEvent?, val particleHandler: HTMachineParticleHandler?) :
    ItemLike,
    StringRepresentable {
    // Consumer
    BEDROCK_MINER(SoundEvents.STONE_BREAK, null),
    FISHER(SoundEvents.FISHING_BOBBER_SPLASH, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE)),

    // Generator
    COMBUSTION_GENERATOR(SoundEvents.FIRE_EXTINGUISH, HTMachineParticleHandler.ofSimple(ParticleTypes.SMOKE)),
    SOLAR_GENERATOR(null, HTMachineParticleHandler.ofTop(ParticleTypes.ELECTRIC_SPARK)),
    STIRLING_GENERATOR(SoundEvents.FIRE_EXTINGUISH, HTMachineParticleHandler.ofSimple(ParticleTypes.LARGE_SMOKE)),
    THERMAL_GENERATOR(SoundEvents.LAVA_POP, HTMachineParticleHandler.ofTop(ParticleTypes.LAVA)),

    // Processor - Basic
    ASSEMBLER(SoundEvents.DISPENSER_DISPENSE, null),
    BLAST_FURNACE(
        SoundEvents.BLASTFURNACE_FIRE_CRACKLE,
        HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, front: Direction ->
            CampfireBlock.makeParticles(level, pos.relative(front.opposite, 2), false, false)
        },
    ),
    COMPRESSOR(SoundEvents.ANVIL_USE, null),
    GRINDER(SoundEvents.GRINDSTONE_USE, HTMachineParticleHandler.ofFront(ParticleTypes.CRIT)),
    MULTI_SMELTER(SoundEvents.FURNACE_FIRE_CRACKLE, HTMachineParticleHandler.ofFront(ParticleTypes.SOUL_FIRE_FLAME)),

    // Processor - Advanced
    EXTRACTOR(SoundEvents.PISTON_EXTEND, null),
    GROWTH_CHAMBER(null, HTMachineParticleHandler.ofSimple(ParticleTypes.HAPPY_VILLAGER)),
    INFUSER(SoundEvents.CONDUIT_ACTIVATE, null),
    MIXER(SoundEvents.PLAYER_SWIM, HTMachineParticleHandler.ofTop(ParticleTypes.BUBBLE_POP)),
    REFINERY(null, HTMachineParticleHandler.ofSimple(ParticleTypes.SMOKE)),

    // Processor - Elite
    ALCHEMICAL_BREWERY(SoundEvents.BREWING_STAND_BREW, null),
    ARCANE_ENCHANTER(SoundEvents.ENCHANTMENT_TABLE_USE, HTMachineParticleHandler.ofSimple(ParticleTypes.ENCHANT)),
    LASER_ASSEMBLY(SoundEvents.BEACON_ACTIVATE, HTMachineParticleHandler.ofFront(ParticleTypes.ELECTRIC_SPARK)),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> = StringRepresentable.fromEnum(HTMachineType::values)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineType> = CODEC.fieldOf("machine_type")

        @JvmStatic
        fun getBlocks(): List<DeferredBlock<*>> = HTMachineType.entries.map(HTMachineType::getBlock)
    }

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

    private lateinit var blockCache: DeferredBlock<*>

    /**
     * このキーに紐づいたブロックを返します。
     * @throws IllegalStateException このキーにブロックが登録されていない場合
     */
    fun getBlock(): DeferredBlock<*> {
        if (!::blockCache.isInitialized) {
            blockCache = RagiumAPI.getInstance().getMachineBlock(this)
        }
        return blockCache
    }

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

    override fun asItem(): Item = getBlock().asItem()

    override fun getSerializedName(): String = name.lowercase()
}
