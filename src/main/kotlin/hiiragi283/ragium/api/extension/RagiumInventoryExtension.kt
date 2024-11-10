package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumNetworks
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

//    Inventory    //

fun <T : Inventory> createInventoryCodec(builder: (Int) -> T): Codec<T> = ItemStack.OPTIONAL_CODEC.listOf().xmap(
    { list: List<ItemStack> ->
        builder(list.size).apply {
            list.forEachIndexed(this::setStack)
        }
    },
) { it.iterateStacks() }

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

fun Inventory.modifyStack(slot: Int, mapping: (ItemStack) -> ItemStack) {
    val stackIn: ItemStack = getStack(slot)
    setStack(slot, mapping(stackIn))
}

fun Inventory.toStorage(side: Direction?): InventoryStorage = InventoryStorage.of(this, side)

fun Inventory.getStackOrNull(slot: Int): ItemStack? = if (slot in 0..size()) getStack(slot) else null

fun Inventory.getStackOrEmpty(slot: Int): ItemStack = getStackOrNull(slot) ?: ItemStack.EMPTY

fun Inventory.asMap(): Map<Int, ItemStack> = (0 until size()).associateWith(::getStack)

fun Inventory.iterateStacks(): List<ItemStack> = (0 until size()).map(::getStack)

fun Inventory.sendS2CPacket(player: ServerPlayerEntity, pos: BlockPos) {
    asMap().forEach { (slot: Int, stack: ItemStack) -> RagiumNetworks.sendItemSync(player, pos, slot, stack) }
}

fun Inventory.writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), iterateStacks())
        .result()
        .orElse(NbtList())
        .let { nbt.put("Inventory", it) }
}

fun Inventory.readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
    ItemStack.OPTIONAL_CODEC
        .listOf()
        .parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.get("Inventory"))
        .result()
        .orElse(listOf())
        .forEachIndexed(::setStack)
}
