package hiiragi283.ragium.client.datagen.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.data.model.HTTexturedModels
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.model.RagiumTexturedModels
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.block.Block

/**
 * @see hiiragi283.core.client.datagen.model.HCModelProvider
 */
data object RagiumModelProvider : HTModelProvider() {
    override fun registerModels(manager: ResourceManager) {
        registerBlocks()
        registerItems()
    }

    //    Block    //

    @JvmStatic
    private fun registerBlocks() {
        val basic = "basic"
        val heat = "heat"
        val chemical = "chemical"

        // Storage
        addBlockState(
            createSimpleGenerator(RagiumBlocks.TANK, RagiumBlocks.TANK.blockId),
            RagiumBlocks.TANK,
        )
        addBlockState(
            createSimpleGenerator(RagiumBlocks.CREATIVE_TANK, RagiumBlocks.TANK.blockId),
            RagiumBlocks.CREATIVE_TANK,
        )
        addSimpleBlock(
            RagiumBlocks.UNIVERSAL_CHEST,
            HTTexturedModels.layeredBlock(
                HTConst.MINECRAFT.toId("block", "white_concrete"),
                RagiumBlocks.UNIVERSAL_CHEST.blockId,
            ),
        )
        // Utilities
        // Fluid
        RagiumFluids.REGISTER.asSequence().forEach(::addLiquidBlock)
    }

    /**
     * @see net.minecraft.data.models.model.ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM
     */
    private fun frontMachineModel(
        block: HTHolderLike<Block, *>,
        prefix: String,
        tier: String,
        front: ResourceLocation,
    ) {
        TODO()
    }

    //    Item    //

    @JvmStatic
    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            removeAll(RagiumItems.FOOD_CANS.values)

            removeAll(RagiumItems.MOLDS.values)
            remove(RagiumItems.BLANK_DISC)
            remove(RagiumItems.POTION_DROP)

            removeAll(RagiumItems.UPGRADES.values)
        }.forEach(::addSimpleItemModel)
        // Materials
        addItemModel(
            RagiumItems.RAGI_ALLOY_COMPOUND,
            HTTexturedModels.layeredItem(
                HTConst.MINECRAFT.toId(HTConst.ITEM, "copper_ingot"),
                RagiumItems.RAGI_ALLOY_COMPOUND.itemId,
            ),
        )
        // Foods
        for ((canType: HTFoodCanType, item: HTIdLike) in RagiumItems.FOOD_CANS) {
            addItemModel(
                item,
                HTTexturedModels.flatAlt(RagiumAPI.id(HTConst.ITEM, "food_can", canType.serializedName)),
            )
        }
        // Utilities
        addItemModel(RagiumItems.BLANK_DISC, RagiumTexturedModels.MUSIC_DISC)
        addItemModel(
            RagiumItems.POTION_DROP,
            HTTexturedModels.flatAlt(HTConst.MINECRAFT.toId(HTConst.ITEM, "ghast_tear")),
        )

        for ((moldType: HTMoldType, item: HTIdLike) in RagiumItems.MOLDS) {
            addItemModel(
                item,
                HTTexturedModels.flatAlt(RagiumAPI.id(HTConst.ITEM, "mold", moldType.serializedName)),
            )
        }
        // Upgrades
        registerUpgrades()
        // Buckets
        registerBuckets()
    }

    @JvmStatic
    private fun registerUpgrades() {
        for ((type: RagiumUpgradeType, item: HTIdLike) in RagiumItems.UPGRADES) {
            val base: ResourceLocation = when (type.group) {
                RagiumUpgradeType.Group.CREATIVE -> {
                    addSimpleItemModel(type::getId)
                    continue
                }
                else -> RagiumAPI.id("item", "upgrade", "${type.group.serializedName}_base")
            }
            val textureId: ResourceLocation = RagiumAPI.id("item", "upgrade", type.serializedName)
            addItemModel(item, HTTexturedModels.layeredItem(base, textureId))
        }
    }

    @JvmStatic
    private fun registerBuckets() {
        val dripFluids: List<HTFluidContent> = buildList {
            // Oil
            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.LUBRICANT)
            // Organic
            add(RagiumFluids.CREOSOTE)
            add(RagiumFluids.SYNTHETIC_OIL)
            // Molten
            add(RagiumFluids.MOLTEN_RAGINITE)
            add(RagiumFluids.MOLTEN_STAINLESS_STEEL)
        }
        for (content: HTFluidContent in RagiumFluids.REGISTER.entries) {
            addBucketModel(content, content in dripFluids)
        }
    }
}
