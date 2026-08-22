package hiiragi283.ragium.block

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.block.entity.RagiumBlockEntityTypes
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

data object RagiumBlocks {
    @JvmStatic
    private val BLOCK_ONLY = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(BLOCK_ONLY)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    fun machine(): BlockBehaviour.Properties = properties(3.5f, 16f)
        .mapColor(MapColor.COLOR_BLACK)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER)

    @JvmStatic
    private fun registerMachine(type: HTDeferredBlockEntityType<*>, properties: BlockBehaviour.Properties = machine()): HTBasicDeferredBlockAndItem<HTMachineBlock> = REGISTER.registerSimple(
        type.path,
        properties,
        { prop: BlockBehaviour.Properties -> HTMachineBlock(type, prop) },
    )

    //    Ingredient    //

    @JvmField
    val MATERIAL_BLOCKS: Table<HTBlockPart, HTMaterial, HTSimpleDeferredBlockAndItem> = buildTable {
        fun register(part: HTBlockPart, material: HTMaterial, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[part, material] = REGISTER.registerSimple(part.createName(material), blockProp, itemProp)
        }

        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Fuel.CHARCOAL, copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF))
        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Fuel.COAL_COKE, copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY))
        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Gem.ECHO, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN))
        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Gem.RAGI_CRYSTAL, copyOf(Blocks.DIAMOND_BLOCK).mapColor(MapColor.COLOR_RED))
        register(HTBlockPart.STORAGE_BLOCK, HTMaterial.Metal.STEEL, copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY))
    }

    @JvmStatic
    fun getOrThrow(part: HTBlockPart, material: HTMaterial): HTSimpleDeferredBlockAndItem = MATERIAL_BLOCKS[part, material] ?: error("Unregistered block: ${part.createName(material)}")

    //    Machine    //

    // Mechanical

    // Heat
    @JvmField
    val MELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.MELTER)
}
