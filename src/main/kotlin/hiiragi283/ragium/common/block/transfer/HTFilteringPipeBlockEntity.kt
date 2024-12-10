package hiiragi283.ragium.common.block.transfer

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.api.extension.mappedCodecOf
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumNetworks
import hiiragi283.ragium.common.init.RagiumTexts
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFilteringPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.FILTERING_PIPE, pos, state) {
    companion object {
        @JvmField
        val FLUID_CODEC: Codec<Map<Direction, RegistryEntryList<Fluid>>> = mappedCodecOf(
            Direction.CODEC.fieldOf("direction"),
            RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids"),
        )

        @JvmField
        val ITEM_CODEC: Codec<Map<Direction, RegistryEntryList<Item>>> = mappedCodecOf(
            Direction.CODEC.fieldOf("direction"),
            RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items"),
        )
    }

    init {
        this.tier = HTMachineTier.ADVANCED
    }

    constructor(pos: BlockPos, state: BlockState, type: HTPipeType) : this(pos, state) {
        this.type = type
    }

    private val fluidFilterMap: MutableMap<Direction, RegistryEntryList<Fluid>> = mutableMapOf()
    private val itemFilterMap: MutableMap<Direction, RegistryEntryList<Item>> = mutableMapOf()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        FLUID_CODEC.encodeStart(NbtOps.INSTANCE, fluidFilterMap).ifSuccess { nbt.put("fluid_filter", it) }
        ITEM_CODEC.encodeStart(NbtOps.INSTANCE, itemFilterMap).ifSuccess { nbt.put("item_filter", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        FLUID_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("fluid_filter")).ifSuccess(fluidFilterMap::putAll)
        ITEM_CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("item_filter")).ifSuccess(itemFilterMap::putAll)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val side: Direction = hit.side
        val stack: ItemStack = player.getStackInActiveHand()
        val result: Boolean = when {
            stack.isIn(RagiumItemTags.FLUID_EXPORTER_FILTERS) -> {
                stack.get(RagiumComponentTypes.FLUID_FILTER)?.let { fluidFilterMap[side] = it }
                true
            }

            stack.isIn(RagiumItemTags.ITEM_EXPORTER_FILTERS) -> {
                stack.get(RagiumComponentTypes.ITEM_FILTER)?.let { itemFilterMap[side] = it }
                true
            }

            else -> {
                if (!world.isClient) {
                    player.sendMessage(Text.literal("Side: $side"))
                    fluidFilterMap[side]?.let { player.sendMessage(RagiumTexts.fluidFilter(it), false) }
                    itemFilterMap[side]?.let { player.sendMessage(RagiumTexts.itemFilter(it), false) }
                }
                false
            }
        }
        if (result) {
            if (!world.isClient) {
                markDirty()
                RagiumNetworks.sendFloatingItem(
                    player,
                    stack,
                    ParticleTypes.WAX_ON,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                )
            }
        }
        return ActionResult.success(world.isClient)
    }
}
