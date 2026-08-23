package hiiragi283.ragium.common.block

import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
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

        register(HTBlockPart.STORAGE_BLOCK, VanillaMaterials.CHARCOAL, copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF))
        register(HTBlockPart.STORAGE_BLOCK, CommonMaterials.COAL_COKE, copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY))
        register(HTBlockPart.STORAGE_BLOCK, VanillaMaterials.ECHO, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN))
        register(HTBlockPart.STORAGE_BLOCK, CommonMaterials.STEEL, copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY))
    }

    @JvmStatic
    fun getOrThrow(part: HTBlockPart, material: HTMaterial): HTSimpleDeferredBlockAndItem = MATERIAL_BLOCKS[part, material] ?: error("Unregistered block: ${part.createName(material)}")

    //    Machine    //

    // Mechanical
    @JvmField
    val CRUSHER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val CUTTING_MACHINE: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE)

    // Heat
    @JvmField
    val FREEZER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.FREEZER)

    @JvmField
    val MELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.MELTER)

    @JvmField
    val MACHINES: ListMultiMap<HTMachineType, HTBasicDeferredBlockAndItem<HTMachineBlock>> = buildListMultiMap(sortedMapOf()) {
        put(HTMachineType.MECHANICAL, CRUSHER)
        put(HTMachineType.MECHANICAL, CUTTING_MACHINE)

        put(HTMachineType.HEAT, FREEZER)
        put(HTMachineType.HEAT, MELTER)
    }
}
