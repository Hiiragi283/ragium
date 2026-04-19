package hiiragi283.ragium.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.tag.HTTagsProvider
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

object RagiumTags {
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

        fun apply(factory: HTTagsProvider.BuilderFactory<T>) {
            factory.apply(basic)
            factory.apply(advanced)
            factory.apply(elite)
            factory.apply(ultimate)
            factory
                .apply(base)
                .addTag(basic)
                .addTag(advanced)
                .addTag(elite)
                .addTag(ultimate)
        }
    }

    //    Blocks    //

    object Blocks {
        @JvmField
        val DEVICES: TieredTags<Block> = TieredTags(Registries.BLOCK, RagiumAPI.id("devices"))

        @JvmField
        val GENERATORS: TieredTags<Block> = TieredTags(Registries.BLOCK, RagiumAPI.id("generators"))

        @JvmField
        val MACHINES: TieredTags<Block> = TieredTags(Registries.BLOCK, RagiumAPI.id("machines"))

        @JvmField
        val STORAGES: TagKey<Block> = mod("storages")

        @JvmField
        val STORAGES_CREATIVE: TagKey<Block> = mod("storages", "creative")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(RagiumAPI.id(*path))
    }

    //    Fluids    //

    object Fluids {
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

    object Items {
        @JvmField
        val EXPLOSIVES: TieredTags<Item> = TieredTags(Registries.ITEM, RagiumAPI.id("explosives"))

        @JvmField
        val FOODS_CAN: TagKey<Item> = common("foods", "can")

        @JvmField
        val DEVICES: TieredTags<Item> = TieredTags(Registries.ITEM, RagiumAPI.id("devices"))

        @JvmField
        val GENERATORS: TieredTags<Item> = TieredTags(Registries.ITEM, RagiumAPI.id("generators"))

        @JvmField
        val MACHINES: TieredTags<Item> = TieredTags(Registries.ITEM, RagiumAPI.id("machines"))

        @JvmField
        val STORAGES: TagKey<Item> = mod("storages")

        @JvmField
        val STORAGES_CREATIVE: TagKey<Item> = mod("storages", "creative")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HTConst.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
