package hiiragi283.ragium.util

import hiiragi283.ragium.api.registry.HTBlockHolderLike
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

enum class HTOreVariant(val pattern: String, val stoneTex: String) {
    STONE("%s_ore", "block/stone") {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
    },
    DEEP("deepslate_%s_ore", "block/deepslate") {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5f, 3f)
            .sound(SoundType.DEEPSLATE)
    },
    NETHER("nether_%s_ore", "block/netherrack") {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
            .sound(SoundType.NETHER_ORE)
    },
    END("end_%s_ore", "block/end_stone") {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(3f, 9f)
            .sound(SoundType.AMETHYST)
    },
    ;

    abstract fun createProperties(): BlockBehaviour.Properties

    interface HolderLike : HTBlockHolderLike {
        val variant: HTOreVariant
    }
}
