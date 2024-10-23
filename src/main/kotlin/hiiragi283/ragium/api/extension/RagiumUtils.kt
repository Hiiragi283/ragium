package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.HTWrappedTable
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.text.NumberFormat
import java.util.*

//    BlockPos    //

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    ChunkPos    //

fun ChunkPos.forEach(yRange: IntRange, action: (BlockPos) -> Unit) {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.forEach { y: Int ->
                action(BlockPos(x, y, z))
            }
        }
    }
}

//    Color    //

fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    FabricLoader    //

fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    Inventory    //

fun openEnderChest(world: World, player: PlayerEntity) {
    if (!world.isClient) {
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x3(
                    syncId,
                    playerInv,
                    playerInv.player.enderChestInventory,
                )
            }, Text.translatable("container.enderchest")),
        )
    }
    world.playSound(
        null,
        player.x,
        player.y,
        player.z,
        SoundEvents.BLOCK_ENDER_CHEST_OPEN,
        SoundCategory.BLOCKS,
        0.5f,
        1.0f,
    )
}

//    ServiceLoader    //

inline fun <reified T : Any> collectInstances(): Iterable<T> = ServiceLoader.load(T::class.java)

//    Recipe    //

fun <T : RecipeInput, U : Recipe<T>> RecipeManager.getFirstMatch(
    type: RecipeType<U>,
    predicate: (RecipeEntry<U>) -> Boolean,
): RecipeEntry<U>? = listAllOfType(type).firstOrNull(predicate)

fun <T : RecipeInput, U : Recipe<T>> RecipeManager.getAllMatches(
    type: RecipeType<U>,
    predicate: (RecipeEntry<U>) -> Boolean,
): List<RecipeEntry<U>> = listAllOfType(type).filter(predicate)

fun RecipeManager.getMachineRecipes(type: HTMachineType, tier: HTMachineTier): List<RecipeEntry<HTMachineRecipe>> =
    getAllMatches(RagiumRecipeTypes.MACHINE) { entry: RecipeEntry<HTMachineRecipe> ->
        entry.value.let { it.machineType == type && it.tier <= tier }
    }

operator fun <T : Recipe<*>> RecipeEntry<T>.component1(): Identifier = this.id

operator fun <T : Recipe<*>> RecipeEntry<T>.component2(): T = this.value

val HTItemIngredient.matchingStacks: List<ItemStack>
    get() = weightedList.map { (item: Item, amount: Long) -> ItemStack(item, amount.toInt()) }

val HTItemResult.itemStack: ItemStack
    get() = resourceAmount.itemStack

//    Registry    //

fun createWrapperLookup(): RegistryWrapper.WrapperLookup = BuiltinRegistries.createWrapperLookup()

//    ScreenHandler    //

fun ScreenHandlerContext.machineInventory(size: Int): Inventory = get { world: World, pos: BlockPos ->
    world.getMachineEntity(pos)?.parent ?: SimpleInventory(size)
}.orElseGet { SimpleInventory(size) }

//    Table    //

fun <R : Any, C : Any, V : Any> tableOf(): HTTable<R, C, V> = HTWrappedTable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTWrappedTable.Mutable<R, C, V> = HTWrappedTable.Mutable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTWrappedTable.Mutable(HashBasedTable.create<R, C, V>()).apply(builderAction)

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    entries.forEach(action)
}

//    Text    //

fun longText(value: Long): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

/*fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    range: Int,
    breaker: Entity,
    tool: ItemStack
) {
    breakRangedBlock(world, pos, breaker.facing, range, breaker, tool::isSuitableFor)
}

fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    direction: Direction,
    range: Int,
    breaker: Entity?,
    predicate: (BlockState) -> Boolean
) {
    when (direction.axis) {
        Direction.Axis.X -> BlockPos.iterate(pos.add(0, range, -range), pos.add(0, -range, range))
        Direction.Axis.Y -> BlockPos.iterate(pos.add(-range, 0, -range), pos.add(range, 0, range))
        Direction.Axis.Z -> BlockPos.iterate(pos.add(-range, range, 0), pos.add(range, -range, 0))
        else -> listOf()
    }
        .filter { predicate(world.getBlockState(it)) }
        .forEach {
            world.setBlockState(it, Blocks.BEDROCK.defaultState)
            // world.breakBlock(it, true, breaker)
        }
}*/
