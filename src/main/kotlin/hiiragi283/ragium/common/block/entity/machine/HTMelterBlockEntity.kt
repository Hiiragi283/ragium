package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTItemToFluidRecipe>(
        RagiumRecipeTypes.MELTING.get(),
        HTMachineVariant.MELTER,
        pos,
        state,
    ),
    HTFluidInteractable {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)
    private val tank = HTFluidStackTank(variant.tankCapacity, this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(RagiumConst.TANK, tank)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(blockPos, 0, tank.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToFluidRecipe): Boolean =
        tank.canFill(recipe.assembleFluid(input, level.registryAccess()), true)

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToFluidRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tank.fill(recipe.assembleFluid(input, level.registryAccess()), false)
        val stack: ItemStack = input.item()
        if (stack.hasCraftingRemainingItem()) {
            inventory.insertItem(1, stack.craftingRemainingItem, false)
        }
        // インプットを減らす
        inventory.shrinkStack(0, recipe.ingredient, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS)
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.DRAIN_ONLY)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, getFluidHandler(null))

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTMelterMenu = HTMelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
