package org.bsdevelopment.mobfarming.utilities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.bsdevelopment.mobfarming.blocks.entity.base.IStorage;
import org.bsdevelopment.mobfarming.utilities.storage.BulkStorage;

import java.util.ArrayList;
import java.util.Map;

public class ItemTransferUtils {
    private static final int MAX_TRANSFER = 4;

    /**
     * Transfers all items from a source IStorage to a destination IItemHandler.
     *
     * @param storage      The source IStorage to extract items from.
     * @param destination The destination IItemHandler to insert items into.
     * @return True if all items were successfully transferred, false if any items could not be moved.
     */
    public static boolean transferAllItems(IStorage storage, IItemHandler destination) {
        boolean allTransferred = true;

        // Iterate over all slots in the bulk storage
        var itemMap = new ArrayList<>(storage.getBulkStorage().getItemMapRaw().entrySet());
        for (Map.Entry<BulkStorage.ItemRecord, Long> entry : itemMap) {
            long beforeCount = entry.getValue();
            while (beforeCount > 0) {
                var key = entry.getKey();
                int itemCount = (int) Math.min(key.toStack(1).getMaxStackSize(), beforeCount);
                var rest = ItemHandlerHelper.insertItem(destination, key.toStack(itemCount), false);
                if (itemCount != rest.getCount()) {
                    // Item transferred.
                    int transferred = itemCount - rest.getCount();

                    long remain = beforeCount - transferred;
                    beforeCount = remain;
                    if (remain > 0) {
                        // the item still exists.
                        storage.getBulkStorage().getItemMapRaw().put(key, remain);
                    } else {
                        // the items all have been transferred.
                        storage.getBulkStorage().getItemMapRaw().remove(key);
                    }
                } else {
                    break;
                }
            }
        }

        storage.saveStorage();
        return allTransferred;
    }

    public static boolean transferAllItems(IItemHandler source, IItemHandler destination) {
        boolean allTransferred = true;

        // Iterate over all slots in the source handler
        for (int i = 0; i < source.getSlots(); i++) {
            ItemStack stackInSlot = source.extractItem(i, Integer.MAX_VALUE, true); // Simulate extraction to see what can be extracted

            if (!stackInSlot.isEmpty()) {
                ItemStack remainingStack = moveStackToDestination(stackInSlot, destination);

                // Actually extract the item now that we know it can be inserted
                source.extractItem(i, stackInSlot.getCount() - remainingStack.getCount(), false);

                // If any items could not be moved, mark as not fully transferred
                if (!remainingStack.isEmpty()) {
                    allTransferred = false;
                }
            }
        }

        return allTransferred;
    }

    /**
     * Attempts to move a stack from the source to the destination handler.
     * 
     * @param stack       The stack to move.
     * @param destination The destination IItemHandler.
     * @return The remaining items that could not be transferred.
     */
    private static ItemStack moveStackToDestination(ItemStack stack, IItemHandler destination) {
        ItemStack remainingStack = stack.copy(); // Make a copy to keep track of the remainder
        
        // Try to insert the entire stack into each destination slot
        for (int j = 0; j < destination.getSlots(); j++) {
            remainingStack = destination.insertItem(j, remainingStack, false);
            
            // If all items were inserted, stop the loop
            if (remainingStack.isEmpty()) {
                break;
            }
        }
        
        return remainingStack; // Return what couldn't be moved
    }
}
