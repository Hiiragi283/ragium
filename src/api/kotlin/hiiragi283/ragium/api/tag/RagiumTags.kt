package hiiragi283.ragium.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.createTagKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

data object RagiumTags {
    data class TieredTags<T : Any>(
        val base: TagKey<T>,
        val basic: TagKey<T>,
        val advanced: TagKey<T>,
        val elite: TagKey<T>,
        val ultimate: TagKey<T>,
    ) {
        constructor(registryKey: RegistryKey<T>, base: ResourceLocation) : this(
            registryKey.createTagKey(base),
            registryKey.createTagKey(base.withSuffix("/${RagiumConst.BASIC}")),
            registryKey.createTagKey(base.withSuffix("/${RagiumConst.ADVANCED}")),
            registryKey.createTagKey(base.withSuffix("/${RagiumConst.ELITE}")),
            registryKey.createTagKey(base.withSuffix("/${RagiumConst.ULTIMATE}")),
        )

        inline fun prepare(consumer: (TagKey<T>) -> Unit) {
            consumer(basic)
            consumer(advanced)
            consumer(elite)
            consumer(ultimate)
        }

        inline fun apply(factory: (TagKey<T>) -> HTTagBuilder<T>) {
            factory(base)
                .addTag(basic)
                .addTag(advanced)
                .addTag(elite)
                .addTag(ultimate)
        }
    }

    //    Blocks    //

    /**
     * @since 21.1.1.0
     */
    data object BlockItems {
        val allTags: List<BlockItemTagKey> field: MutableList<BlockItemTagKey> = mutableListOf()

        // Generators

        // Machines
        @JvmField
        val MACHINES: BlockItemTagKey = mod("machines")

        @JvmField
        val MACHINES_MECHANICAL: BlockItemTagKey = mod("machines", "mechanical")

        @JvmField
        val MACHINES_HEAT: BlockItemTagKey = mod("machines", "heat")

        @JvmField
        val MACHINES_CHEMICAL: BlockItemTagKey = mod("machines", "chemical")

        @JvmField
        val MACHINES_BIO: BlockItemTagKey = mod("machines", "bio")

        @JvmField
        val MACHINES_COLD: BlockItemTagKey = mod("machines", "cold")

        @JvmField
        val MACHINES_ELECTRONICS: BlockItemTagKey = mod("machines", "electronics")

        @JvmField
        val MACHINES_ARCANE: BlockItemTagKey = mod("machines", "arcane")

        // Storages
        @JvmField
        val STORAGES: BlockItemTagKey = mod("storages")

        @JvmField
        val STORAGES_CREATIVE: BlockItemTagKey = mod("storages", "creative")

        @JvmStatic
        private fun common(vararg path: String): BlockItemTagKey = BlockItemTagKey(HTConst.COMMON.toId(*path)).also(allTags::add)

        @JvmStatic
        private fun mod(vararg path: String): BlockItemTagKey = BlockItemTagKey(RagiumAPI.id(*path)).also(allTags::add)
    }

    data object Blocks {
        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(RagiumAPI.id(*path))
    }

    //    Fluids    //

    data object Fluids {
        // Common
        @JvmField
        val ALCOHOL: TagKey<Fluid> = common("alcohol")

        @JvmField
        val BIODIESEL: TagKey<Fluid> = common("biodiesel")

        @JvmField
        val DIESEL: TagKey<Fluid> = common("diesel")

        // Mod
        @JvmField
        val LIQUID_MATTER: TagKey<Fluid> = mod("liquid_matter")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Fluid> = Registries.FLUID.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Fluid> = Registries.FLUID.createTagKey(RagiumAPI.id(*path))
    }

    //    Items    //

    data object Items {
        @JvmField
        val EXPLOSIVES: TieredTags<Item> = TieredTags(Registries.ITEM, RagiumAPI.id("explosives"))

        @JvmField
        val FOODS_CAN: TagKey<Item> = common("foods", "can")

        // Integration
        @JvmField
        val ENRICHED_RAGINITE: TagKey<Item> = mod("enriched", "raginite")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
