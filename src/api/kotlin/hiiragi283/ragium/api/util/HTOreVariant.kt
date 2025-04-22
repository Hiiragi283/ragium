package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.extension.vanillaId
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

/**
 * 鉱石の母岩部分を管理するクラス
 */
enum class HTOreVariant(baseStoneName: String, val path: String) {
    OVERWORLD("stone", "%s_ore") {
        override fun setupProperty(properties: BlockBehaviour.Properties) {
            properties
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3f, 3f)
        }
    },
    DEEPSLATE("deepslate", "deepslate_%s_ore") {
        override fun setupProperty(properties: BlockBehaviour.Properties) {
            properties
                .mapColor(MapColor.DEEPSLATE)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 3f)
                .sound(SoundType.DEEPSLATE)
        }
    },
    NETHER("netherrack", "nether_%s_ore") {
        override fun setupProperty(properties: BlockBehaviour.Properties) {
            properties
                .mapColor(MapColor.NETHER)
                .requiresCorrectToolForDrops()
                .strength(3f, 3f)
                .sound(SoundType.NETHER_ORE)
        }
    },
    END("end_stone", "end_%s_ore") {
        override fun setupProperty(properties: BlockBehaviour.Properties) {
            properties
                .mapColor(MapColor.SAND)
                .requiresCorrectToolForDrops()
                .strength(3f, 9f)
                .sound(SoundType.AMETHYST)
        }
    },
    ;

    @JvmField
    val baseStoneName: ResourceLocation = vanillaId(baseStoneName)

    abstract fun setupProperty(properties: BlockBehaviour.Properties)

    @JvmField
    val translationKey = "ore_variant.ragium.$baseStoneName"
}
